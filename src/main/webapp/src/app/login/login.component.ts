import {Component} from '@angular/core';
import {User} from "../model/user";
import {AuthService} from "../service/auth.service";

@Component({
  selector: 'my-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent {

  user = new User();
  model = this.user;
  submitted = false;
  errorMessage = '';
  mode = 'Observable';

  constructor (private authService: AuthService) {}

  onSubmit() {
    this.submitted = true;
  }

  authRequest() {

    this.authService.authRequest(this.model.username, this.model.password)
      .subscribe(
        user => this.user = user,
        error => this.errorMessage = <any>error
      );
    console.log(this.user.toString());
    this.model = this.user;
  }
}


