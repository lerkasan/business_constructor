import {Injectable} from "@angular/core";
import {Http, Response, Headers, RequestOptions} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {Question} from "../model/question";
import {Option} from "../model/option";

@Injectable()
export class QuestionService{

  private question = new Question();

  constructor(private http: Http) {
  }

  public createQuestion(question: Question){
    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });

    return this.http.post('/api/questions', JSON.stringify(question), RequestOptions)
      .map((response) => {
        if(response.status == 200){
          this.question = response.json() as Question;
          localStorage.setItem('question' + this.question.id , JSON.stringify(this.question));
        }
        return response.status;
      })
      .catch(this.handleError);
  }

  public createLinkFromOption(option: Option, questionId: string){
    let path = '/api/questions' + questionId + '/options/' + option.id;
    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });

    return this.http.put(path, JSON.stringify(option), options)
      .map((response) => {
        if(response.status == 200){

        }
      })
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
