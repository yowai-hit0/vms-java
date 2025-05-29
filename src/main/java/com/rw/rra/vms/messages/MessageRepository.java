package com.rw.rra.vms.messages;

import com.rw.rra.vms.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByEmployee(User employee);
    Page<Message> findByEmployee(User employee, Pageable pageable);

    List<Message> findByEmployeeAndMonthAndYear(User employee, Integer month, Integer year);
    Page<Message> findByEmployeeAndMonthAndYear(User employee, Integer month, Integer year, Pageable pageable);

    List<Message> findBySent(boolean sent);
    Page<Message> findBySent(boolean sent, Pageable pageable);

    List<Message> findByEmployeeAndSent(User employee, boolean sent);
    Page<Message> findByEmployeeAndSent(User employee, boolean sent, Pageable pageable);

    Page<Message> findAll(Pageable pageable);
}
