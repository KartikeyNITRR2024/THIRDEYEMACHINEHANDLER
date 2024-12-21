package com.thirdeye.machinehandler.services;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.thirdeye.machinehandler.entity.HoldedStockViewerMachine;

public interface HoldedStockViewerMachineService {
	void getAllMicroServicesStatus();
	CompletableFuture<Boolean> checkStatusAsync(HoldedStockViewerMachine holdedStockViewerMachine);
	Map<String, Boolean> getAllHoldedStockViewerMachineByApi();
	void updateHoldedStockViewerMachineList();
}
