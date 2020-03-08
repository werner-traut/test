import { Moment } from 'moment';
import { IExpense } from 'app/shared/model/expense.model';

export interface IExpenseInstance {
  id?: number;
  date?: Moment;
  amount?: number;
  paid?: boolean;
  expense?: IExpense;
}

export class ExpenseInstance implements IExpenseInstance {
  constructor(public id?: number, public date?: Moment, public amount?: number, public paid?: boolean, public expense?: IExpense) {
    this.paid = this.paid || false;
  }
}
