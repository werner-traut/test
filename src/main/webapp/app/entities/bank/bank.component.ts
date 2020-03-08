import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBank } from 'app/shared/model/bank.model';
import { BankService } from './bank.service';
import { BankDeleteDialogComponent } from './bank-delete-dialog.component';

@Component({
  selector: 'jhi-bank',
  templateUrl: './bank.component.html'
})
export class BankComponent implements OnInit, OnDestroy {
  banks?: IBank[];
  eventSubscriber?: Subscription;

  constructor(protected bankService: BankService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.bankService.query().subscribe((res: HttpResponse<IBank[]>) => (this.banks = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInBanks();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IBank): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInBanks(): void {
    this.eventSubscriber = this.eventManager.subscribe('bankListModification', () => this.loadAll());
  }

  delete(bank: IBank): void {
    const modalRef = this.modalService.open(BankDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bank = bank;
  }
}
