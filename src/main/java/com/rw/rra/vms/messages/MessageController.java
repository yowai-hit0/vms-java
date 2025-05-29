package com.rw.rra.vms.messages;

import com.rw.rra.vms.messages.dto.MessageRequestDTO;
import com.rw.rra.vms.messages.dto.MessageResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageService messageService;

//    @GetMapping
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
//    public ResponseEntity<List<MessageResponseDTO>> getAllMessages() {
//        return ResponseEntity.ok(messageService.getAllMessages());
//    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Page<MessageResponseDTO>> getAllMessagesPaged(Pageable pageable) {
        return ResponseEntity.ok(messageService.getAllMessages(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<MessageResponseDTO> getMessageById(@PathVariable UUID id) {
        return ResponseEntity.ok(messageService.getMessageById(id));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<List<MessageResponseDTO>> getMessagesByEmployee(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(messageService.getMessagesByEmployee(employeeId));
    }

//    @GetMapping("/employee/{employeeId}/paged")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
//    public ResponseEntity<Page<MessageResponseDTO>> getMessagesByEmployeePaged(
//            @PathVariable UUID employeeId,
//            Pageable pageable) {
//        return ResponseEntity.ok(messageService.getMessagesByEmployee(employeeId, pageable));
//    }

    @GetMapping("/employee/{employeeId}/month/{month}/year/{year}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<List<MessageResponseDTO>> getMessagesByEmployeeAndMonthAndYear(
            @PathVariable UUID employeeId,
            @PathVariable Integer month,
            @PathVariable Integer year) {
        return ResponseEntity.ok(messageService.getMessagesByEmployeeAndMonthAndYear(employeeId, month, year));
    }

//    @GetMapping("/employee/{employeeId}/month/{month}/year/{year}/paged")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
//    public ResponseEntity<Page<MessageResponseDTO>> getMessagesByEmployeeAndMonthAndYearPaged(
//            @PathVariable UUID employeeId,
//            @PathVariable Integer month,
//            @PathVariable Integer year,
//            Pageable pageable) {
//        return ResponseEntity.ok(messageService.getMessagesByEmployeeAndMonthAndYear(employeeId, month, year, pageable));
//    }


//    @GetMapping("/sent/{sent}")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
//    public ResponseEntity<Page<MessageResponseDTO>> getMessagesBySent(
//            @PathVariable boolean sent,
//            Pageable pageable) {
//        return ResponseEntity.ok(messageService.getMessagesBySent(sent, pageable));
//    }

//    @GetMapping("/employee/{employeeId}/sent/{sent}")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
//    public ResponseEntity<Page<MessageResponseDTO>> getMessagesByEmployeeAndSent(
//            @PathVariable UUID employeeId,
//            @PathVariable boolean sent,
//            Pageable pageable) {
//        return ResponseEntity.ok(messageService.getMessagesByEmployeeAndSent(employeeId, sent, pageable));
//    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<MessageResponseDTO> createMessage(
            @RequestBody MessageRequestDTO messageDTO,
            UriComponentsBuilder uriBuilder) {
        MessageResponseDTO createdMessage = messageService.createMessage(messageDTO);
        var uri = uriBuilder.path("/api/v1/messages/{id}").buildAndExpand(createdMessage.getId()).toUri();
        return ResponseEntity.created(uri).body(createdMessage);
    }

    @PostMapping("/{id}/send")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Void> sendMessage(@PathVariable UUID id) {
        messageService.sendMessage(id);
        return ResponseEntity.ok().build();
    }
}
