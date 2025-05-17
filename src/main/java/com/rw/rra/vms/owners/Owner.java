package com.rw.rra.vms.owners;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import com.rw.rra.vms.owners.Address;
import com.rw.rra.vms.ownership.OwnershipTransfer;
import com.rw.rra.vms.plates.PlateNumber;
import com.rw.rra.vms.vehicles.Vehicle;

import java.util.List;
import java.util.UUID;

/**
 * Represents a vehicle owner in the system.
 * Tracks personal details, address, their plates, vehicles, and transfers.
 */
@Entity
@Table(
        name = "owners",
        indexes = {
                @Index(name = "idx_owner_email_unq",       columnList = "email",       unique = true),
                @Index(name = "idx_owner_phonenumber_unq", columnList = "phone_number",unique = true),
                @Index(name = "idx_owner_nationalid_unq",  columnList = "national_id", unique = true)
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true, length = 10)
    private String phoneNumber;

    @Column(name = "national_id", nullable = false, unique = true, length = 16)
    private String nationalId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "district", column = @Column(name = "owner_district", nullable = false, length = 100)),
            @AttributeOverride(name = "province", column = @Column(name = "owner_province", nullable = false, length = 100)),
            @AttributeOverride(name = "street",   column = @Column(name = "owner_street",   nullable = false, length = 150))
    })
    private Address address;

    /**
     * All plate numbers ever issued to this owner.
     */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PlateNumber> plateNumbers;

    /**
     * All vehicles currently registered under this owner.
     */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Vehicle> vehicles;

    /**
     * Transfers where this owner was the seller.
     */
    @OneToMany(mappedBy = "fromOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OwnershipTransfer> transfersFrom;

    /**
     * Transfers where this owner was the buyer.
     */
    @OneToMany(mappedBy = "toOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OwnershipTransfer> transfersTo;
}
