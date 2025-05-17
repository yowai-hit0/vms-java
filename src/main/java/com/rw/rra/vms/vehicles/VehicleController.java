package com.rw.rra.vms.vehicles;

import com.rw.rra.vms.files.CsvService;
import com.rw.rra.vms.files.ExcelService;
import com.rw.rra.vms.vehicles.DTO.VehicleRequestDTO;
import com.rw.rra.vms.vehicles.DTO.VehicleResponseDTO;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
@Slf4j
@Validated
public class VehicleController {

    private final VehicleService service;
    private final ExcelService excelService;
    private final CsvService csvService;
    public VehicleController(VehicleService service, ExcelService excelService, CsvService csvService) {
        this.service = service;
        this.excelService = excelService;
        this.csvService = csvService;
    }

    @PostMapping
    public ResponseEntity<VehicleResponseDTO> register(
            @Valid @RequestBody VehicleRequestDTO dto) {
//        log.info("Received register vehicle");
        return ResponseEntity.ok(service.register(dto));
    }

    @GetMapping
    public ResponseEntity<Page<VehicleResponseDTO>> list(
            Pageable pageable) {
//        log.info("Received list vehicles");
        return ResponseEntity.ok(service.listAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> getById(@PathVariable UUID id) {
//        log.info("Received get vehicle {}", id);
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/search/chassis")
    public ResponseEntity<VehicleResponseDTO> getByChassis(@RequestParam String chassis) {
//        log.info("Received search chassis {}", chassis);
        return ResponseEntity.ok(service.getByChassis(chassis));
    }

    /**
     * Download all vehicles as CSV or Excel.
     * format = "csv" or "excel"
     */
    @GetMapping("/report")
    public void downloadReport(@RequestParam(defaultValue="excel") String format, HttpServletResponse response) throws IOException, IOException {
        List<VehicleResponseDTO> list = service.listAllForReport();

        // build headers & rows
        List<String> headers = List.of(
                "ID","Chassis","Manufacturer","Year","Price","Color","Brand","Model","OwnerId","PlateId","Inspected"
        );
        List<List<String>> rows = new ArrayList<>();
        for (var v : list) {
            rows.add(List.of(
                    v.getId().toString(),
                    v.getChassisNumber(),
                    v.getManufacturerCompany(),
                    v.getManufactureYear().toString(),
                    String.valueOf(v.getPrice()),
                    v.getColor(),
                    v.getBrand(),
                    v.getModelName(),
                    v.getOwnerId().toString(),
                    v.getPlateId().toString(),
                    v.getLastInspectionTime().toString()
            ));
        }

        if ("csv".equalsIgnoreCase(format)) {
            byte[] data = csvService.generateCsv(headers, rows);
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition","attachment; filename=vehicles.csv");
            response.getOutputStream().write(data);

        } else {
            byte[] data = excelService.generateExcel("Vehicles", headers, rows);
            response.setContentType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            );
            response.setHeader("Content-Disposition","attachment; filename=vehicles.xlsx");
            response.getOutputStream().write(data);
        }
    }
}
