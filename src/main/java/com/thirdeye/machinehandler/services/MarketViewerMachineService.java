package com.thirdeye.machinehandler.services;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.thirdeye.machinehandler.entity.MarketViewerMachine;

public interface MarketViewerMachineService {
	void updateMarketViewerMachineList();
	void getAllMicroServicesStatus();
	CompletableFuture<Boolean> checkStatusAsync(MarketViewerMachine marketViewerMachine);
	Map<String, Boolean> getAllMarketViewerMachineByApi();
}
