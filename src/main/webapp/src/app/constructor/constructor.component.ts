import {Component, OnInit} from '@angular/core';
import {Question} from "../model/question";
import {Option} from "../model/option";
import {QuestionService} from "../service/questions.service";

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

  inputType = [
    { value: 'SINGLE_CHOICE' },
    { value: 'MULTI_CHOICE' }
  ];

  constructor(private questionService: QuestionService) {
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
    option.title = '';
    let question = new Question();
    question.text = '';
    question.options = [option];
    question.inputType = this.inputType[0].value;
    return question;
  }

  changeInputType(question: Question): void{
    if(question.inputType == this.inputType[0].value){
      question.inputType = this.inputType[1].value;
    } else {
      question.inputType = this.inputType[0].value;
    }
  }

  addOption(options: Option[]): void {
    let option = new Option;
    option.title = '';
    options.push(option);
  }

  deleteLastOption(options: Option[]): void {
    options.pop();
  }

  saveQuestion(question: Question): void {
    if(question.id != undefined) {
      console.log('send PUT to server question within changed data');
    }
    console.log('I do not create question');
  }

  saveOption(question: Question, option: Option): void {
    let elementNumber = this.questions.indexOf(question);
    let optionsLength = question.options.length;
    if(question.text == undefined || question.text == ''){
      return;
    }
    if(question.id == undefined && question.options[optionsLength-1] == option){
      this.questionService.createQuestion(question)
        .subscribe(
          (ques: Question) => {this.questions[elementNumber] = ques; console.log(ques.id)},
          error => console.log(<any>error)
        );
      return;
    }
    if(question.id != undefined){
      console.log('send put question with this changed option');
    }
    console.log('sumsing that I do not suppose on blur option');
  }
}

