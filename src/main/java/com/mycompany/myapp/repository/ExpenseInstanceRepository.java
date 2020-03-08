package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ExpenseInstance;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ExpenseInstance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpenseInstanceRepository extends JpaRepository<ExpenseInstance, Long> {
}
