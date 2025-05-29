package com.rw.rra.vms.messages;

import com.rw.rra.vms.common.exceptions.ResourceNotFoundException;
import com.rw.rra.vms.email.EmailService;
import com.rw.rra.vms.messages.dto.MessageMapper;
import com.rw.rra.vms.messages.dto.MessageRequestDTO;
import com.rw.rra.vms.messages.dto.MessageResponseDTO;
import com.rw.rra.vms.users.User;
import com.rw.rra.vms.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;
    private final EmailService emailService;

//    @Transactional(readOnly = true)
//    public List<MessageResponseDTO> getAllMessages() {
//        return messageMapper.toResponseDTOList(messageRepository.findAll());
//    }

    @Transactional(readOnly = true)
    public Page<MessageResponseDTO> getAllMessages(Pageable pageable) {
        return messageRepository.findAll(pageable)
                .map(messageMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public MessageResponseDTO getMessageById(UUID id) {
        return messageRepository.findById(id)
                .map(messageMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<MessageResponseDTO> getMessagesByEmployee(UUID employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        return messageMapper.toResponseDTOList(messageRepository.findByEmployee(employee));
    }

//    @Transactional(readOnly = true)
//    public Page<MessageResponseDTO> getMessagesByEmployee(UUID employeeId, Pageable pageable) {
//        User employee = userRepository.findById(employeeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
//        return messageRepository.findByEmployee(employee, pageable)
//                .map(messageMapper::toResponseDTO);
//    }

    @Transactional(readOnly = true)
    public List<MessageResponseDTO> getMessagesByEmployeeAndMonthAndYear(UUID employeeId, Integer month, Integer year) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        return messageMapper.toResponseDTOList(messageRepository.findByEmployeeAndMonthAndYear(employee, month, year));
    }

//    @Transactional(readOnly = true)
//    public Page<MessageResponseDTO> getMessagesByEmployeeAndMonthAndYear(UUID employeeId, Integer month, Integer year, Pageable pageable) {
//        User employee = userRepository.findById(employeeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
//        return messageRepository.findByEmployeeAndMonthAndYear(employee, month, year, pageable)
//                .map(messageMapper::toResponseDTO);
//    }
//
//    @Transactional(readOnly = true)
//    public Page<MessageResponseDTO> getMessagesBySent(boolean sent, Pageable pageable) {
//        return messageRepository.findBySent(sent, pageable)
//                .map(messageMapper::toResponseDTO);
//    }
//
//    @Transactional(readOnly = true)
//    public Page<MessageResponseDTO> getMessagesByEmployeeAndSent(UUID employeeId, boolean sent, Pageable pageable) {
//        User employee = userRepository.findById(employeeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
//        return messageRepository.findByEmployeeAndSent(employee, sent, pageable)
//                .map(messageMapper::toResponseDTO);
//    }

    @Transactional
    public MessageResponseDTO createMessage(MessageRequestDTO messageDTO) {
        User employee = userRepository.findById(messageDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + messageDTO.getEmployeeId()));

        Message message = messageMapper.toEntity(messageDTO);
        message.setEmployee(employee);
        message.setSent(false);

        return messageMapper.toResponseDTO(messageRepository.save(message));
    }

    @Transactional
    public void sendMessage(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));

        if (!message.isSent()) {
            // Send email
            String to = message.getEmployee().getEmail();
            String name = message.getEmployee().getFirstName();
            String body = message.getMessage();

            emailService.sendSalaryNotification(to, name, body);

            // Update message status
            message.setSent(true);
            message.setSentAt(LocalDateTime.now());
            messageRepository.save(message);
        }
    }

    @Scheduled(fixedRate = 60000) // Run every minute
    @Transactional
    public void sendPendingMessages() {
        List<Message> pendingMessages = messageRepository.findBySent(false);

        for (Message message : pendingMessages) {
            try {
                // Send email
                String to = message.getEmployee().getEmail();
                String name = message.getEmployee().getFirstName();
                String body = message.getMessage();

                emailService.sendSalaryNotification(to, name, body);

                // Update message status
                message.setSent(true);
                message.setSentAt(LocalDateTime.now());
                messageRepository.save(message);
            } catch (Exception e) {
                // Log error but continue with other messages
                System.err.println("Failed to send message ID: " + message.getId() + ". Error: " + e.getMessage());
            }
        }
    }
}
