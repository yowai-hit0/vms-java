package com.rw.rra.vms.deductions;

import com.rw.rra.vms.common.exceptions.ResourceNotFoundException;
import com.rw.rra.vms.deductions.dto.DeductionMapper;
import com.rw.rra.vms.deductions.dto.DeductionRequestDTO;
import com.rw.rra.vms.deductions.dto.DeductionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeductionService {
    private final DeductionRepository deductionRepository;
    private final DeductionMapper deductionMapper;

    @Transactional(readOnly = true)
    public List<DeductionResponseDTO> getAllDeductions() {
        return deductionMapper.toResponseDTOList(deductionRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Page<DeductionResponseDTO> getAllDeductions(Pageable pageable) {
        return deductionRepository.findAll(pageable)
                .map(deductionMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public DeductionResponseDTO getDeductionById(UUID id) {
        return deductionRepository.findById(id)
                .map(deductionMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Deduction not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public DeductionResponseDTO getDeductionByName(String name) {
        return deductionRepository.findByDeductionName(name)
                .map(deductionMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Deduction not found with name: " + name));
    }

    @Transactional(readOnly = true)
    public Page<DeductionResponseDTO> getDeductionsByNameContaining(String name, Pageable pageable) {
        return deductionRepository.findByDeductionNameContaining(name, pageable)
                .map(deductionMapper::toResponseDTO);
    }

    @Transactional
    public DeductionResponseDTO createDeduction(DeductionRequestDTO deductionDTO) {
        if (deductionRepository.existsByDeductionName(deductionDTO.getDeductionName())) {
            throw new IllegalArgumentException("Deduction with name " + deductionDTO.getDeductionName() + " already exists");
        }

        Deduction deduction = deductionMapper.toEntity(deductionDTO);
        return deductionMapper.toResponseDTO(deductionRepository.save(deduction));
    }

    @Transactional
    public DeductionResponseDTO updateDeduction(UUID id, DeductionRequestDTO deductionDTO) {
        Deduction deduction = deductionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deduction not found with id: " + id));

        // Check if name is being changed and if new name already exists
        if (!deduction.getDeductionName().equals(deductionDTO.getDeductionName()) && 
            deductionRepository.existsByDeductionName(deductionDTO.getDeductionName())) {
            throw new IllegalArgumentException("Deduction with name " + deductionDTO.getDeductionName() + " already exists");
        }

        deduction.setDeductionName(deductionDTO.getDeductionName());
        deduction.setPercentage(deductionDTO.getPercentage());

        return deductionMapper.toResponseDTO(deductionRepository.save(deduction));
    }

    @Transactional
    public void deleteDeduction(UUID id) {
        if (!deductionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Deduction not found with id: " + id);
        }
        deductionRepository.deleteById(id);
    }

    @Transactional
    public void initializeDefaultDeductions() {
        // Only initialize if no deductions exist
        if (deductionRepository.count() == 0) {
            createDefaultDeduction("Employee Tax", new BigDecimal("30.0"));
            createDefaultDeduction("Pension", new BigDecimal("6.0"));
            createDefaultDeduction("Medical Insurance", new BigDecimal("5.0"));
            createDefaultDeduction("Others", new BigDecimal("5.0"));
            createDefaultDeduction("Housing", new BigDecimal("14.0"));
            createDefaultDeduction("Transport", new BigDecimal("14.0"));
        }
    }

    private void createDefaultDeduction(String name, BigDecimal percentage) {
        Deduction deduction = new Deduction();
        deduction.setDeductionName(name);
        deduction.setPercentage(percentage);
        deductionRepository.save(deduction);
    }
}
