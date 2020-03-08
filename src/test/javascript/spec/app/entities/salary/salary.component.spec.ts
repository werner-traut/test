import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { TestTestModule } from '../../../test.module';
import { SalaryComponent } from 'app/entities/salary/salary.component';
import { SalaryService } from 'app/entities/salary/salary.service';
import { Salary } from 'app/shared/model/salary.model';

describe('Component Tests', () => {
  describe('Salary Management Component', () => {
    let comp: SalaryComponent;
    let fixture: ComponentFixture<SalaryComponent>;
    let service: SalaryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TestTestModule],
        declarations: [SalaryComponent]
      })
        .overrideTemplate(SalaryComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SalaryComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SalaryService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Salary(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.salaries && comp.salaries[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
