import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { TestTestModule } from '../../../test.module';
import { BankComponent } from 'app/entities/bank/bank.component';
import { BankService } from 'app/entities/bank/bank.service';
import { Bank } from 'app/shared/model/bank.model';

describe('Component Tests', () => {
  describe('Bank Management Component', () => {
    let comp: BankComponent;
    let fixture: ComponentFixture<BankComponent>;
    let service: BankService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TestTestModule],
        declarations: [BankComponent]
      })
        .overrideTemplate(BankComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BankComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BankService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Bank(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.banks && comp.banks[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
