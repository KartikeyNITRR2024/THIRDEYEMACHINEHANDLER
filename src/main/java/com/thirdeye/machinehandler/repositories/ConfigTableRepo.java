package com.thirdeye.machinehandler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thirdeye.machinehandler.entity.ConfigTable;


@Repository
public interface ConfigTableRepo extends JpaRepository<ConfigTable, Long> {
}
