import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import {User} from "../model/user";
import {Observable} from "rxjs/Observable";
import { Headers, RequestOptions } from '@angular/http';

@Injectable()
export class RegistrationService {

  constructor(private http: Http) {
  }

  public registrationRequest(user: User){
    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });

    return this.http.post("/api/users", JSON.stringify(user), options)
      .map((res) => {return res.status})
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
