import {Component} from '@angular/core';
import {User} from "../model/user";
import {AuthService} from "../service/auth.service";
import { Router } from '@angular/router';
@Component({
  selector: 'my-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent {

  model = new User();
  errorMessage = '';
  mode = 'Observable';

  constructor (private authService: AuthService, private router: Router) {}

  onSubmit() {
    console.log('Must be roted to the home');
  }

  authRequest() {

    console.log('I am hear');
    this.authService.authRequest(this.model.username, this.model.password)
      .subscribe((result) => {
        if(result == 200) {
          this.router.navigate(['/']);
        }
      },
        (error) => { this.errorMessage = <any>error; console.log(this.errorMessage)}
      );
      // .subscribe(
      //   user => this.model = user,
      //   error => this.errorMessage = <any>error
      // );
  }
}
