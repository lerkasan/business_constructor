import {Injectable} from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {BusinessType} from '../model/business.type';

@Injectable()
export class BusinessTypeService {

  constructor(private http: Http) {
  }

  public getBusinessTypes() {
    let url = '/api/business-types';
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});

    return this.http.get(url, options)
      .map(
        (response) => {
          return response.json() as BusinessType[];
        }
      )
      .catch(this.handleError);
  }

  public createBusinessType(businessType: BusinessType) {
    let path = '/api/business-types';
    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});

    return this.http.post(path, JSON.stringify(businessType), options)
      .map((response) => {
        return response;
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
