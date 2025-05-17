package com.rw.rra.vms.vehicles;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(String msg) { super(msg); }
    public static VehicleNotFoundException byId(String id) {
        return new VehicleNotFoundException("Vehicle not found with id: " + id);
    }
    public static VehicleNotFoundException byChassis(String ch) {
        return new VehicleNotFoundException("Vehicle not found with chassis: " + ch);
    }
}
