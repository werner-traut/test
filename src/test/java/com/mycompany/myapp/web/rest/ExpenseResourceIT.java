package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TestApp;
import com.mycompany.myapp.config.TestSecurityConfiguration;
import com.mycompany.myapp.domain.Expense;
import com.mycompany.myapp.repository.ExpenseRepository;

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

import com.mycompany.myapp.domain.enumeration.Frequency;
/**
 * Integration tests for the {@link ExpenseResource} REST controller.
 */
@SpringBootTest(classes = { TestApp.class, TestSecurityConfiguration.class })

@AutoConfigureMockMvc
@WithMockUser
public class ExpenseResourceIT {

    private static final String DEFAULT_EXPENSE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EXPENSE_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_AMOUNT = 1F;
    private static final Float UPDATED_AMOUNT = 2F;

    private static final Frequency DEFAULT_FREQUENCY = Frequency.DAILY;
    private static final Frequency UPDATED_FREQUENCY = Frequency.BIWEEKLY;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpenseMockMvc;

    private Expense expense;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expense createEntity(EntityManager em) {
        Expense expense = new Expense()
            .expenseName(DEFAULT_EXPENSE_NAME)
            .startDate(DEFAULT_START_DATE)
            .amount(DEFAULT_AMOUNT)
            .frequency(DEFAULT_FREQUENCY);
        return expense;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expense createUpdatedEntity(EntityManager em) {
        Expense expense = new Expense()
            .expenseName(UPDATED_EXPENSE_NAME)
            .startDate(UPDATED_START_DATE)
            .amount(UPDATED_AMOUNT)
            .frequency(UPDATED_FREQUENCY);
        return expense;
    }

    @BeforeEach
    public void initTest() {
        expense = createEntity(em);
    }

    @Test
    @Transactional
    public void createExpense() throws Exception {
        int databaseSizeBeforeCreate = expenseRepository.findAll().size();

        // Create the Expense
        restExpenseMockMvc.perform(post("/api/expenses").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isCreated());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeCreate + 1);
        Expense testExpense = expenseList.get(expenseList.size() - 1);
        assertThat(testExpense.getExpenseName()).isEqualTo(DEFAULT_EXPENSE_NAME);
        assertThat(testExpense.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testExpense.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testExpense.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
    }

    @Test
    @Transactional
    public void createExpenseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = expenseRepository.findAll().size();

        // Create the Expense with an existing ID
        expense.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseMockMvc.perform(post("/api/expenses").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkExpenseNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseRepository.findAll().size();
        // set the field null
        expense.setExpenseName(null);

        // Create the Expense, which fails.

        restExpenseMockMvc.perform(post("/api/expenses").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isBadRequest());

        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseRepository.findAll().size();
        // set the field null
        expense.setStartDate(null);

        // Create the Expense, which fails.

        restExpenseMockMvc.perform(post("/api/expenses").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isBadRequest());

        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseRepository.findAll().size();
        // set the field null
        expense.setAmount(null);

        // Create the Expense, which fails.

        restExpenseMockMvc.perform(post("/api/expenses").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isBadRequest());

        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrequencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = expenseRepository.findAll().size();
        // set the field null
        expense.setFrequency(null);

        // Create the Expense, which fails.

        restExpenseMockMvc.perform(post("/api/expenses").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isBadRequest());

        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExpenses() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList
        restExpenseMockMvc.perform(get("/api/expenses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expense.getId().intValue())))
            .andExpect(jsonPath("$.[*].expenseName").value(hasItem(DEFAULT_EXPENSE_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.toString())));
    }
    
    @Test
    @Transactional
    public void getExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get the expense
        restExpenseMockMvc.perform(get("/api/expenses/{id}", expense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expense.getId().intValue()))
            .andExpect(jsonPath("$.expenseName").value(DEFAULT_EXPENSE_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExpense() throws Exception {
        // Get the expense
        restExpenseMockMvc.perform(get("/api/expenses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();

        // Update the expense
        Expense updatedExpense = expenseRepository.findById(expense.getId()).get();
        // Disconnect from session so that the updates on updatedExpense are not directly saved in db
        em.detach(updatedExpense);
        updatedExpense
            .expenseName(UPDATED_EXPENSE_NAME)
            .startDate(UPDATED_START_DATE)
            .amount(UPDATED_AMOUNT)
            .frequency(UPDATED_FREQUENCY);

        restExpenseMockMvc.perform(put("/api/expenses").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedExpense)))
            .andExpect(status().isOk());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeUpdate);
        Expense testExpense = expenseList.get(expenseList.size() - 1);
        assertThat(testExpense.getExpenseName()).isEqualTo(UPDATED_EXPENSE_NAME);
        assertThat(testExpense.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testExpense.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testExpense.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
    }

    @Test
    @Transactional
    public void updateNonExistingExpense() throws Exception {
        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();

        // Create the Expense

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseMockMvc.perform(put("/api/expenses").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        int databaseSizeBeforeDelete = expenseRepository.findAll().size();

        // Delete the expense
        restExpenseMockMvc.perform(delete("/api/expenses/{id}", expense.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
