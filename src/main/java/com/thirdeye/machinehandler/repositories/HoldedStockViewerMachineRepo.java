package com.thirdeye.machinehandler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thirdeye.machinehandler.entity.HoldedStockViewerMachine;

@Repository
public interface HoldedStockViewerMachineRepo extends JpaRepository<HoldedStockViewerMachine, Long> {

}

