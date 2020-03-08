import { Moment } from 'moment';
import { IExpenseInstance } from 'app/shared/model/expense-instance.model';
import { Frequency } from 'app/shared/model/enumerations/frequency.model';

export interface IExpense {
  id?: number;
  expenseName?: string;
  startDate?: Moment;
  amount?: number;
  frequency?: Frequency;
  expenseInsts?: IExpenseInstance[];
}

export class Expense implements IExpense {
  constructor(
    public id?: number,
    public expenseName?: string,
    public startDate?: Moment,
    public amount?: number,
    public frequency?: Frequency,
    public expenseInsts?: IExpenseInstance[]
  ) {}
}
