import { Moment } from 'moment';

export interface IBank {
  id?: number;
  entryDate?: Moment;
  currentPeriodEndBalance?: number;
  nextPeriodEndBalance?: number;
  periodAfterEndBalance?: number;
  previousDayExpense?: number;
}

export class Bank implements IBank {
  constructor(
    public id?: number,
    public entryDate?: Moment,
    public currentPeriodEndBalance?: number,
    public nextPeriodEndBalance?: number,
    public periodAfterEndBalance?: number,
    public previousDayExpense?: number
  ) {}
}
