import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { TestTestModule } from '../../../test.module';
import { ExpenseComponent } from 'app/entities/expense/expense.component';
import { ExpenseService } from 'app/entities/expense/expense.service';
import { Expense } from 'app/shared/model/expense.model';

describe('Component Tests', () => {
  describe('Expense Management Component', () => {
    let comp: ExpenseComponent;
    let fixture: ComponentFixture<ExpenseComponent>;
    let service: ExpenseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TestTestModule],
        declarations: [ExpenseComponent]
      })
        .overrideTemplate(ExpenseComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExpenseComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExpenseService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Expense(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.expenses && comp.expenses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
