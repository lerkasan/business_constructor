import { Component, OnInit } from '@angular/core';
import {User} from "../model/user";
import {Router} from "@angular/router";
import {RegistrationService} from "../service/registration.service";

@Component({
  selector: 'my-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  user = new User();
  duplicatePassword = new DuplicatePassword();
  message = '';


  constructor(private registrationService: RegistrationService, private router: Router) {
  }

  ngOnInit() {
  }

  registrationAway(){
    this.router.navigate(['/']);
  }

  registrationRequest(){

    this.user.rawPassword = this.user.password;
    this.registrationService.registrationRequest(this.user)
      .subscribe((status) => {
        if(status == 201){
          this.message = 'Юзера створенно';
          this.user.password = '';
          this.user.rawPassword = '';
        }
        },
        (error) => { this.message = <any>error}
    );
  }

}

class DuplicatePassword {
  duplicatePassword: string;

  constructor(){}
}
