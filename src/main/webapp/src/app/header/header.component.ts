import { Component, OnInit, ElementRef } from '@angular/core';
import {AuthService} from "../service/auth.service";
import {User} from "../model/user";


@Component({
  selector: 'my-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  loggedin = true;
  model = new User;

  constructor(private authService: AuthService) {
    // Do stuff
  }

  ngOnInit() {
    if(this.authService.getAuthUser().username != undefined){
      this.loggedin = false;
      this.model = this.authService.getAuthUser();
    }
  }

}
