import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ISalary, Salary } from 'app/shared/model/salary.model';
import { SalaryService } from './salary.service';

@Component({
  selector: 'jhi-salary-update',
  templateUrl: './salary-update.component.html'
})
export class SalaryUpdateComponent implements OnInit {
  isSaving = false;
  salaryDateDp: any;

  editForm = this.fb.group({
    id: [],
    salaryDate: [null, [Validators.required]],
    period: [null, [Validators.required]],
    amount: [null, [Validators.required]]
  });

  constructor(protected salaryService: SalaryService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salary }) => {
      this.updateForm(salary);
    });
  }

  updateForm(salary: ISalary): void {
    this.editForm.patchValue({
      id: salary.id,
      salaryDate: salary.salaryDate,
      period: salary.period,
      amount: salary.amount
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const salary = this.createFromForm();
    if (salary.id !== undefined) {
      this.subscribeToSaveResponse(this.salaryService.update(salary));
    } else {
      this.subscribeToSaveResponse(this.salaryService.create(salary));
    }
  }

  private createFromForm(): ISalary {
    return {
      ...new Salary(),
      id: this.editForm.get(['id'])!.value,
      salaryDate: this.editForm.get(['salaryDate'])!.value,
      period: this.editForm.get(['period'])!.value,
      amount: this.editForm.get(['amount'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISalary>>): void {
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
