import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IBank, Bank } from 'app/shared/model/bank.model';
import { BankService } from './bank.service';

@Component({
  selector: 'jhi-bank-update',
  templateUrl: './bank-update.component.html'
})
export class BankUpdateComponent implements OnInit {
  isSaving = false;
  entryDateDp: any;

  editForm = this.fb.group({
    id: [],
    entryDate: [null, [Validators.required]],
    currentPeriodEndBalance: [null, [Validators.required]],
    nextPeriodEndBalance: [null, [Validators.required]],
    periodAfterEndBalance: [null, [Validators.required]],
    previousDayExpense: [null, [Validators.required]]
  });

  constructor(protected bankService: BankService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bank }) => {
      this.updateForm(bank);
    });
  }

  updateForm(bank: IBank): void {
    this.editForm.patchValue({
      id: bank.id,
      entryDate: bank.entryDate,
      currentPeriodEndBalance: bank.currentPeriodEndBalance,
      nextPeriodEndBalance: bank.nextPeriodEndBalance,
      periodAfterEndBalance: bank.periodAfterEndBalance,
      previousDayExpense: bank.previousDayExpense
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bank = this.createFromForm();
    if (bank.id !== undefined) {
      this.subscribeToSaveResponse(this.bankService.update(bank));
    } else {
      this.subscribeToSaveResponse(this.bankService.create(bank));
    }
  }

  private createFromForm(): IBank {
    return {
      ...new Bank(),
      id: this.editForm.get(['id'])!.value,
      entryDate: this.editForm.get(['entryDate'])!.value,
      currentPeriodEndBalance: this.editForm.get(['currentPeriodEndBalance'])!.value,
      nextPeriodEndBalance: this.editForm.get(['nextPeriodEndBalance'])!.value,
      periodAfterEndBalance: this.editForm.get(['periodAfterEndBalance'])!.value,
      previousDayExpense: this.editForm.get(['previousDayExpense'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBank>>): void {
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
