import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IExpenseInstance } from 'app/shared/model/expense-instance.model';

type EntityResponseType = HttpResponse<IExpenseInstance>;
type EntityArrayResponseType = HttpResponse<IExpenseInstance[]>;

@Injectable({ providedIn: 'root' })
export class ExpenseInstanceService {
  public resourceUrl = SERVER_API_URL + 'api/expense-instances';

  constructor(protected http: HttpClient) {}

  create(expenseInstance: IExpenseInstance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseInstance);
    return this.http
      .post<IExpenseInstance>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(expenseInstance: IExpenseInstance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseInstance);
    return this.http
      .put<IExpenseInstance>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExpenseInstance>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExpenseInstance[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(expenseInstance: IExpenseInstance): IExpenseInstance {
    const copy: IExpenseInstance = Object.assign({}, expenseInstance, {
      date: expenseInstance.date && expenseInstance.date.isValid() ? expenseInstance.date.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? moment(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((expenseInstance: IExpenseInstance) => {
        expenseInstance.date = expenseInstance.date ? moment(expenseInstance.date) : undefined;
      });
    }
    return res;
  }
}
