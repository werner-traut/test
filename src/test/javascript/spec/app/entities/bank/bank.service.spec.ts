import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { BankService } from 'app/entities/bank/bank.service';
import { IBank, Bank } from 'app/shared/model/bank.model';

describe('Service Tests', () => {
  describe('Bank Service', () => {
    let injector: TestBed;
    let service: BankService;
    let httpMock: HttpTestingController;
    let elemDefault: IBank;
    let expectedResult: IBank | IBank[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(BankService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Bank(0, currentDate, 0, 0, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            entryDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Bank', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            entryDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            entryDate: currentDate
          },
          returnedFromService
        );

        service.create(new Bank()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Bank', () => {
        const returnedFromService = Object.assign(
          {
            entryDate: currentDate.format(DATE_FORMAT),
            currentPeriodEndBalance: 1,
            nextPeriodEndBalance: 1,
            periodAfterEndBalance: 1,
            previousDayExpense: 1
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            entryDate: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Bank', () => {
        const returnedFromService = Object.assign(
          {
            entryDate: currentDate.format(DATE_FORMAT),
            currentPeriodEndBalance: 1,
            nextPeriodEndBalance: 1,
            periodAfterEndBalance: 1,
            previousDayExpense: 1
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            entryDate: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Bank', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
