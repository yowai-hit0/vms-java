package com.rw.rra.vms.owners;

import com.rw.rra.vms.owners.DTO.OwnerRequestDTO;
import com.rw.rra.vms.owners.DTO.OwnerResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class OwnerService {
    private final OwnerRepository repo;
    private final OwnerMapper mapper;

    public OwnerService(OwnerRepository repo, OwnerMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public OwnerResponseDTO create(OwnerRequestDTO dto) {
        if(repo.findByNationalId(dto.getNationalId()).isPresent()){
            throw new OwnerNotFoundException("owner already exists");
        }
        log.info("Creating owner: {} {}", dto.getFirstName(), dto.getLastName());
        Owner entity = mapper.toEntity(dto);
        entity.setAddress(mapper.toAddressEntity(dto.getAddress()));
        Owner saved = repo.save(entity);
        log.debug("Saved owner id={}", saved.getId());
        return mapper.toDTO(saved);
    }

    public Page<OwnerResponseDTO> listAll(Pageable pageable) {
        log.info("Listing owners page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        return repo.findAll(pageable).map(mapper::toDTO);
    }

    public OwnerResponseDTO getById(UUID id) throws OwnerNotFoundException{
        log.info("Fetching owner by id={}", id);
        Owner owner = repo.findById(id).orElseThrow(() -> OwnerNotFoundException.byId(id.toString()));
        return mapper.toDTO(owner);
    }

    public OwnerResponseDTO getByNationalId(String nid) throws OwnerNotFoundException{
        log.info("Fetching owner by NID={}", nid);
        Owner owner = repo.findByNationalId(nid).orElseThrow(() -> OwnerNotFoundException.byNid(nid));
        return mapper.toDTO(owner);
    }

    public OwnerResponseDTO getByEmail(String email) throws OwnerNotFoundException{
        log.info("Fetching owner by email={}", email);
        Owner owner = repo.findByEmail(email)
                .orElseThrow(() -> new OwnerNotFoundException("Owner not found with email: " + email));
        return mapper.toDTO(owner);
    }

    public OwnerResponseDTO getByPhone(String phone) throws OwnerNotFoundException {
        log.info("Fetching owner by phone={}", phone);
        Owner owner = repo.findByPhoneNumber(phone)
                .orElseThrow(() -> new OwnerNotFoundException("Owner not found with phone: " + phone));
        return mapper.toDTO(owner);
    }
}
