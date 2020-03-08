package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TestApp;
import com.mycompany.myapp.config.TestSecurityConfiguration;
import com.mycompany.myapp.domain.ExpenseInstance;
import com.mycompany.myapp.repository.ExpenseInstanceRepository;

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

/**
 * Integration tests for the {@link ExpenseInstanceResource} REST controller.
 */
@SpringBootTest(classes = { TestApp.class, TestSecurityConfiguration.class })

@AutoConfigureMockMvc
@WithMockUser
public class ExpenseInstanceResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_AMOUNT = 1F;
    private static final Float UPDATED_AMOUNT = 2F;

    private static final Boolean DEFAULT_PAID = false;
    private static final Boolean UPDATED_PAID = true;

    @Autowired
    private ExpenseInstanceRepository expenseInstanceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpenseInstanceMockMvc;

    private ExpenseInstance expenseInstance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseInstance createEntity(EntityManager em) {
        ExpenseInstance expenseInstance = new ExpenseInstance()
            .date(DEFAULT_DATE)
            .amount(DEFAULT_AMOUNT)
            .paid(DEFAULT_PAID);
        return expenseInstance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseInstance createUpdatedEntity(EntityManager em) {
        ExpenseInstance expenseInstance = new ExpenseInstance()
            .date(UPDATED_DATE)
            .amount(UPDATED_AMOUNT)
            .paid(UPDATED_PAID);
        return expenseInstance;
    }

    @BeforeEach
    public void initTest() {
        expenseInstance = createEntity(em);
    }

    @Test
    @Transactional
    public void createExpenseInstance() throws Exception {
        int databaseSizeBeforeCreate = expenseInstanceRepository.findAll().size();

        // Create the ExpenseInstance
        restExpenseInstanceMockMvc.perform(post("/api/expense-instances").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseInstance)))
            .andExpect(status().isCreated());

        // Validate the ExpenseInstance in the database
        List<ExpenseInstance> expenseInstanceList = expenseInstanceRepository.findAll();
        assertThat(expenseInstanceList).hasSize(databaseSizeBeforeCreate + 1);
        ExpenseInstance testExpenseInstance = expenseInstanceList.get(expenseInstanceList.size() - 1);
        assertThat(testExpenseInstance.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testExpenseInstance.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testExpenseInstance.isPaid()).isEqualTo(DEFAULT_PAID);
    }

    @Test
    @Transactional
    public void createExpenseInstanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = expenseInstanceRepository.findAll().size();

        // Create the ExpenseInstance with an existing ID
        expenseInstance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseInstanceMockMvc.perform(post("/api/expense-instances").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseInstance)))
            .andExpect(status().isBadRequest());

        // Validate the ExpenseInstance in the database
        List<ExpenseInstance> expenseInstanceList = expenseInstanceRepository.findAll();
        assertThat(expenseInstanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseInstanceRepository.findAll().size();
        // set the field null
        expenseInstance.setDate(null);

        // Create the ExpenseInstance, which fails.

        restExpenseInstanceMockMvc.perform(post("/api/expense-instances").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseInstance)))
            .andExpect(status().isBadRequest());

        List<ExpenseInstance> expenseInstanceList = expenseInstanceRepository.findAll();
        assertThat(expenseInstanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseInstanceRepository.findAll().size();
        // set the field null
        expenseInstance.setAmount(null);

        // Create the ExpenseInstance, which fails.

        restExpenseInstanceMockMvc.perform(post("/api/expense-instances").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseInstance)))
            .andExpect(status().isBadRequest());

        List<ExpenseInstance> expenseInstanceList = expenseInstanceRepository.findAll();
        assertThat(expenseInstanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaidIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseInstanceRepository.findAll().size();
        // set the field null
        expenseInstance.setPaid(null);

        // Create the ExpenseInstance, which fails.

        restExpenseInstanceMockMvc.perform(post("/api/expense-instances").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseInstance)))
            .andExpect(status().isBadRequest());

        List<ExpenseInstance> expenseInstanceList = expenseInstanceRepository.findAll();
        assertThat(expenseInstanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExpenseInstances() throws Exception {
        // Initialize the database
        expenseInstanceRepository.saveAndFlush(expenseInstance);

        // Get all the expenseInstanceList
        restExpenseInstanceMockMvc.perform(get("/api/expense-instances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseInstance.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getExpenseInstance() throws Exception {
        // Initialize the database
        expenseInstanceRepository.saveAndFlush(expenseInstance);

        // Get the expenseInstance
        restExpenseInstanceMockMvc.perform(get("/api/expense-instances/{id}", expenseInstance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expenseInstance.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingExpenseInstance() throws Exception {
        // Get the expenseInstance
        restExpenseInstanceMockMvc.perform(get("/api/expense-instances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpenseInstance() throws Exception {
        // Initialize the database
        expenseInstanceRepository.saveAndFlush(expenseInstance);

        int databaseSizeBeforeUpdate = expenseInstanceRepository.findAll().size();

        // Update the expenseInstance
        ExpenseInstance updatedExpenseInstance = expenseInstanceRepository.findById(expenseInstance.getId()).get();
        // Disconnect from session so that the updates on updatedExpenseInstance are not directly saved in db
        em.detach(updatedExpenseInstance);
        updatedExpenseInstance
            .date(UPDATED_DATE)
            .amount(UPDATED_AMOUNT)
            .paid(UPDATED_PAID);

        restExpenseInstanceMockMvc.perform(put("/api/expense-instances").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedExpenseInstance)))
            .andExpect(status().isOk());

        // Validate the ExpenseInstance in the database
        List<ExpenseInstance> expenseInstanceList = expenseInstanceRepository.findAll();
        assertThat(expenseInstanceList).hasSize(databaseSizeBeforeUpdate);
        ExpenseInstance testExpenseInstance = expenseInstanceList.get(expenseInstanceList.size() - 1);
        assertThat(testExpenseInstance.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testExpenseInstance.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testExpenseInstance.isPaid()).isEqualTo(UPDATED_PAID);
    }

    @Test
    @Transactional
    public void updateNonExistingExpenseInstance() throws Exception {
        int databaseSizeBeforeUpdate = expenseInstanceRepository.findAll().size();

        // Create the ExpenseInstance

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseInstanceMockMvc.perform(put("/api/expense-instances").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expenseInstance)))
            .andExpect(status().isBadRequest());

        // Validate the ExpenseInstance in the database
        List<ExpenseInstance> expenseInstanceList = expenseInstanceRepository.findAll();
        assertThat(expenseInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExpenseInstance() throws Exception {
        // Initialize the database
        expenseInstanceRepository.saveAndFlush(expenseInstance);

        int databaseSizeBeforeDelete = expenseInstanceRepository.findAll().size();

        // Delete the expenseInstance
        restExpenseInstanceMockMvc.perform(delete("/api/expense-instances/{id}", expenseInstance.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExpenseInstance> expenseInstanceList = expenseInstanceRepository.findAll();
        assertThat(expenseInstanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
