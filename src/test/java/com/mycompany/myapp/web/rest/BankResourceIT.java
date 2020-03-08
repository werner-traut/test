package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TestApp;
import com.mycompany.myapp.config.TestSecurityConfiguration;
import com.mycompany.myapp.domain.Bank;
import com.mycompany.myapp.repository.BankRepository;

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
 * Integration tests for the {@link BankResource} REST controller.
 */
@SpringBootTest(classes = { TestApp.class, TestSecurityConfiguration.class })

@AutoConfigureMockMvc
@WithMockUser
public class BankResourceIT {

    private static final LocalDate DEFAULT_ENTRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENTRY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_CURRENT_PERIOD_END_BALANCE = 1F;
    private static final Float UPDATED_CURRENT_PERIOD_END_BALANCE = 2F;

    private static final Float DEFAULT_NEXT_PERIOD_END_BALANCE = 1F;
    private static final Float UPDATED_NEXT_PERIOD_END_BALANCE = 2F;

    private static final Float DEFAULT_PERIOD_AFTER_END_BALANCE = 1F;
    private static final Float UPDATED_PERIOD_AFTER_END_BALANCE = 2F;

    private static final Float DEFAULT_PREVIOUS_DAY_EXPENSE = 1F;
    private static final Float UPDATED_PREVIOUS_DAY_EXPENSE = 2F;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBankMockMvc;

    private Bank bank;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bank createEntity(EntityManager em) {
        Bank bank = new Bank()
            .entryDate(DEFAULT_ENTRY_DATE)
            .currentPeriodEndBalance(DEFAULT_CURRENT_PERIOD_END_BALANCE)
            .nextPeriodEndBalance(DEFAULT_NEXT_PERIOD_END_BALANCE)
            .periodAfterEndBalance(DEFAULT_PERIOD_AFTER_END_BALANCE)
            .previousDayExpense(DEFAULT_PREVIOUS_DAY_EXPENSE);
        return bank;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bank createUpdatedEntity(EntityManager em) {
        Bank bank = new Bank()
            .entryDate(UPDATED_ENTRY_DATE)
            .currentPeriodEndBalance(UPDATED_CURRENT_PERIOD_END_BALANCE)
            .nextPeriodEndBalance(UPDATED_NEXT_PERIOD_END_BALANCE)
            .periodAfterEndBalance(UPDATED_PERIOD_AFTER_END_BALANCE)
            .previousDayExpense(UPDATED_PREVIOUS_DAY_EXPENSE);
        return bank;
    }

    @BeforeEach
    public void initTest() {
        bank = createEntity(em);
    }

    @Test
    @Transactional
    public void createBank() throws Exception {
        int databaseSizeBeforeCreate = bankRepository.findAll().size();

        // Create the Bank
        restBankMockMvc.perform(post("/api/banks").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bank)))
            .andExpect(status().isCreated());

        // Validate the Bank in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeCreate + 1);
        Bank testBank = bankList.get(bankList.size() - 1);
        assertThat(testBank.getEntryDate()).isEqualTo(DEFAULT_ENTRY_DATE);
        assertThat(testBank.getCurrentPeriodEndBalance()).isEqualTo(DEFAULT_CURRENT_PERIOD_END_BALANCE);
        assertThat(testBank.getNextPeriodEndBalance()).isEqualTo(DEFAULT_NEXT_PERIOD_END_BALANCE);
        assertThat(testBank.getPeriodAfterEndBalance()).isEqualTo(DEFAULT_PERIOD_AFTER_END_BALANCE);
        assertThat(testBank.getPreviousDayExpense()).isEqualTo(DEFAULT_PREVIOUS_DAY_EXPENSE);
    }

    @Test
    @Transactional
    public void createBankWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bankRepository.findAll().size();

        // Create the Bank with an existing ID
        bank.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBankMockMvc.perform(post("/api/banks").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bank)))
            .andExpect(status().isBadRequest());

        // Validate the Bank in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkEntryDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankRepository.findAll().size();
        // set the field null
        bank.setEntryDate(null);

        // Create the Bank, which fails.

        restBankMockMvc.perform(post("/api/banks").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bank)))
            .andExpect(status().isBadRequest());

        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrentPeriodEndBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankRepository.findAll().size();
        // set the field null
        bank.setCurrentPeriodEndBalance(null);

        // Create the Bank, which fails.

        restBankMockMvc.perform(post("/api/banks").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bank)))
            .andExpect(status().isBadRequest());

        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNextPeriodEndBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankRepository.findAll().size();
        // set the field null
        bank.setNextPeriodEndBalance(null);

        // Create the Bank, which fails.

        restBankMockMvc.perform(post("/api/banks").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bank)))
            .andExpect(status().isBadRequest());

        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPeriodAfterEndBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankRepository.findAll().size();
        // set the field null
        bank.setPeriodAfterEndBalance(null);

        // Create the Bank, which fails.

        restBankMockMvc.perform(post("/api/banks").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bank)))
            .andExpect(status().isBadRequest());

        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPreviousDayExpenseIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankRepository.findAll().size();
        // set the field null
        bank.setPreviousDayExpense(null);

        // Create the Bank, which fails.

        restBankMockMvc.perform(post("/api/banks").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bank)))
            .andExpect(status().isBadRequest());

        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBanks() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList
        restBankMockMvc.perform(get("/api/banks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bank.getId().intValue())))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].currentPeriodEndBalance").value(hasItem(DEFAULT_CURRENT_PERIOD_END_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].nextPeriodEndBalance").value(hasItem(DEFAULT_NEXT_PERIOD_END_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].periodAfterEndBalance").value(hasItem(DEFAULT_PERIOD_AFTER_END_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].previousDayExpense").value(hasItem(DEFAULT_PREVIOUS_DAY_EXPENSE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getBank() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get the bank
        restBankMockMvc.perform(get("/api/banks/{id}", bank.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bank.getId().intValue()))
            .andExpect(jsonPath("$.entryDate").value(DEFAULT_ENTRY_DATE.toString()))
            .andExpect(jsonPath("$.currentPeriodEndBalance").value(DEFAULT_CURRENT_PERIOD_END_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.nextPeriodEndBalance").value(DEFAULT_NEXT_PERIOD_END_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.periodAfterEndBalance").value(DEFAULT_PERIOD_AFTER_END_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.previousDayExpense").value(DEFAULT_PREVIOUS_DAY_EXPENSE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBank() throws Exception {
        // Get the bank
        restBankMockMvc.perform(get("/api/banks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBank() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        int databaseSizeBeforeUpdate = bankRepository.findAll().size();

        // Update the bank
        Bank updatedBank = bankRepository.findById(bank.getId()).get();
        // Disconnect from session so that the updates on updatedBank are not directly saved in db
        em.detach(updatedBank);
        updatedBank
            .entryDate(UPDATED_ENTRY_DATE)
            .currentPeriodEndBalance(UPDATED_CURRENT_PERIOD_END_BALANCE)
            .nextPeriodEndBalance(UPDATED_NEXT_PERIOD_END_BALANCE)
            .periodAfterEndBalance(UPDATED_PERIOD_AFTER_END_BALANCE)
            .previousDayExpense(UPDATED_PREVIOUS_DAY_EXPENSE);

        restBankMockMvc.perform(put("/api/banks").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedBank)))
            .andExpect(status().isOk());

        // Validate the Bank in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeUpdate);
        Bank testBank = bankList.get(bankList.size() - 1);
        assertThat(testBank.getEntryDate()).isEqualTo(UPDATED_ENTRY_DATE);
        assertThat(testBank.getCurrentPeriodEndBalance()).isEqualTo(UPDATED_CURRENT_PERIOD_END_BALANCE);
        assertThat(testBank.getNextPeriodEndBalance()).isEqualTo(UPDATED_NEXT_PERIOD_END_BALANCE);
        assertThat(testBank.getPeriodAfterEndBalance()).isEqualTo(UPDATED_PERIOD_AFTER_END_BALANCE);
        assertThat(testBank.getPreviousDayExpense()).isEqualTo(UPDATED_PREVIOUS_DAY_EXPENSE);
    }

    @Test
    @Transactional
    public void updateNonExistingBank() throws Exception {
        int databaseSizeBeforeUpdate = bankRepository.findAll().size();

        // Create the Bank

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankMockMvc.perform(put("/api/banks").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bank)))
            .andExpect(status().isBadRequest());

        // Validate the Bank in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBank() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        int databaseSizeBeforeDelete = bankRepository.findAll().size();

        // Delete the bank
        restBankMockMvc.perform(delete("/api/banks/{id}", bank.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
