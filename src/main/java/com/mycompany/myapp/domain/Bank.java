package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

/**
 * A Bank.
 */
@Entity
@Table(name = "bank")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Bank implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @NotNull
    @Column(name = "current_period_end_balance", nullable = false)
    private Float currentPeriodEndBalance;

    @NotNull
    @Column(name = "next_period_end_balance", nullable = false)
    private Float nextPeriodEndBalance;

    @NotNull
    @Column(name = "period_after_end_balance", nullable = false)
    private Float periodAfterEndBalance;

    @NotNull
    @Column(name = "previous_day_expense", nullable = false)
    private Float previousDayExpense;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public Bank entryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
        return this;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public Float getCurrentPeriodEndBalance() {
        return currentPeriodEndBalance;
    }

    public Bank currentPeriodEndBalance(Float currentPeriodEndBalance) {
        this.currentPeriodEndBalance = currentPeriodEndBalance;
        return this;
    }

    public void setCurrentPeriodEndBalance(Float currentPeriodEndBalance) {
        this.currentPeriodEndBalance = currentPeriodEndBalance;
    }

    public Float getNextPeriodEndBalance() {
        return nextPeriodEndBalance;
    }

    public Bank nextPeriodEndBalance(Float nextPeriodEndBalance) {
        this.nextPeriodEndBalance = nextPeriodEndBalance;
        return this;
    }

    public void setNextPeriodEndBalance(Float nextPeriodEndBalance) {
        this.nextPeriodEndBalance = nextPeriodEndBalance;
    }

    public Float getPeriodAfterEndBalance() {
        return periodAfterEndBalance;
    }

    public Bank periodAfterEndBalance(Float periodAfterEndBalance) {
        this.periodAfterEndBalance = periodAfterEndBalance;
        return this;
    }

    public void setPeriodAfterEndBalance(Float periodAfterEndBalance) {
        this.periodAfterEndBalance = periodAfterEndBalance;
    }

    public Float getPreviousDayExpense() {
        return previousDayExpense;
    }

    public Bank previousDayExpense(Float previousDayExpense) {
        this.previousDayExpense = previousDayExpense;
        return this;
    }

    public void setPreviousDayExpense(Float previousDayExpense) {
        this.previousDayExpense = previousDayExpense;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bank)) {
            return false;
        }
        return id != null && id.equals(((Bank) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Bank{" +
            "id=" + getId() +
            ", entryDate='" + getEntryDate() + "'" +
            ", currentPeriodEndBalance=" + getCurrentPeriodEndBalance() +
            ", nextPeriodEndBalance=" + getNextPeriodEndBalance() +
            ", periodAfterEndBalance=" + getPeriodAfterEndBalance() +
            ", previousDayExpense=" + getPreviousDayExpense() +
            "}";
    }
}
