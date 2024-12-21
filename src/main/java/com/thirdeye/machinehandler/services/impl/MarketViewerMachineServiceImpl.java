package com.thirdeye.machinehandler.services.impl;

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

import com.thirdeye.machinehandler.services.MarketViewerMachineService;
import com.thirdeye.machinehandler.entity.MarketViewerMachine;
import com.thirdeye.machinehandler.repositories.MarketViewerMachineRepo;

@Service
public class MarketViewerMachineServiceImpl implements MarketViewerMachineService {

    @Value("${statusTimeGapInSeconds}")
    private Long statusTimeGapInSeconds;

    @Autowired
    private RestTemplate restTemplate;

    private final List<MarketViewerMachine> marketViewerMachineList = new ArrayList<>();

    @Autowired
    private MarketViewerMachineRepo marketViewerMachineRepo;

    private static final Logger logger = LoggerFactory.getLogger(MarketViewerMachineServiceImpl.class);

    @Override
    public void updateMarketViewerMachineList() {
        synchronized (marketViewerMachineList) {
            List<MarketViewerMachine> updatedList = marketViewerMachineRepo.findAll();
            marketViewerMachineList.clear();
            marketViewerMachineList.addAll(updatedList);
        }
    }

    @Override
    public Map<String, Boolean> getAllMarketViewerMachineByApi() {
        Map<String, CompletableFuture<Boolean>> futureMap;

        synchronized (marketViewerMachineList) {
            futureMap = marketViewerMachineList.stream()
                .collect(Collectors.toMap(
                		machine -> "Market Viewer Machine " + machine.getMachineNo(),
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
    public CompletableFuture<Boolean> checkStatusAsync(MarketViewerMachine marketViewerMachine) {
        Boolean status;
        try {
        	ResponseEntity<String> response = restTemplate.getForEntity(
                    marketViewerMachine.getMachineUrl(),
                    String.class
                );

                if (response.getBody() != null) {
                    status = Boolean.TRUE;
                } else {
                    status = Boolean.FALSE;
                }
        } catch (Exception e) {
            logger.error("Error while checking status for machine: " + marketViewerMachine.getMachineNo(), e);
            status = Boolean.FALSE;
        }
        return CompletableFuture.completedFuture(status);
    }

    @Scheduled(fixedRateString = "${statusTimeGapInSeconds}000", zone = "Asia/Kolkata")
    @Override
    public void getAllMicroServicesStatus() {
        logger.info("Checking the status of all market viewer machines...");
        Map<String, Boolean> statusMap = getAllMarketViewerMachineByApi();
        logger.info("Status check completed: " + statusMap);
    }
}
