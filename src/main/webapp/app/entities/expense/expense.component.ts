import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IExpense } from 'app/shared/model/expense.model';
import { ExpenseService } from './expense.service';
import { ExpenseDeleteDialogComponent } from './expense-delete-dialog.component';

@Component({
  selector: 'jhi-expense',
  templateUrl: './expense.component.html'
})
export class ExpenseComponent implements OnInit, OnDestroy {
  expenses?: IExpense[];
  eventSubscriber?: Subscription;

  constructor(protected expenseService: ExpenseService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.expenseService.query().subscribe((res: HttpResponse<IExpense[]>) => (this.expenses = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInExpenses();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IExpense): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInExpenses(): void {
    this.eventSubscriber = this.eventManager.subscribe('expenseListModification', () => this.loadAll());
  }

  delete(expense: IExpense): void {
    const modalRef = this.modalService.open(ExpenseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.expense = expense;
  }
}
