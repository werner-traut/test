import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBank } from 'app/shared/model/bank.model';

type EntityResponseType = HttpResponse<IBank>;
type EntityArrayResponseType = HttpResponse<IBank[]>;

@Injectable({ providedIn: 'root' })
export class BankService {
  public resourceUrl = SERVER_API_URL + 'api/banks';

  constructor(protected http: HttpClient) {}

  create(bank: IBank): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bank);
    return this.http
      .post<IBank>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(bank: IBank): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bank);
    return this.http
      .put<IBank>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBank>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBank[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(bank: IBank): IBank {
    const copy: IBank = Object.assign({}, bank, {
      entryDate: bank.entryDate && bank.entryDate.isValid() ? bank.entryDate.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.entryDate = res.body.entryDate ? moment(res.body.entryDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((bank: IBank) => {
        bank.entryDate = bank.entryDate ? moment(bank.entryDate) : undefined;
      });
    }
    return res;
  }
}
