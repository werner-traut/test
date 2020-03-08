package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

import com.mycompany.myapp.domain.enumeration.Period;

/**
 * A Salary.
 */
@Entity
@Table(name = "salary")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Salary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "salary_date", nullable = false)
    private LocalDate salaryDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "period", nullable = false)
    private Period period;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Float amount;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSalaryDate() {
        return salaryDate;
    }

    public Salary salaryDate(LocalDate salaryDate) {
        this.salaryDate = salaryDate;
        return this;
    }

    public void setSalaryDate(LocalDate salaryDate) {
        this.salaryDate = salaryDate;
    }

    public Period getPeriod() {
        return period;
    }

    public Salary period(Period period) {
        this.period = period;
        return this;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Float getAmount() {
        return amount;
    }

    public Salary amount(Float amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Salary)) {
            return false;
        }
        return id != null && id.equals(((Salary) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Salary{" +
            "id=" + getId() +
            ", salaryDate='" + getSalaryDate() + "'" +
            ", period='" + getPeriod() + "'" +
            ", amount=" + getAmount() +
            "}";
    }
}
