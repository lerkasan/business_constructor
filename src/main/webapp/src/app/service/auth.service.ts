import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {Response, Http, Headers, RequestOptions} from "@angular/http";
import {User} from "../model/user";

@Injectable()
export class AuthService {

  private authenticatedUser = new User;
  private url = 'http://localhost:8080/';

  constructor(private http: Http) {
  }

  public authRequest(username: string, password: string) {
    let body = this.authBodyGenerator(username, password);
    let headers = new Headers({'Content-Type': 'multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW'});
    headers.append('cache-control', 'no-cache');
    let options = new RequestOptions({headers: headers});

    return this.http.post(this.url + 'login', body, options)
      .map((res) => {
        if (res.status == 200) {
          this.authenticatedUser = res.json() as User;
          localStorage.setItem('currentUser', JSON.stringify(this.authenticatedUser));
          console.log(res.json());
          console.log(this.authenticatedUser.roles);
        }
        return res.status;
      })
      .catch(this.handleError);
  }

  public logout() {
    localStorage.removeItem('currentUser');
    return this.http.get(this.url + 'logout')
      .subscribe();
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

  public getAuthUser() {
    return this.authenticatedUser;
  }

}
