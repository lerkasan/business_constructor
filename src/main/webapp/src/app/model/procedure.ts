import {Stage} from './stage';
export class Procedure {
  id: number;
  name: string;
  reason: string;
  result: string;
  cost: string;
  term: string;
  method: string;
  decision: string;
  deny: string;
  abuse: string;
  procedureType: any;
  permit: any;
  procedureDocuments: any[];
  stage: Stage;

  constructor() {
  }
}
