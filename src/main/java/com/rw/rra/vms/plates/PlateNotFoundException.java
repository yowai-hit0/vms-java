package com.rw.rra.vms.plates;

public class PlateNotFoundException extends RuntimeException {
    public PlateNotFoundException(String message) {
        super(message);
    }
    public static PlateNotFoundException byId(String id) {
        return new PlateNotFoundException("Plate not found with id: " + id);
    }
    public static PlateNotFoundException byNumber(String num) {
        return new PlateNotFoundException("Plate not found with number: " + num);
    }
}
