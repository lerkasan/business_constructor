import {Component, OnInit, ElementRef} from '@angular/core';
import {BusinessTypeService} from '../service/business.type.service';
import {BusinessType} from '../model/business.type';
import {Questionnaire} from '../model/questionnaire';
import {QuestionService} from '../service/questions.service';
import {Question} from '../model/question';

declare var $: any;

@Component({
  selector: 'brdo-my-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  businessTypes: BusinessType[];
  questionnaires: Questionnaire[];
  questions: Question[];
  selectedQuestionnaire: Questionnaire;
  selectedBusinessType: BusinessType;

  constructor(private _elmRef: ElementRef, private businessTypeService: BusinessTypeService,
              private questionService: QuestionService) {
  }

  ngOnInit() {
    $(this._elmRef.nativeElement).find('[data-toggle="tooltip"]').tooltip();
    $(this._elmRef.nativeElement).find(".spoiler-trigger").click(function () {
      $(this).parent().next().collapse('toggle');
    });
    this.getBusinessTypes();
    this.getQuestionnair();
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
    for (let questionnaire of this.questionnaires) {
      if (questionnaire.id.toString() === id) {
        this.selectedQuestionnaire = questionnaire;
        this.questions = this.selectedQuestionnaire.questions;
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
}
