import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISalary } from 'app/shared/model/salary.model';
import { SalaryService } from './salary.service';

@Component({
  templateUrl: './salary-delete-dialog.component.html'
})
export class SalaryDeleteDialogComponent {
  salary?: ISalary;

  constructor(protected salaryService: SalaryService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.salaryService.delete(id).subscribe(() => {
      this.eventManager.broadcast('salaryListModification');
      this.activeModal.close();
    });
  }
}
