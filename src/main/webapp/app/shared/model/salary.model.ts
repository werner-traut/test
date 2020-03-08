import { Moment } from 'moment';
import { Period } from 'app/shared/model/enumerations/period.model';

export interface ISalary {
  id?: number;
  salaryDate?: Moment;
  period?: Period;
  amount?: number;
}

export class Salary implements ISalary {
  constructor(public id?: number, public salaryDate?: Moment, public period?: Period, public amount?: number) {}
}
