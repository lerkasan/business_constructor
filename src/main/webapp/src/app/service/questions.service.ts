import {Injectable} from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {Question} from '../model/question';
import {Option} from '../model/option';
import {BusinessType} from '../model/business.type';
import {Questionnare} from "../model/questionnaire";

@Injectable()
export class QuestionService {

  constructor(private http: Http) {
  }

  public createQuestion(question: Question) {
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});

    return this.http.post('/api/questions', JSON.stringify(question), options)
      .map((response) => {
        return response.json() as Question;
      })
      .catch(this.handleError);
  }

  public updateQuestion(question: Question) {
    let path = '/api/questions/' + question.id;
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});

    return this.http.put(path, JSON.stringify(question), options)
      .map((response) => {
        return response.json() as Question;
      })
      .catch(this.handleError);
  }

  public createLinkFromOption(option: Option, questionId: number) {
    let path = '/api/questions/' + questionId + '/options/' + option.id;
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});

    return this.http.put(path, JSON.stringify(option), options)
      .map((response) => {
        return response.status as number;
      })
      .catch(this.handleError);

  }

  public createBusinessType(businessType: BusinessType) {
    let path = '/api/business-types';
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});

    return this.http.post(path, JSON.stringify(businessType), options)
      .map((response) => {
        return response.json() as BusinessType;
      })
      .catch(this.handleError);
  }

  public createQuestionare(questionare: Questionnare) {
    let path = '/api/questionnaires';
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});

    return this.http.post(path, JSON.stringify(questionare), options)
      .map((response) => {
          return response.json() as Questionnare;
        }
      )
      .catch(this.handleError);
  }

  private handleError(error: Response | any) {
    let errMsg: string;
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = body.error || JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);
    return Observable.throw(errMsg);
  }
}
