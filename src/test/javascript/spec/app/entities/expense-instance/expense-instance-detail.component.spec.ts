import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TestTestModule } from '../../../test.module';
import { ExpenseInstanceDetailComponent } from 'app/entities/expense-instance/expense-instance-detail.component';
import { ExpenseInstance } from 'app/shared/model/expense-instance.model';

describe('Component Tests', () => {
  describe('ExpenseInstance Management Detail Component', () => {
    let comp: ExpenseInstanceDetailComponent;
    let fixture: ComponentFixture<ExpenseInstanceDetailComponent>;
    const route = ({ data: of({ expenseInstance: new ExpenseInstance(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TestTestModule],
        declarations: [ExpenseInstanceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ExpenseInstanceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExpenseInstanceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load expenseInstance on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.expenseInstance).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
