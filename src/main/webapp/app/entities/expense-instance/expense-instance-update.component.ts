import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IExpenseInstance, ExpenseInstance } from 'app/shared/model/expense-instance.model';
import { ExpenseInstanceService } from './expense-instance.service';
import { IExpense } from 'app/shared/model/expense.model';
import { ExpenseService } from 'app/entities/expense/expense.service';

@Component({
  selector: 'jhi-expense-instance-update',
  templateUrl: './expense-instance-update.component.html'
})
export class ExpenseInstanceUpdateComponent implements OnInit {
  isSaving = false;
  expenses: IExpense[] = [];
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    amount: [null, [Validators.required]],
    paid: [null, [Validators.required]],
    expense: []
  });

  constructor(
    protected expenseInstanceService: ExpenseInstanceService,
    protected expenseService: ExpenseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expenseInstance }) => {
      this.updateForm(expenseInstance);

      this.expenseService.query().subscribe((res: HttpResponse<IExpense[]>) => (this.expenses = res.body || []));
    });
  }

  updateForm(expenseInstance: IExpenseInstance): void {
    this.editForm.patchValue({
      id: expenseInstance.id,
      date: expenseInstance.date,
      amount: expenseInstance.amount,
      paid: expenseInstance.paid,
      expense: expenseInstance.expense
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expenseInstance = this.createFromForm();
    if (expenseInstance.id !== undefined) {
      this.subscribeToSaveResponse(this.expenseInstanceService.update(expenseInstance));
    } else {
      this.subscribeToSaveResponse(this.expenseInstanceService.create(expenseInstance));
    }
  }

  private createFromForm(): IExpenseInstance {
    return {
      ...new ExpenseInstance(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      paid: this.editForm.get(['paid'])!.value,
      expense: this.editForm.get(['expense'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpenseInstance>>): void {
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

  trackById(index: number, item: IExpense): any {
    return item.id;
  }
}
