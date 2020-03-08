package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ExpenseInstance;
import com.mycompany.myapp.repository.ExpenseInstanceRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ExpenseInstance}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ExpenseInstanceResource {

    private final Logger log = LoggerFactory.getLogger(ExpenseInstanceResource.class);

    private static final String ENTITY_NAME = "expenseInstance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExpenseInstanceRepository expenseInstanceRepository;

    public ExpenseInstanceResource(ExpenseInstanceRepository expenseInstanceRepository) {
        this.expenseInstanceRepository = expenseInstanceRepository;
    }

    /**
     * {@code POST  /expense-instances} : Create a new expenseInstance.
     *
     * @param expenseInstance the expenseInstance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expenseInstance, or with status {@code 400 (Bad Request)} if the expenseInstance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/expense-instances")
    public ResponseEntity<ExpenseInstance> createExpenseInstance(@Valid @RequestBody ExpenseInstance expenseInstance) throws URISyntaxException {
        log.debug("REST request to save ExpenseInstance : {}", expenseInstance);
        if (expenseInstance.getId() != null) {
            throw new BadRequestAlertException("A new expenseInstance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExpenseInstance result = expenseInstanceRepository.save(expenseInstance);
        return ResponseEntity.created(new URI("/api/expense-instances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /expense-instances} : Updates an existing expenseInstance.
     *
     * @param expenseInstance the expenseInstance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expenseInstance,
     * or with status {@code 400 (Bad Request)} if the expenseInstance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expenseInstance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/expense-instances")
    public ResponseEntity<ExpenseInstance> updateExpenseInstance(@Valid @RequestBody ExpenseInstance expenseInstance) throws URISyntaxException {
        log.debug("REST request to update ExpenseInstance : {}", expenseInstance);
        if (expenseInstance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExpenseInstance result = expenseInstanceRepository.save(expenseInstance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, expenseInstance.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /expense-instances} : get all the expenseInstances.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of expenseInstances in body.
     */
    @GetMapping("/expense-instances")
    public List<ExpenseInstance> getAllExpenseInstances() {
        log.debug("REST request to get all ExpenseInstances");
        return expenseInstanceRepository.findAll();
    }

    /**
     * {@code GET  /expense-instances/:id} : get the "id" expenseInstance.
     *
     * @param id the id of the expenseInstance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expenseInstance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/expense-instances/{id}")
    public ResponseEntity<ExpenseInstance> getExpenseInstance(@PathVariable Long id) {
        log.debug("REST request to get ExpenseInstance : {}", id);
        Optional<ExpenseInstance> expenseInstance = expenseInstanceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(expenseInstance);
    }

    /**
     * {@code DELETE  /expense-instances/:id} : delete the "id" expenseInstance.
     *
     * @param id the id of the expenseInstance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/expense-instances/{id}")
    public ResponseEntity<Void> deleteExpenseInstance(@PathVariable Long id) {
        log.debug("REST request to delete ExpenseInstance : {}", id);
        expenseInstanceRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
