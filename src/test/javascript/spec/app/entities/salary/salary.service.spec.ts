import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SalaryService } from 'app/entities/salary/salary.service';
import { ISalary, Salary } from 'app/shared/model/salary.model';
import { Period } from 'app/shared/model/enumerations/period.model';

describe('Service Tests', () => {
  describe('Salary Service', () => {
    let injector: TestBed;
    let service: SalaryService;
    let httpMock: HttpTestingController;
    let elemDefault: ISalary;
    let expectedResult: ISalary | ISalary[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(SalaryService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Salary(0, currentDate, Period.CLOSED_PERIOD, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            salaryDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Salary', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            salaryDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            salaryDate: currentDate
          },
          returnedFromService
        );

        service.create(new Salary()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Salary', () => {
        const returnedFromService = Object.assign(
          {
            salaryDate: currentDate.format(DATE_FORMAT),
            period: 'BBBBBB',
            amount: 1
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            salaryDate: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Salary', () => {
        const returnedFromService = Object.assign(
          {
            salaryDate: currentDate.format(DATE_FORMAT),
            period: 'BBBBBB',
            amount: 1
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            salaryDate: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Salary', () => {
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
