package com.rw.rra.vms.ownership;

public class TransferException extends RuntimeException {
    public TransferException(String msg) { super(msg); }

    public static TransferException vehicleNotFound(String vid) {
        return new TransferException("Vehicle not found with id: " + vid);
    }
    public static TransferException ownerNotFound(String oid) {
        return new TransferException("Owner not found with id: " + oid);
    }
    public static TransferException plateNotFound(String pid) {
        return new TransferException("Plate not found with id: " + pid);
    }
    public static TransferException invalidPlate(String pid) {
        return new TransferException("Plate is not AVAILABLE for id: " + pid);
    }
}
