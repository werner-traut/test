import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'expense',
        loadChildren: () => import('./expense/expense.module').then(m => m.TestExpenseModule)
      },
      {
        path: 'expense-instance',
        loadChildren: () => import('./expense-instance/expense-instance.module').then(m => m.TestExpenseInstanceModule)
      },
      {
        path: 'salary',
        loadChildren: () => import('./salary/salary.module').then(m => m.TestSalaryModule)
      },
      {
        path: 'bank',
        loadChildren: () => import('./bank/bank.module').then(m => m.TestBankModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class TestEntityModule {}
