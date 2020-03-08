import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExpenseInstance } from 'app/shared/model/expense-instance.model';

@Component({
  selector: 'jhi-expense-instance-detail',
  templateUrl: './expense-instance-detail.component.html'
})
export class ExpenseInstanceDetailComponent implements OnInit {
  expenseInstance: IExpenseInstance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expenseInstance }) => (this.expenseInstance = expenseInstance));
  }

  previousState(): void {
    window.history.back();
  }
}
