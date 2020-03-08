import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IExpenseInstance } from 'app/shared/model/expense-instance.model';
import { ExpenseInstanceService } from './expense-instance.service';
import { ExpenseInstanceDeleteDialogComponent } from './expense-instance-delete-dialog.component';

@Component({
  selector: 'jhi-expense-instance',
  templateUrl: './expense-instance.component.html'
})
export class ExpenseInstanceComponent implements OnInit, OnDestroy {
  expenseInstances?: IExpenseInstance[];
  eventSubscriber?: Subscription;

  constructor(
    protected expenseInstanceService: ExpenseInstanceService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.expenseInstanceService.query().subscribe((res: HttpResponse<IExpenseInstance[]>) => (this.expenseInstances = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInExpenseInstances();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IExpenseInstance): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInExpenseInstances(): void {
    this.eventSubscriber = this.eventManager.subscribe('expenseInstanceListModification', () => this.loadAll());
  }

  delete(expenseInstance: IExpenseInstance): void {
    const modalRef = this.modalService.open(ExpenseInstanceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.expenseInstance = expenseInstance;
  }
}
