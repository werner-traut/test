import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IExpenseInstance, ExpenseInstance } from 'app/shared/model/expense-instance.model';
import { ExpenseInstanceService } from './expense-instance.service';
import { ExpenseInstanceComponent } from './expense-instance.component';
import { ExpenseInstanceDetailComponent } from './expense-instance-detail.component';
import { ExpenseInstanceUpdateComponent } from './expense-instance-update.component';

@Injectable({ providedIn: 'root' })
export class ExpenseInstanceResolve implements Resolve<IExpenseInstance> {
  constructor(private service: ExpenseInstanceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExpenseInstance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((expenseInstance: HttpResponse<ExpenseInstance>) => {
          if (expenseInstance.body) {
            return of(expenseInstance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExpenseInstance());
  }
}

export const expenseInstanceRoute: Routes = [
  {
    path: '',
    component: ExpenseInstanceComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ExpenseInstances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ExpenseInstanceDetailComponent,
    resolve: {
      expenseInstance: ExpenseInstanceResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ExpenseInstances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ExpenseInstanceUpdateComponent,
    resolve: {
      expenseInstance: ExpenseInstanceResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ExpenseInstances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ExpenseInstanceUpdateComponent,
    resolve: {
      expenseInstance: ExpenseInstanceResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ExpenseInstances'
    },
    canActivate: [UserRouteAccessService]
  }
];
