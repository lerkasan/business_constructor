import {Component} from '@angular/core';
import {User} from '../model/user';
import {AuthService} from '../service/auth.service';
import {Router} from '@angular/router';
@Component({
  selector: 'brdo-my-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent {

  wrongcredentials = false;
  model = new User();
  message = '';
  mode = 'Observable';

  constructor(private authService: AuthService, private router: Router) {
  }

  onSubmit() {
    console.log('Must be roted to the home');
  }

  authRequest() {

    this.authService.authRequest(this.model.username, this.model.password)
      .subscribe((result) => {
          if (result === 200) {
            this.router.navigate(['/']);
          }
        },
        (error) => {
          this.message = <any>error;
          this.wrongcredentials = true;
        }
      );
  }
}
