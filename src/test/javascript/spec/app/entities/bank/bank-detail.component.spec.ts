import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TestTestModule } from '../../../test.module';
import { BankDetailComponent } from 'app/entities/bank/bank-detail.component';
import { Bank } from 'app/shared/model/bank.model';

describe('Component Tests', () => {
  describe('Bank Management Detail Component', () => {
    let comp: BankDetailComponent;
    let fixture: ComponentFixture<BankDetailComponent>;
    const route = ({ data: of({ bank: new Bank(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TestTestModule],
        declarations: [BankDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(BankDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BankDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load bank on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.bank).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
