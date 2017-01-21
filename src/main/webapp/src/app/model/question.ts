import {Option} from '../model/option';

export class Question {
  id: number;
  text: string;
  inputType: string;
  options: Option[];

  constructor() {
  }
}

