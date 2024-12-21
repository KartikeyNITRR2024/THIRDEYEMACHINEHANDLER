package com.thirdeye.machinehandler.utils;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thirdeye.machinehandler.repositories.ConfigUsedRepo;
import com.thirdeye.machinehandler.services.HoldedStockViewerMachineService;
import com.thirdeye.machinehandler.services.MarketViewerMachineService;
import com.thirdeye.machinehandler.entity.ConfigUsed;
import com.thirdeye.machinehandler.entity.ConfigTable;
import com.thirdeye.machinehandler.repositories.ConfigTableRepo;

@Component 
public class PropertyLoader {
    private Long configId;

    private static final Logger logger = LoggerFactory.getLogger(PropertyLoader.class);

    @Autowired
    private ConfigTableRepo configTableRepo;
    
    @Autowired
    private ConfigUsedRepo configUsedRepo;
    
    @Autowired
    private MarketViewerMachineService marketViewerMachineService;
    
    @Autowired
    private HoldedStockViewerMachineService holdedStockViewerMachineService;

    public void updatePropertyLoader() {
        try {
        	logger.info("Fetching currently config used.");
            ConfigUsed configUsed = configUsedRepo.findById(1L).get();
            configId = configUsed.getId();
            logger.info("Fetching configuration for configId: {}", configId);
            Optional<ConfigTable> configTable = configTableRepo.findById(configId);
            if (configTable.isPresent()) {
            } else {
                logger.warn("No configuration found for configId: {}", configId);
            }
            marketViewerMachineService.updateMarketViewerMachineList();
            logger.info("Updating all market viewer machine.");
            holdedStockViewerMachineService.updateHoldedStockViewerMachineList();
            logger.info("Updating all holded stock viewer machine.");
        } catch (Exception e) {
            logger.error("An error occurred while fetching configuration: {}", e.getMessage(), e);
        }
    }
}
