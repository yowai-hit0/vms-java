package com.rw.rra.vms.ownership;

import com.rw.rra.vms.files.CsvService;
import com.rw.rra.vms.files.ExcelService;
import com.rw.rra.vms.ownership.DTO.TransferRequestDTO;
import com.rw.rra.vms.ownership.DTO.TransferResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transfers")
@Slf4j
@Validated
public class TransferController {

    private final TransferService service;
    private final ExcelService excelService;
    private final CsvService csvService;

    public TransferController(TransferService service, ExcelService excelService, CsvService csvService) {
        this.service = service;
        this.excelService = excelService;
        this.csvService = csvService;
    }

    /** Perform a vehicle ownership transfer */
    @PostMapping
    public ResponseEntity<TransferResponseDTO> transfer(
            @Valid @RequestBody TransferRequestDTO dto) {
        log.info("Received transfer request");
        return ResponseEntity.ok(service.transfer(dto));
    }

    /** Get full transfer history for a vehicle */
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<TransferResponseDTO>> history(
            @PathVariable UUID vehicleId) {
        log.info("Received history request for vehicle {}", vehicleId);
        return ResponseEntity.ok(service.historyByVehicle(vehicleId));
    }

    @GetMapping("/report")
    public void downloadReport(@RequestParam(defaultValue="excel") String format,
                               HttpServletResponse response) throws IOException {
        List<TransferResponseDTO> list = service.listAllForReport();

        List<String> headers = List.of(
                "ID","VehicleId","FromOwnerId","ToOwnerId",
                "OldPlateId","NewPlateId","Amount","Date"
        );
        List<List<String>> rows = new ArrayList<>();
        for (var t : list) {
            rows.add(List.of(
                    t.getId().toString(),
                    t.getVehicleId().toString(),
                    t.getFromOwnerId().toString(),
                    t.getToOwnerId().toString(),
                    t.getOldPlateId().toString(),
                    t.getNewPlateId().toString(),
                    String.valueOf(t.getTransferAmount()),
                    t.getTransferDate().toString()
            ));
        }

        if ("csv".equalsIgnoreCase(format)) {
            byte[] data = csvService.generateCsv(headers, rows);
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition","attachment; filename=transfers.csv");
            response.getOutputStream().write(data);

        } else {
            byte[] data = excelService.generateExcel("Transfers", headers, rows);
            response.setContentType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            );
            response.setHeader("Content-Disposition","attachment; filename=transfers.xlsx");
            response.getOutputStream().write(data);
        }
    }
}
