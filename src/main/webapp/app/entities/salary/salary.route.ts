import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISalary, Salary } from 'app/shared/model/salary.model';
import { SalaryService } from './salary.service';
import { SalaryComponent } from './salary.component';
import { SalaryDetailComponent } from './salary-detail.component';
import { SalaryUpdateComponent } from './salary-update.component';

@Injectable({ providedIn: 'root' })
export class SalaryResolve implements Resolve<ISalary> {
  constructor(private service: SalaryService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISalary> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((salary: HttpResponse<Salary>) => {
          if (salary.body) {
            return of(salary.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Salary());
  }
}

export const salaryRoute: Routes = [
  {
    path: '',
    component: SalaryComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Salaries'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SalaryDetailComponent,
    resolve: {
      salary: SalaryResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Salaries'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SalaryUpdateComponent,
    resolve: {
      salary: SalaryResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Salaries'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SalaryUpdateComponent,
    resolve: {
      salary: SalaryResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Salaries'
    },
    canActivate: [UserRouteAccessService]
  }
];
