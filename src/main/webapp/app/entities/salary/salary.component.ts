import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISalary } from 'app/shared/model/salary.model';
import { SalaryService } from './salary.service';
import { SalaryDeleteDialogComponent } from './salary-delete-dialog.component';

@Component({
  selector: 'jhi-salary',
  templateUrl: './salary.component.html'
})
export class SalaryComponent implements OnInit, OnDestroy {
  salaries?: ISalary[];
  eventSubscriber?: Subscription;

  constructor(protected salaryService: SalaryService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.salaryService.query().subscribe((res: HttpResponse<ISalary[]>) => (this.salaries = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSalaries();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISalary): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSalaries(): void {
    this.eventSubscriber = this.eventManager.subscribe('salaryListModification', () => this.loadAll());
  }

  delete(salary: ISalary): void {
    const modalRef = this.modalService.open(SalaryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.salary = salary;
  }
}
