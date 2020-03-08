import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TestTestModule } from '../../../test.module';
import { ExpenseInstanceUpdateComponent } from 'app/entities/expense-instance/expense-instance-update.component';
import { ExpenseInstanceService } from 'app/entities/expense-instance/expense-instance.service';
import { ExpenseInstance } from 'app/shared/model/expense-instance.model';

describe('Component Tests', () => {
  describe('ExpenseInstance Management Update Component', () => {
    let comp: ExpenseInstanceUpdateComponent;
    let fixture: ComponentFixture<ExpenseInstanceUpdateComponent>;
    let service: ExpenseInstanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TestTestModule],
        declarations: [ExpenseInstanceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ExpenseInstanceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExpenseInstanceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExpenseInstanceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ExpenseInstance(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ExpenseInstance();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
