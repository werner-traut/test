package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Salary;
import com.mycompany.myapp.repository.SalaryRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Salary}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SalaryResource {

    private final Logger log = LoggerFactory.getLogger(SalaryResource.class);

    private static final String ENTITY_NAME = "salary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SalaryRepository salaryRepository;

    public SalaryResource(SalaryRepository salaryRepository) {
        this.salaryRepository = salaryRepository;
    }

    /**
     * {@code POST  /salaries} : Create a new salary.
     *
     * @param salary the salary to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salary, or with status {@code 400 (Bad Request)} if the salary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/salaries")
    public ResponseEntity<Salary> createSalary(@Valid @RequestBody Salary salary) throws URISyntaxException {
        log.debug("REST request to save Salary : {}", salary);
        if (salary.getId() != null) {
            throw new BadRequestAlertException("A new salary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Salary result = salaryRepository.save(salary);
        return ResponseEntity.created(new URI("/api/salaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /salaries} : Updates an existing salary.
     *
     * @param salary the salary to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salary,
     * or with status {@code 400 (Bad Request)} if the salary is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salary couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/salaries")
    public ResponseEntity<Salary> updateSalary(@Valid @RequestBody Salary salary) throws URISyntaxException {
        log.debug("REST request to update Salary : {}", salary);
        if (salary.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Salary result = salaryRepository.save(salary);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, salary.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /salaries} : get all the salaries.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salaries in body.
     */
    @GetMapping("/salaries")
    public List<Salary> getAllSalaries() {
        log.debug("REST request to get all Salaries");
        return salaryRepository.findAll();
    }

    /**
     * {@code GET  /salaries/:id} : get the "id" salary.
     *
     * @param id the id of the salary to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salary, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/salaries/{id}")
    public ResponseEntity<Salary> getSalary(@PathVariable Long id) {
        log.debug("REST request to get Salary : {}", id);
        Optional<Salary> salary = salaryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(salary);
    }

    /**
     * {@code DELETE  /salaries/:id} : delete the "id" salary.
     *
     * @param id the id of the salary to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/salaries/{id}")
    public ResponseEntity<Void> deleteSalary(@PathVariable Long id) {
        log.debug("REST request to delete Salary : {}", id);
        salaryRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
