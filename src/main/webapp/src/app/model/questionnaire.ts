import {Question} from './question';
import {BusinessType} from './business.type';
export class Questionnaire {
  id: number;
  title: string;
  businessType: BusinessType;
  questions: Question[];


  constructor() {
  }
}
