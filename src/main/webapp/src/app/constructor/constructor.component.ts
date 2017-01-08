import {Component, OnInit} from '@angular/core';
import {Question} from "../model/question";
import {Option} from "../model/option";

@Component({
  selector: 'constructor-panel',
  templateUrl: './constructor.component.html',
  styleUrls: ['./constructor.component.scss']

})

export class ConstructorComponent implements OnInit{

  businesType = '';
  selectedOption: Option;
  selectedQuestion: Question;
  questions: Question[];


  constructor() {
  }

  ngOnInit() {
  }

  onSelectQuestion(question: Question): void {
    this.selectedQuestion = question;
  }

  onSelectOption(option: Option): void {
    this.selectedOption = option;
  }

  addQuestion(): void {
    if(this.questions == undefined){
      this.questions = [this.newQuestion()];
    } else {
      this.questions.push(this.newQuestion());
    }
  }

  deleteLastQuestion(): void {
    this.questions.pop();
  }

  newQuestion(){
    let option = new Option();
    option.title = 'Нова відповідь';
    let question = new Question();
    question.text = 'Нове питання';
    question.options = [option];
    return question;
  }

  addOption(options: Option[]): void {
    let option = new Option;
    option.title = 'Нова відповідь';
    options.push(option);
  }

  deleteLastOption(options: Option[]): void {
    options.pop();
  }

}

