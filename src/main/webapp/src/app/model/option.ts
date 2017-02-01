import {Question} from './question';
import {Procedure} from './procedure';
export class Option {
  id: number;
  title: string;
  question: any;
  nextQuestion: Question;
  procedure: Procedure;

  constructor() {
  }
}
