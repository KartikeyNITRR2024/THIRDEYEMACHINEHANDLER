package com.thirdeye.machinehandler.entity;

import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Marketviewermachine")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MarketViewerMachine {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Column(name = "machineno", nullable = false)
    private Integer machineNo;
    
    @Column(name = "machineurl", nullable = false)
    private String machineUrl;
}

