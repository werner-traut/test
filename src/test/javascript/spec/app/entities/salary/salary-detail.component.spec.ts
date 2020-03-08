import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TestTestModule } from '../../../test.module';
import { SalaryDetailComponent } from 'app/entities/salary/salary-detail.component';
import { Salary } from 'app/shared/model/salary.model';

describe('Component Tests', () => {
  describe('Salary Management Detail Component', () => {
    let comp: SalaryDetailComponent;
    let fixture: ComponentFixture<SalaryDetailComponent>;
    const route = ({ data: of({ salary: new Salary(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TestTestModule],
        declarations: [SalaryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SalaryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SalaryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load salary on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.salary).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
