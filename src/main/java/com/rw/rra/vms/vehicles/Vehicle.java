package com.rw.rra.vms.vehicles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rw.rra.vms.owners.Owner;
import com.rw.rra.vms.ownership.OwnershipTransfer;
import com.rw.rra.vms.plates.PlateNumber;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "vehicles",
        uniqueConstraints = @UniqueConstraint(columnNames = "chassis_number")
)
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Vehicle {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "chassis_number", nullable = false, unique = true, length = 50)
    private String chassisNumber;

    @Column(name = "manufacturer_company", nullable = false, length = 100)
    private String manufacturerCompany;

    @Column(name = "manufacture_year", nullable = false)
    private Year manufactureYear;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false, length = 30)
    private String color;

    @Column(nullable = false, length = 50)
    private String brand;

    @Column(name = "model_name", nullable = false, length = 100)
    private String modelName;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plate_number_id", unique = true)
    private PlateNumber plateNumber;

    @Column(name = "last_inspection_time", nullable = false)
    private LocalDateTime lastInspectionTime = LocalDateTime.now();

    @JsonIgnore
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OwnershipTransfer> ownershipTransfers;
}
