package com.rw.rra.vms.ownership;

import com.rw.rra.vms.owners.Owner;
import com.rw.rra.vms.plates.PlateNumber;
import com.rw.rra.vms.vehicles.Vehicle;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "ownership_transfers")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OwnershipTransfer {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_owner_id", nullable = false)
    private Owner fromOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_owner_id", nullable = false)
    private Owner toOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "old_plate_id", nullable = false)
    private PlateNumber oldPlate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_plate_id", nullable = false)
    private PlateNumber newPlate;

    @Column(name = "transfer_amount", nullable = false)
    private double transferAmount;

    @Column(name = "transfer_date", nullable = false)
    private LocalDate transferDate;
}
