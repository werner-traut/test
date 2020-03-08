import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISalary } from 'app/shared/model/salary.model';

type EntityResponseType = HttpResponse<ISalary>;
type EntityArrayResponseType = HttpResponse<ISalary[]>;

@Injectable({ providedIn: 'root' })
export class SalaryService {
  public resourceUrl = SERVER_API_URL + 'api/salaries';

  constructor(protected http: HttpClient) {}

  create(salary: ISalary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salary);
    return this.http
      .post<ISalary>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(salary: ISalary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salary);
    return this.http
      .put<ISalary>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISalary>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISalary[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(salary: ISalary): ISalary {
    const copy: ISalary = Object.assign({}, salary, {
      salaryDate: salary.salaryDate && salary.salaryDate.isValid() ? salary.salaryDate.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.salaryDate = res.body.salaryDate ? moment(res.body.salaryDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((salary: ISalary) => {
        salary.salaryDate = salary.salaryDate ? moment(salary.salaryDate) : undefined;
      });
    }
    return res;
  }
}
