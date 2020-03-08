package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TestApp;
import com.mycompany.myapp.config.TestSecurityConfiguration;
import com.mycompany.myapp.domain.Salary;
import com.mycompany.myapp.repository.SalaryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.Period;
/**
 * Integration tests for the {@link SalaryResource} REST controller.
 */
@SpringBootTest(classes = { TestApp.class, TestSecurityConfiguration.class })

@AutoConfigureMockMvc
@WithMockUser
public class SalaryResourceIT {

    private static final LocalDate DEFAULT_SALARY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SALARY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Period DEFAULT_PERIOD = Period.CLOSED_PERIOD;
    private static final Period UPDATED_PERIOD = Period.CURRENT_PERIOD;

    private static final Float DEFAULT_AMOUNT = 1F;
    private static final Float UPDATED_AMOUNT = 2F;

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalaryMockMvc;

    private Salary salary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salary createEntity(EntityManager em) {
        Salary salary = new Salary()
            .salaryDate(DEFAULT_SALARY_DATE)
            .period(DEFAULT_PERIOD)
            .amount(DEFAULT_AMOUNT);
        return salary;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salary createUpdatedEntity(EntityManager em) {
        Salary salary = new Salary()
            .salaryDate(UPDATED_SALARY_DATE)
            .period(UPDATED_PERIOD)
            .amount(UPDATED_AMOUNT);
        return salary;
    }

    @BeforeEach
    public void initTest() {
        salary = createEntity(em);
    }

    @Test
    @Transactional
    public void createSalary() throws Exception {
        int databaseSizeBeforeCreate = salaryRepository.findAll().size();

        // Create the Salary
        restSalaryMockMvc.perform(post("/api/salaries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(salary)))
            .andExpect(status().isCreated());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeCreate + 1);
        Salary testSalary = salaryList.get(salaryList.size() - 1);
        assertThat(testSalary.getSalaryDate()).isEqualTo(DEFAULT_SALARY_DATE);
        assertThat(testSalary.getPeriod()).isEqualTo(DEFAULT_PERIOD);
        assertThat(testSalary.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createSalaryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salaryRepository.findAll().size();

        // Create the Salary with an existing ID
        salary.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalaryMockMvc.perform(post("/api/salaries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(salary)))
            .andExpect(status().isBadRequest());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSalaryDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryRepository.findAll().size();
        // set the field null
        salary.setSalaryDate(null);

        // Create the Salary, which fails.

        restSalaryMockMvc.perform(post("/api/salaries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(salary)))
            .andExpect(status().isBadRequest());

        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPeriodIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryRepository.findAll().size();
        // set the field null
        salary.setPeriod(null);

        // Create the Salary, which fails.

        restSalaryMockMvc.perform(post("/api/salaries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(salary)))
            .andExpect(status().isBadRequest());

        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryRepository.findAll().size();
        // set the field null
        salary.setAmount(null);

        // Create the Salary, which fails.

        restSalaryMockMvc.perform(post("/api/salaries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(salary)))
            .andExpect(status().isBadRequest());

        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSalaries() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get all the salaryList
        restSalaryMockMvc.perform(get("/api/salaries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salary.getId().intValue())))
            .andExpect(jsonPath("$.[*].salaryDate").value(hasItem(DEFAULT_SALARY_DATE.toString())))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getSalary() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        // Get the salary
        restSalaryMockMvc.perform(get("/api/salaries/{id}", salary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salary.getId().intValue()))
            .andExpect(jsonPath("$.salaryDate").value(DEFAULT_SALARY_DATE.toString()))
            .andExpect(jsonPath("$.period").value(DEFAULT_PERIOD.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSalary() throws Exception {
        // Get the salary
        restSalaryMockMvc.perform(get("/api/salaries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalary() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        int databaseSizeBeforeUpdate = salaryRepository.findAll().size();

        // Update the salary
        Salary updatedSalary = salaryRepository.findById(salary.getId()).get();
        // Disconnect from session so that the updates on updatedSalary are not directly saved in db
        em.detach(updatedSalary);
        updatedSalary
            .salaryDate(UPDATED_SALARY_DATE)
            .period(UPDATED_PERIOD)
            .amount(UPDATED_AMOUNT);

        restSalaryMockMvc.perform(put("/api/salaries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSalary)))
            .andExpect(status().isOk());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
        Salary testSalary = salaryList.get(salaryList.size() - 1);
        assertThat(testSalary.getSalaryDate()).isEqualTo(UPDATED_SALARY_DATE);
        assertThat(testSalary.getPeriod()).isEqualTo(UPDATED_PERIOD);
        assertThat(testSalary.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingSalary() throws Exception {
        int databaseSizeBeforeUpdate = salaryRepository.findAll().size();

        // Create the Salary

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalaryMockMvc.perform(put("/api/salaries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(salary)))
            .andExpect(status().isBadRequest());

        // Validate the Salary in the database
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSalary() throws Exception {
        // Initialize the database
        salaryRepository.saveAndFlush(salary);

        int databaseSizeBeforeDelete = salaryRepository.findAll().size();

        // Delete the salary
        restSalaryMockMvc.perform(delete("/api/salaries/{id}", salary.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Salary> salaryList = salaryRepository.findAll();
        assertThat(salaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
