package org.example.repository;

import org.example.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findAllByOrderByCreatedAtDesc();
}
