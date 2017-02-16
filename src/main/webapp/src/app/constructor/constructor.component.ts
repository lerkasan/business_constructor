import {Component, OnInit} from '@angular/core';
import {Question} from '../model/question';
import {Option} from '../model/option';
import {QuestionService} from '../service/questions.service';
import {Procedure} from '../model/procedure';
import {ProcedureService} from '../service/procedure.service';
import {BusinessType} from '../model/business.type';
import {Questionnaire} from '../model/questionnaire';
import {BusinessTypeService} from '../service/business.type.service';
import {Response} from '@angular/http';

@Component({
  selector: 'brdo-constructor-panel',
  templateUrl: './constructor.component.html',
  styleUrls: ['./constructor.component.scss']

})

export class ConstructorComponent implements OnInit {

  ERROR_MSG_FIRST_SAVE_FIELD: string = 'Поле повинно бути заповнене і збережено!';
  ERROR_MSG_EMPTY_FIELD: string = 'Поле не повинно бути пустим!';
  STYLE_WRONG_SUBMISSION: string = 'wrongSubmission';
  STYLE_SUCCESS_SUBMISSION: string = 'successSubmission';
  ATTACHED_QUESTION: string = 'Приеднати питання';
  ATTACHED_PROCEDURE: string = 'Приеднати картку';

  attachedProcedure: string;
  attachedQuestion: string;
  errorMessage: string;
  selectedOption: Option;
  selectedQuestion: Question;
  selectedQuestionText: string;
  selectedOptionTitle: string;
  questions: Question[];
  procedures: Procedure[];
  businessType: BusinessType;
  questionnaire: Questionnaire;
  wrongBusinessType: boolean = false;
  wrongQuestionnaire: boolean = false;
  wrongQuestion: boolean = false;
  successQuestionnaire: boolean = false;
  successBusinessType: boolean = false;
  successQuestionLink: boolean = false;
  successProcedureLink: boolean = false;
  businessTypes: BusinessType[];
  businessTypeTitleClass: string = '';
  businessTypeKvedClass: string = '';
  questionnaireClass: string = '';
  questionFieldIndexWithChange: number;
  optionFieldIndexWithChange: number;
  questionOptionFieldIndexWithChange: number;
  inputTypeFieldIndexWithChange: number;
  quesIndex: number;

  inputType = [
    {value: 'SINGLE_CHOICE'},
    {value: 'MULTI_CHOICE'}
  ];

  constructor(private questionService: QuestionService,
              private procedureService: ProcedureService,
              private businessTypeService: BusinessTypeService) {
  }

  ngOnInit() {
    this.procedures = [];
    this.businessType = new BusinessType();
    this.businessType.title = '';
    this.businessType.codeKved = '';
    this.questionnaire = new Questionnaire();
    this.questionnaire.title = '';
    this.getBusinessTypes();
    this.getProcedure();
  }

  onSelectQuestion(question: Question): void {
    this.selectedQuestion = question;
    this.selectedQuestionText = question.text;
  }

  onSelectOption(option: Option): void {
    this.selectedOption = option;
    this.selectedOptionTitle = option.title;
    if (option.nextQuestion !== undefined) {
      for (let question of this.questions) {
        if (question.id === option.nextQuestion.id) {
          this.attachedQuestion = question.text;
        }
      }
    } else {
      this.attachedQuestion = this.ATTACHED_QUESTION;
    }
    if (option.procedure !== undefined) {
      for (let procedure of this.procedures) {
        if (procedure.id === option.procedure.id) {
          this.attachedProcedure = procedure.name;
        }
      }
    } else {
      this.attachedProcedure = this.ATTACHED_PROCEDURE;
    }
  }

  addQuestion(): void {
    this.resetErrorStatus();
    if (this.questionnaire.id === undefined) {
      this.errorMessage = this.ERROR_MSG_FIRST_SAVE_FIELD;
      this.questionnaireClass = this.STYLE_WRONG_SUBMISSION;
      this.wrongQuestionnaire = true;
      return;
    }
    if (this.businessType.id === undefined) {
      this.errorMessage = this.ERROR_MSG_FIRST_SAVE_FIELD;
      this.businessTypeTitleClass = this.STYLE_WRONG_SUBMISSION;
      this.businessTypeKvedClass = this.STYLE_WRONG_SUBMISSION;
      this.wrongBusinessType = true;
    }
    if (this.questions === undefined) {
      this.questions = [this.newQuestion()];
    } else {
      this.questions.push(this.newQuestion());
    }
  }

  deleteLastQuestion(): void {
    this.questions.pop();
  }

  newQuestion() {
    let option = new Option();
    option.title = '';
    let question = new Question();
    question.text = '';
    question.questionnaire = new Id(this.questionnaire.id);
    question.options = [option];
    question.inputType = this.inputType[0].value;
    return question;
  }

  newOption(): Option {
    let option = new Option;
    option.title = '';
    return option;
  }

  changeInputType(question: Question): void {
    if (question.inputType === this.inputType[0].value) {
      question.inputType = this.inputType[1].value;
    } else {
      question.inputType = this.inputType[0].value;
    }
    for (let i = 0; i < question.options.length; i++) {
      if (question.options[i].title === undefined || question.options[i].title === '') {
        return;
      }
    }
    if (question.text === undefined || question.text === '') {
      return;
    }

    let elementNumber = this.questions.indexOf(question);
    if (question.id !== undefined) {
      this.questionService.updateQuestion(question)
        .subscribe(
          (response: Response) => {
            if (response.status === 200) {
              this.inputTypeFieldIndexWithChange = elementNumber;
              this.resetStatusSubmissionWithDelay();
              let resQuestion = response.json() as Question;
              for (let locOption of this.questions[elementNumber].options) {
                for (let resOption of resQuestion.options) {
                  if (locOption.title === resOption.title) {
                    locOption.id = resOption.id;
                  }
                }
              }
            }
          },
          error => console.log(<any>error)
        );
    }
  }

  addOption(options: Option[]): void {
    options.push(this.newOption());
  }

  deleteLastOption(options: Option[]): void {
    options.pop();
  }

  saveQuestion(question: Question): void {
    for (let option of question.options) {
      if (option.title === '' || option.title === undefined) {
        return;
      }
    }
    if (question.text === '' || question === undefined) {
      return;
    }
    if (question.text === this.selectedQuestionText) {
      return;
    }
    if (question.id !== undefined) {
      this.questionService.updateQuestion(question)
        .subscribe(
          (response: Response) => {
            if (response.status === 200) {
              let elementNumber = this.questions.indexOf(question);
              this.questionFieldIndexWithChange = elementNumber;
              this.resetStatusSubmissionWithDelay();
              let resQuestion = response.json() as Question;
              for (let locOption of this.questions[elementNumber].options) {
                for (let resOption of resQuestion.options) {
                  if (locOption.title === resOption.title) {
                    locOption.id = resOption.id;
                  }
                }
              }
            }
          },
          error => console.log(<any>error)
        );
      return;
    }
  }

  saveOption(question: Question, option: Option): void {
    let elementNumber = this.questions.indexOf(question);

    if (this.selectedOptionTitle === option.title) {
      return;
    }
    for (let optione of question.options) {
      if (optione.title === undefined || optione.title === '') {
        return;
      }
    }
    if (question.text === undefined || question.text === '') {
      return;
    }
    if (question.id === undefined) {
      this.questionService.createQuestion(question)
        .subscribe(
          (response: Response) => {
            if (response.status === 201) {
              let optionNumber = question.options.indexOf(option);
              let questionNumber = this.questions.indexOf(question);
              this.questionFieldIndexWithChange = questionNumber;
              this.optionFieldIndexWithChange = optionNumber;
              this.questionOptionFieldIndexWithChange = questionNumber;
              this.inputTypeFieldIndexWithChange = questionNumber;
              this.resetStatusSubmissionWithDelay();
              let resQuestion = response.json() as Question;
              this.questions[elementNumber].id = resQuestion.id;
              for (let locOption of this.questions[elementNumber].options) {
                for (let resOption of resQuestion.options) {
                  if (locOption.title === resOption.title) {
                    locOption.id = resOption.id;
                  }
                }
              }
            }
          },
          error => console.log(<any>error)
        );
      return;
    }
    if (question.id !== undefined) {
      this.questionService.updateQuestion(question)
        .subscribe(
          (response: Response) => {
            if (response.status === 200) {
              let questionNumber = this.questions.indexOf(question);
              let optionNumber = question.options.indexOf(option);
              this.optionFieldIndexWithChange = optionNumber;
              this.questionOptionFieldIndexWithChange = questionNumber;
              this.resetStatusSubmissionWithDelay();
              let resQuestion = response.json() as Question;
              for (let locOption of this.questions[elementNumber].options) {
                for (let resOption of resQuestion.options) {
                  if (locOption.title === resOption.title) {
                    locOption.id = resOption.id;
                  }
                }
              }
            }
          },
          error => console.log(<any>error)
        );
    }
  }

  questionLinker(questionText: string, option: Option): void {
    let id: number;
    for (let question of this.questions) {
      if (question.text === questionText) {
        id = question.id;
      }
    }
    if (id === undefined) {
      return;
    }
    let question = new Question();
    question.id = id;
    option.nextQuestion = question;
    this.resetStatusSubmissionWithDelay();

    if (option.id === undefined) {
      return;
    }

    this.questionService.createLinkFromOption(option, this.selectedQuestion.id)
      .subscribe(
        (response: Response) => {
          if (response.status === 200) {
            let resOption = response.json() as Option;
            option.id = resOption.id;
          }
        },
        error => console.log(<any>error)
      );
  }

  procedureLinker(id, option) {
    let procedure = new Procedure();
    procedure.id = +id;
    option.procedure = procedure;
    this.resetStatusSubmissionWithDelay();
    if (option.id === undefined) {
      return;
    }

    this.questionService.createLinkFromOption(option, this.selectedQuestion.id)
      .subscribe(
        (response) => {
          if (response === 200) {
            let resOption = response.json() as Option;
            option.id = resOption.id;
          }
        },
        error => console.log(<any>error)
      );
  }

  getProcedure() {
    this.procedureService.getAllProcedure()
      .subscribe(
        (response: Response) => {
          if (response.status === 200) {
            this.procedures = response.json() as Procedure[];
          }
        },
        error => console.log(<any>error)
      );
  }

  saveBusinessType() {
    this.resetErrorStatus();
    if (this.businessType.title === undefined || this.businessType.title === '') {
      this.errorMessage = this.ERROR_MSG_EMPTY_FIELD;
      this.wrongBusinessType = true;
      this.businessTypeTitleClass = this.STYLE_WRONG_SUBMISSION;
      return;
    }
    if (this.businessType.codeKved === undefined || this.businessType.codeKved === '') {
      this.errorMessage = this.ERROR_MSG_EMPTY_FIELD;
      this.wrongBusinessType = true;
      this.businessTypeKvedClass = this.STYLE_WRONG_SUBMISSION;
      return;
    }
    this.businessTypeService.createBusinessType(this.businessType)
      .subscribe(
        (response: Response) => {
          if (response.status === 201) {
            this.businessType = response.json() as BusinessType;
            this.wrongBusinessType = false;
            this.businessTypeTitleClass = this.STYLE_SUCCESS_SUBMISSION;
            this.businessTypeKvedClass = this.STYLE_SUCCESS_SUBMISSION;
            this.successBusinessType = true;
            this.resetStatusSubmissionWithDelay();
          }
        },
        (failResponse) => {
          if (failResponse !== 201) {
            this.wrongBusinessType = true;
            this.errorMessage = 'Поле КВЄД пара чисел розділених крапкою, або такі поля вже існують!';
            this.businessTypeKvedClass = this.STYLE_WRONG_SUBMISSION;
            this.businessTypeTitleClass = this.STYLE_WRONG_SUBMISSION;
          }
        }
      );
  }

  saveQuestionnare() {
    this.resetErrorStatus();
    if (this.businessType.id === undefined) {
      this.errorMessage = this.ERROR_MSG_FIRST_SAVE_FIELD;
      this.businessTypeKvedClass = this.STYLE_WRONG_SUBMISSION;
      this.businessTypeTitleClass = this.STYLE_WRONG_SUBMISSION;
      this.wrongQuestionnaire = true;
      return;
    }
    if (this.questionnaire.title === undefined || this.questionnaire.title === '') {
      this.errorMessage = this.ERROR_MSG_EMPTY_FIELD;
      this.questionnaireClass = this.STYLE_WRONG_SUBMISSION;
      this.wrongQuestionnaire = true;
      return;
    }
    this.questionnaire.businessType = new Id(this.businessType.id);
    this.questionService.createQuestionare(this.questionnaire)
      .subscribe(
        (response: Response) => {
          if (response.status === 201) {
            let questionnaire = response.json() as Questionnaire;
            this.questionnaire.id = questionnaire.id;
            this.wrongQuestionnaire = false;
            this.questionnaireClass = this.STYLE_SUCCESS_SUBMISSION;
            this.successQuestionnaire = true;
            this.resetStatusSubmissionWithDelay();
          }
        },
        (failResponse: Response) => {
          if (failResponse.status !== 201) {
            this.errorMessage = 'Назва опитувальника вже існує!';
            this.wrongQuestionnaire = true;
            this.questionnaireClass = this.STYLE_WRONG_SUBMISSION;
          }
        }
      );
  }

  getBusinessTypes() {
    if (this.businessTypes === undefined) {
      this.businessTypes = [];
    }
    this.businessTypeService.getBusinessTypes()
      .subscribe(
        (response: BusinessType[]) => {
          this.businessTypes = response;
        },
        (error) => {
          console.log(error);
        }
      );
  }

  chooseTypeBusinessFromServer(id) {
    this.resetErrorStatus();
    for (let businessType of this.businessTypes) {
      if (businessType.id.toString() === id) {
        this.businessType = businessType;
      }
    }
  }

  resetErrorStatus() {
    if (this.businessTypeKvedClass !== this.STYLE_SUCCESS_SUBMISSION) {
      this.businessTypeKvedClass = '';
    }
    if (this.businessTypeTitleClass !== this.STYLE_SUCCESS_SUBMISSION) {
      this.businessTypeTitleClass = '';
    }
    if (this.questionnaireClass !== this.STYLE_SUCCESS_SUBMISSION) {
      this.questionnaireClass = '';
    }
    this.errorMessage = '';
    this.wrongBusinessType = false;
    this.wrongQuestionnaire = false;
    this.wrongQuestion = false;
  }

  resetStatusSubmissionWithDelay() {
    setTimeout(() => {
        this.businessTypeKvedClass = '';
        this.businessTypeTitleClass = '';
        this.questionnaireClass = '';
        this.successBusinessType = false;
        this.successQuestionnaire = false;
        this.successQuestionLink = false;
        this.successProcedureLink = false;
        this.questionFieldIndexWithChange = undefined;
        this.optionFieldIndexWithChange = undefined;
        this.questionOptionFieldIndexWithChange = undefined;
        this.inputTypeFieldIndexWithChange = undefined;
      }, 5000
    );
  }
}

class Id {
  id: number;

  constructor(id: number) {
    this.id = id;
  }
}
