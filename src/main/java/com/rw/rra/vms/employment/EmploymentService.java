package com.rw.rra.vms.employment;

import com.rw.rra.vms.common.exceptions.ResourceNotFoundException;
import com.rw.rra.vms.employment.dto.EmploymentMapper;
import com.rw.rra.vms.employment.dto.EmploymentRequestDTO;
import com.rw.rra.vms.employment.dto.EmploymentResponseDTO;
import com.rw.rra.vms.users.User;
import com.rw.rra.vms.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmploymentService {
    private final EmploymentRepository employmentRepository;
    private final UserRepository userRepository;
    private final EmploymentMapper employmentMapper;

    @Transactional(readOnly = true)
    public List<EmploymentResponseDTO> getAllEmployments() {
        return employmentMapper.toResponseDTOList(employmentRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Page<EmploymentResponseDTO> getAllEmployments(Pageable pageable) {
        return employmentRepository.findAll(pageable)
                .map(employmentMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public EmploymentResponseDTO getEmploymentById(UUID id) {
        return employmentRepository.findById(id)
                .map(employmentMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Employment not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<EmploymentResponseDTO> getEmploymentsByEmployee(UUID employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        return employmentMapper.toResponseDTOList(employmentRepository.findByEmployee(employee));
    }

//    @Transactional(readOnly = true)
//    public Page<EmploymentResponseDTO> getEmploymentsByEmployee(UUID employeeId, Pageable pageable) {
//        User employee = userRepository.findById(employeeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
//        return employmentRepository.findByEmployee(employee, pageable)
//                .map(employmentMapper::toResponseDTO);
//    }

    @Transactional(readOnly = true)
    public List<EmploymentResponseDTO> getActiveEmployments() {
        return employmentMapper.toResponseDTOList(employmentRepository.findByStatus(EmploymentStatus.ACTIVE));
    }

//    @Transactional(readOnly = true)
//    public Page<EmploymentResponseDTO> getActiveEmployments(Pageable pageable) {
//        return employmentRepository.findByStatus(EmploymentStatus.ACTIVE, pageable)
//                .map(employmentMapper::toResponseDTO);
//    }

    @Transactional
    public EmploymentResponseDTO createEmployment(EmploymentRequestDTO employmentDTO) {
        User employee = userRepository.findById(employmentDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employmentDTO.getEmployeeId()));

        Employment employment = employmentMapper.toEntity(employmentDTO);
        employment.setEmployee(employee);

        return employmentMapper.toResponseDTO(employmentRepository.save(employment));
    }

    @Transactional
    public EmploymentResponseDTO updateEmployment(UUID id, EmploymentRequestDTO employmentDTO) {
        Employment employment = employmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employment not found with id: " + id));

        User employee = userRepository.findById(employmentDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employmentDTO.getEmployeeId()));

        employment.setEmployee(employee);
        employment.setDepartment(employmentDTO.getDepartment());
        employment.setPosition(employmentDTO.getPosition());
        employment.setBaseSalary(employmentDTO.getBaseSalary());
        employment.setStatus(employmentDTO.getStatus());
        employment.setJoiningDate(employmentDTO.getJoiningDate());

        return employmentMapper.toResponseDTO(employmentRepository.save(employment));
    }

    @Transactional
    public void deleteEmployment(UUID id) {
        if (!employmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employment not found with id: " + id);
        }
        employmentRepository.deleteById(id);
    }
}
