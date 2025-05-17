package com.rw.rra.vms.utils;// src/main/java/rw/rra/management/utils/PlateGeneratorUtility.java

import com.rw.rra.vms.plates.PlateRepository;
import org.springframework.stereotype.Component;

@Component
public class PlateGeneratorUtility {

    private final PlateRepository plateRepository;

    public PlateGeneratorUtility(PlateRepository plateRepository) {
        this.plateRepository = plateRepository;
    }

    /**
     * Generates a unique Rwandan plate number of the form RA{Letter}{3 digits}{Letter},
     * e.g. RAA123B, guaranteed not to collide with existing plates.
     */
    public String generateUniquePlateNumber() {
        String plate;
        do {
            plate = "RA" + randomLetter() + randomDigits(3) + randomLetter();
        } while (plateRepository.findByPlateNumber(plate).isPresent());
        // you can switch this to a logger if you prefer
        System.out.println("Generated unique plate number: " + plate);
        return plate;
    }

    private String randomLetter() {
        char[] letters = {'A', 'B', 'C', 'D', 'E', 'F','G'};
        int idx = (int) (Math.random() * letters.length);
        return String.valueOf(letters[idx]);
    }

    private String randomDigits(int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }
}
