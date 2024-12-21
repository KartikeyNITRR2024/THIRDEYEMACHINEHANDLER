package com.thirdeye.machinehandler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thirdeye.machinehandler.entity.MarketViewerMachine;

@Repository
public interface MarketViewerMachineRepo extends JpaRepository<MarketViewerMachine, Long> {

}
