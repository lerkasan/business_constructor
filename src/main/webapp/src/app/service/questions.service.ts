import {Injectable} from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {Question} from '../model/question';
import {Option} from '../model/option';
import {Questionnaire} from '../model/questionnaire';

@Injectable()
export class QuestionService {
  host = 'http://localhost:8080';

  constructor(private http: Http) {
  }

  public createQuestion(question: Question) {
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});
    options.withCredentials = true;

    return this.http.post(this.host + '/api/questions', JSON.stringify(question), options)
      .map((response) => {
        return response.json() as Question;
      })
      .catch(this.handleError);
  }

  public updateQuestion(question: Question) {
    let path = '/api/questions/' + question.id;
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});
    options.withCredentials = true;

    return this.http.put(this.host + path, JSON.stringify(question), options)
      .map((response) => {
        return response.json() as Question;
      })
      .catch(this.handleError);
  }

  public createLinkFromOption(option: Option, questionId: number) {
    let path = '/api/questions/' + questionId + '/options/' + option.id;
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});
    options.withCredentials = true;

    return this.http.put(this.host + path, JSON.stringify(option), options)
      .map((response) => {
        return response.status as number;
      })
      .catch(this.handleError);

  }

  public createQuestionare(questionare: Questionnaire) {
    let path = '/api/questionnaires';
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});
    options.withCredentials = true;

    return this.http.post(this.host + path, JSON.stringify(questionare), options)
      .map((response) => {
          return response;
        }
      )
      .catch(this.handleError);
  }

  public listQuestionnaires() {
    let path = '/api/questionnaires';
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});
    options.withCredentials = true;
    console.log('Catch me');
    return this.http.get(this.host + path, options)
      .map(
        (response) => {
          return response.json() as Questionnaire[];
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
