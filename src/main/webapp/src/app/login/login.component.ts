import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'my-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']

})
export class LoginComponent{
	user = {
		email: ' ',
		password:''

	};

		onSubmit(form:NgForm){
			console.log(form.value);
		
	}

}