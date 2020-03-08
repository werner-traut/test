import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TestSharedModule } from 'app/shared/shared.module';
import { ExpenseInstanceComponent } from './expense-instance.component';
import { ExpenseInstanceDetailComponent } from './expense-instance-detail.component';
import { ExpenseInstanceUpdateComponent } from './expense-instance-update.component';
import { ExpenseInstanceDeleteDialogComponent } from './expense-instance-delete-dialog.component';
import { expenseInstanceRoute } from './expense-instance.route';

@NgModule({
  imports: [TestSharedModule, RouterModule.forChild(expenseInstanceRoute)],
  declarations: [
    ExpenseInstanceComponent,
    ExpenseInstanceDetailComponent,
    ExpenseInstanceUpdateComponent,
    ExpenseInstanceDeleteDialogComponent
  ],
  entryComponents: [ExpenseInstanceDeleteDialogComponent]
})
export class TestExpenseInstanceModule {}
