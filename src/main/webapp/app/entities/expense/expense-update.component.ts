import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IExpense, Expense } from 'app/shared/model/expense.model';
import { ExpenseService } from './expense.service';

@Component({
  selector: 'jhi-expense-update',
  templateUrl: './expense-update.component.html'
})
export class ExpenseUpdateComponent implements OnInit {
  isSaving = false;
  startDateDp: any;

  editForm = this.fb.group({
    id: [],
    expenseName: [null, [Validators.required]],
    startDate: [null, [Validators.required]],
    amount: [null, [Validators.required]],
    frequency: [null, [Validators.required]]
  });

  constructor(protected expenseService: ExpenseService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expense }) => {
      this.updateForm(expense);
    });
  }

  updateForm(expense: IExpense): void {
    this.editForm.patchValue({
      id: expense.id,
      expenseName: expense.expenseName,
      startDate: expense.startDate,
      amount: expense.amount,
      frequency: expense.frequency
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expense = this.createFromForm();
    if (expense.id !== undefined) {
      this.subscribeToSaveResponse(this.expenseService.update(expense));
    } else {
      this.subscribeToSaveResponse(this.expenseService.create(expense));
    }
  }

  private createFromForm(): IExpense {
    return {
      ...new Expense(),
      id: this.editForm.get(['id'])!.value,
      expenseName: this.editForm.get(['expenseName'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      frequency: this.editForm.get(['frequency'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpense>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
