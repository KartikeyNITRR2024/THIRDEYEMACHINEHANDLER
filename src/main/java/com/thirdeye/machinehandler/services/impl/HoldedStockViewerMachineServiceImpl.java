package com.thirdeye.machinehandler.services.impl;
import com.thirdeye.machinehandler.entity.HoldedStockViewerMachine;
import com.thirdeye.machinehandler.repositories.HoldedStockViewerMachineRepo;
import com.thirdeye.machinehandler.services.HoldedStockViewerMachineService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HoldedStockViewerMachineServiceImpl implements HoldedStockViewerMachineService {

    @Value("${statusTimeGapInSeconds}")
    private Long statusTimeGapInSeconds;

    @Autowired
    private RestTemplate restTemplate;

    private final List<HoldedStockViewerMachine> holdedStockViewerMachineList = new ArrayList<>();

    @Autowired
    private HoldedStockViewerMachineRepo holdedStockViewerMachineRepo;

    private static final Logger logger = LoggerFactory.getLogger(HoldedStockViewerMachineServiceImpl.class);

    @Override
    public void updateHoldedStockViewerMachineList() {
        synchronized (holdedStockViewerMachineList) {
            List<HoldedStockViewerMachine> updatedList = holdedStockViewerMachineRepo.findAll();
            holdedStockViewerMachineList.clear();
            holdedStockViewerMachineList.addAll(updatedList);
        }
    }

    @Override
    public Map<String, Boolean> getAllHoldedStockViewerMachineByApi() {
        Map<String, CompletableFuture<Boolean>> futureMap;

        synchronized (holdedStockViewerMachineList) {
            futureMap = holdedStockViewerMachineList.stream()
                .collect(Collectors.toMap(
                		machine -> "Holded Stock Viewer Machine " + machine.getMachineNo(),
                    this::checkStatusAsync
                ));
        }

        Map<String, Boolean> statusMap = new HashMap<>();
        futureMap.forEach((machineNo, future) -> {
            try {
                statusMap.put(machineNo, future.get());
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error while retrieving status for machine " + machineNo, e);
                statusMap.put(machineNo, Boolean.FALSE);
            }
        });

        return statusMap;
    }

    @Async("StatusCheckerAsyncThread")
    @Override
    public CompletableFuture<Boolean> checkStatusAsync(HoldedStockViewerMachine holdedStockViewerMachine) {
        Boolean status;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
            		holdedStockViewerMachine.getMachineUrl(),
            		String.class
            );
            if (response.getBody() != null) {
                status = Boolean.TRUE;
            } else {
                status = Boolean.FALSE;
            }
        } catch (Exception e) {
            logger.error("Error while checking status for machine: " + holdedStockViewerMachine.getMachineNo(), e);
            status = Boolean.FALSE;
        }
        return CompletableFuture.completedFuture(status);
    }

    @Scheduled(fixedRateString = "${statusTimeGapInSeconds}000", zone = "Asia/Kolkata")
    @Override
    public void getAllMicroServicesStatus() {
        logger.info("Checking the status of all holded stock viewer machines...");
        Map<String, Boolean> statusMap = getAllHoldedStockViewerMachineByApi();
        logger.info("Status check completed: " + statusMap);
    }
}

