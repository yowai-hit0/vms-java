package com.rw.rra.vms.owners;


public class OwnerNotFoundException extends RuntimeException {
    public OwnerNotFoundException(String message) {
        super(message);
    }

    public static OwnerNotFoundException byId(String id) {
        return new OwnerNotFoundException("Owner not found with id: " + id);
    }

    public static OwnerNotFoundException byNid(String nid) {
        return new OwnerNotFoundException("Owner not found with national ID: " + nid);
    }
}
