import {Component, OnInit, ElementRef} from '@angular/core';
import {BusinessTypeService} from '../service/business.type.service';
import {BusinessType} from '../model/business.type';
import {Questionnaire} from '../model/questionnaire';
import {QuestionService} from '../service/questions.service';
import {Question} from '../model/question';
import {Procedure} from '../model/procedure';
import {ProcedureService} from '../service/procedure.service';
import {Option} from '../model/option';
import {Response} from '@angular/http';

declare var $: any;

@Component({
  selector: 'brdo-my-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  PROCEDURE_IS_NOT_SELCTED = 'Виберіть будь ласка процедуру у лівому списку!';

  businessType: BusinessType;
  procedure: Procedure;
  procedures: Procedure[];
  businessTypes: BusinessType[];
  questionnaires: Questionnaire[];
  questions: Question[];
  selectedQuestionnaire: Questionnaire;
  selectedBusinessType: BusinessType;

  constructor(private _elmRef: ElementRef, private businessTypeService: BusinessTypeService,
              private questionService: QuestionService, private procedureService: ProcedureService) {
  }

  ngOnInit() {
    $(this._elmRef.nativeElement).find('[data-toggle="tooltip"]').tooltip();
    $(this._elmRef.nativeElement).find('.spoiler-trigger').click(function () {
      $(this).parent().next().collapse('toggle');
    });
    this.procedures = [];
    this.getBusinessTypes();
    this.getQuestionnair();
    this.procedureInit();
    this.businessType = new BusinessType();
    this.businessType.codeKved = '';
    this.businessType.title = '';
  }

  getBusinessTypes() {
    if (this.businessTypes === undefined) {
      this.businessTypes = [new BusinessType()];
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

  getQuestionnair() {
    if (this.questionnaires === undefined) {
      let questionnaire = new Questionnaire();
      questionnaire.questions = [new Question];
      questionnaire.businessType = new BusinessType;
      this.questionnaires = [questionnaire];
    }
    this.questionService.listQuestionnaires()
      .subscribe(
        (response) => {
          this.questionnaires = response;
        },
        (error) => {
          console.log(error);
        }
      );
  }

  onSelectQuestionnaire(id) {
    this.questions = [];
    this.procedures = [];
    for (let questionnaire of this.questionnaires) {
      if (questionnaire.id.toString() === id) {
        this.businessType = questionnaire.businessType;
        this.selectedQuestionnaire = questionnaire;
        this.selectedQuestionnaire.questions.sort(
          function (a, b) {
            return a.id - b.id;
          }
        );
        this.questions.push(this.selectedQuestionnaire.questions[0]);
      }
    }
  }

  onSelectBusinessType(id) {
    for (let businessType of this.businessTypes) {
      if (businessType.id.toString() === id) {
        this.selectedBusinessType = businessType;
      }
    }
  }

  nextQuestion(option, question) {
    if (question.inputType === 'MULTI_CHOICE') {
      if (this.ifQuestionIsPresent(this.findQuestionById(option.nextQuestion.id))) {
        return;
      }
      this.questions.push(this.findQuestionById(option.nextQuestion.id));
      return;
    }
    if (question.inputType === 'SINGLE_CHOICE') {
      if (option.nextQuestion === undefined) {
        return;
      }
      if (this.ifQuestionIsPresent(this.findQuestionById(option.nextQuestion.id))) {
        return;
      }
      let questionLength = this.questions.length;
      if (question.id !== this.questions[questionLength - 1].id) {
        return;
      }
      this.questions.push(this.findQuestionById(option.nextQuestion.id));
    }
  }

  nextProcedure(option: Option) {
    if (option.procedure === undefined) {
      return;
    }
    if (this.ifProcedureIsPresent(option.procedure.id)) {
      return;
    }
    this.procedureService.getProcedure(option.procedure.id)
      .subscribe(
        (response: Response) => {
          let procedure = response.json() as Procedure;
          this.procedures.push(procedure);
        },
      );
  }

  onSelectProcedure(procedure: Procedure) {
    this.procedure = procedure;
  }

  findQuestionById(id: number) {
    for (let question of this.selectedQuestionnaire.questions) {
      if (question.id === id) {
        return question;
      }
    }
  }

  ifQuestionIsPresent(question): boolean {
    for (let q of this.questions) {
      if (q === question) {
        return true;
      }
    }
    return false;
  }

  ifProcedureIsPresent(id: number): boolean {
    for (let p of this.procedures) {
      if (id === p.id) {
        return true;
      }
    }
    return false;
  }

  procedureInit() {
    this.procedure = new Procedure();
    this.procedure.reason = this.PROCEDURE_IS_NOT_SELCTED;
    this.procedure.result = this.PROCEDURE_IS_NOT_SELCTED;
    this.procedure.cost = this.PROCEDURE_IS_NOT_SELCTED;
    this.procedure.term = this.PROCEDURE_IS_NOT_SELCTED;
    this.procedure.method = this.PROCEDURE_IS_NOT_SELCTED;
    this.procedure.decision = this.PROCEDURE_IS_NOT_SELCTED;
    this.procedure.deny = this.PROCEDURE_IS_NOT_SELCTED;
    this.procedure.abuse = this.PROCEDURE_IS_NOT_SELCTED;
  }
}
