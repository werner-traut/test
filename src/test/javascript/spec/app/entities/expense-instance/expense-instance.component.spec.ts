import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { TestTestModule } from '../../../test.module';
import { ExpenseInstanceComponent } from 'app/entities/expense-instance/expense-instance.component';
import { ExpenseInstanceService } from 'app/entities/expense-instance/expense-instance.service';
import { ExpenseInstance } from 'app/shared/model/expense-instance.model';

describe('Component Tests', () => {
  describe('ExpenseInstance Management Component', () => {
    let comp: ExpenseInstanceComponent;
    let fixture: ComponentFixture<ExpenseInstanceComponent>;
    let service: ExpenseInstanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TestTestModule],
        declarations: [ExpenseInstanceComponent]
      })
        .overrideTemplate(ExpenseInstanceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExpenseInstanceComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExpenseInstanceService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ExpenseInstance(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.expenseInstances && comp.expenseInstances[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
