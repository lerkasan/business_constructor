import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {Response, Http, Headers, RequestOptions} from "@angular/http";
import {User} from "../model/user";

@Injectable()
export class AuthService {

  private url = 'http://localhost:8080/';

  constructor(private http: Http) {
  }

  public authRequest(username: string, password: string): Observable<User> {
    let body = this.authBodyGenerator(username, password);
    let headers = new Headers({'Content-Type': 'multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW'});
    headers.append('cache-control', 'no-cache');
    headers.append('postman-token', '5c363745-122b-7901-b85c-9b17c82c19d6');
    let options = new RequestOptions({headers: headers});

    return this.http.post(this.url + 'login', body, options)
      .map((response:Response) => {response.json() as User; console.log(response.status); console.log(response.json())})
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

  private authBodyGenerator(username: string, password: string) {
    let body = '------WebKitFormBoundary7MA4YWxkTrZu0gW\r\n';
    body += 'Content-Disposition: form-data; name=\"username\"\r\n\r\n' + username + '\r\n';
    body += '------WebKitFormBoundary7MA4YWxkTrZu0gW\r\n';
    body += 'Content-Disposition: form-data; name=\"password\"\r\n\r\n' + password + '\r\n';
    body += '------WebKitFormBoundary7MA4YWxkTrZu0gW--';
    return body;
  }
}
