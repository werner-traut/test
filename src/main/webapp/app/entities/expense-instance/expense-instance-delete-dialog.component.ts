import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IExpenseInstance } from 'app/shared/model/expense-instance.model';
import { ExpenseInstanceService } from './expense-instance.service';

@Component({
  templateUrl: './expense-instance-delete-dialog.component.html'
})
export class ExpenseInstanceDeleteDialogComponent {
  expenseInstance?: IExpenseInstance;

  constructor(
    protected expenseInstanceService: ExpenseInstanceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.expenseInstanceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('expenseInstanceListModification');
      this.activeModal.close();
    });
  }
}
