package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.mycompany.myapp.domain.enumeration.Frequency;

/**
 * A Expense.
 */
@Entity
@Table(name = "expense")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Expense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "expense_name", nullable = false)
    private String expenseName;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Float amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "frequency", nullable = false)
    private Frequency frequency;

    @OneToMany(mappedBy = "expense")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ExpenseInstance> expenseInsts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public Expense expenseName(String expenseName) {
        this.expenseName = expenseName;
        return this;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Expense startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Float getAmount() {
        return amount;
    }

    public Expense amount(Float amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public Expense frequency(Frequency frequency) {
        this.frequency = frequency;
        return this;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public Set<ExpenseInstance> getExpenseInsts() {
        return expenseInsts;
    }

    public Expense expenseInsts(Set<ExpenseInstance> expenseInstances) {
        this.expenseInsts = expenseInstances;
        return this;
    }

    public Expense addExpenseInst(ExpenseInstance expenseInstance) {
        this.expenseInsts.add(expenseInstance);
        expenseInstance.setExpense(this);
        return this;
    }

    public Expense removeExpenseInst(ExpenseInstance expenseInstance) {
        this.expenseInsts.remove(expenseInstance);
        expenseInstance.setExpense(null);
        return this;
    }

    public void setExpenseInsts(Set<ExpenseInstance> expenseInstances) {
        this.expenseInsts = expenseInstances;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Expense)) {
            return false;
        }
        return id != null && id.equals(((Expense) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Expense{" +
            "id=" + getId() +
            ", expenseName='" + getExpenseName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", amount=" + getAmount() +
            ", frequency='" + getFrequency() + "'" +
            "}";
    }
}
