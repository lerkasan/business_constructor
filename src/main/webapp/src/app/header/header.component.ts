import {Component, OnInit} from '@angular/core';
import {AuthService} from '../service/auth.service';
import {User} from '../model/user';


@Component({
  selector: 'brdo-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  loggedin = true;
  user = new User;
  model = this.user;

  constructor(private authService: AuthService) {
    // Do stuff
  }

  ngOnInit() {
    if (localStorage.getItem('currentUser')) {
      this.loggedin = false;
      this.user = JSON.parse(localStorage.getItem('currentUser'));
      this.model = this.user;
    }
  }

  logout() {
    this.loggedin = true;
    this.authService.logout();
  }

}
