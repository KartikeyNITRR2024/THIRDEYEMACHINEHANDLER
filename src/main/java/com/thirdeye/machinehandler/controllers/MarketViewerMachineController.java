package com.thirdeye.machinehandler.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thirdeye.machinehandler.annotation.AdminRequired;
import com.thirdeye.machinehandler.services.MarketViewerMachineService;
import com.thirdeye.machinehandler.utils.AllMicroservicesData;


@RestController
@RequestMapping("/api/machine")
public class MarketViewerMachineController {

	@Autowired
	AllMicroservicesData allMicroservicesData;
	
	@Autowired
	private MarketViewerMachineService marketViewerMachineService;
	
	@Value("${updaterMicroserviceName}")
    private String updaterMicroserviceName;
	
    private static final Logger logger = LoggerFactory.getLogger(MarketViewerMachineController.class);
    
    @PostMapping("/update/marketviewermachine/{uniqueId}/{uniqueIdOfSender}")
    public ResponseEntity<Boolean> updateAllMarketViewerMachine(@PathVariable("uniqueId") Integer pathUniqueId, @PathVariable("uniqueIdOfSender") Integer pathUniqueIdOfSender) {
        if (pathUniqueId.equals(allMicroservicesData.current.getMicroserviceUniqueId()) && pathUniqueIdOfSender.equals(allMicroservicesData.allMicroservices.get(updaterMicroserviceName).getMicroserviceUniqueId())) {
            logger.info("Status check for uniqueId {} and uniqueIdOfSender {}: Found", allMicroservicesData.current.getMicroserviceUniqueId(), allMicroservicesData.allMicroservices.get(updaterMicroserviceName).getMicroserviceUniqueId());
			try {
				marketViewerMachineService.updateMarketViewerMachineList();
			} catch (Exception e) {
				return ResponseEntity.ok(Boolean.FALSE);
			}
            return ResponseEntity.ok(Boolean.TRUE);
        } else {
            logger.warn("Status check for uniqueId {} or uniqueIdOfSender {}: Not Found", allMicroservicesData.current.getMicroserviceUniqueId(), allMicroservicesData.allMicroservices.get(updaterMicroserviceName).getMicroserviceUniqueId());
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("fromfrontend/marketviewermachine/update/{uniqueId}")
    @AdminRequired
    public ResponseEntity<Boolean> updateAllMarketViewerMachineFromFrontEnd(@PathVariable("uniqueId") Integer pathUniqueId) {
        if (pathUniqueId.equals(allMicroservicesData.current.getMicroserviceUniqueId())) {
            logger.info("Status check for uniqueId {} and uniqueIdOfSender {}: Found", allMicroservicesData.current.getMicroserviceUniqueId());
            try {
				marketViewerMachineService.updateMarketViewerMachineList();
			} catch (Exception e) {
				return ResponseEntity.ok(Boolean.FALSE);
			}
            return ResponseEntity.ok(Boolean.TRUE);
        } else {
            logger.warn("Status check for uniqueId {} or uniqueIdOfSender {}: Not Found", allMicroservicesData.current.getMicroserviceUniqueId());
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("fromfrontend/marketviewermachine/get/{uniqueId}")
    @AdminRequired
    public ResponseEntity<Map<String,Boolean>> getAllMarketViewerMachineFromFrontEnd(@PathVariable("uniqueId") Integer pathUniqueId) {
        if (pathUniqueId.equals(allMicroservicesData.current.getMicroserviceUniqueId())) {
            logger.info("Status check for uniqueId {} and uniqueIdOfSender {}: Found", allMicroservicesData.current.getMicroserviceUniqueId());
            Map<String,Boolean> updateStatus = new HashMap<>();
			updateStatus = marketViewerMachineService.getAllMarketViewerMachineByApi();
            return ResponseEntity.ok(updateStatus);
        } else {
            logger.warn("Status check for uniqueId {} or uniqueIdOfSender {}: Not Found", allMicroservicesData.current.getMicroserviceUniqueId());
            return ResponseEntity.notFound().build();
        }
    }
}


