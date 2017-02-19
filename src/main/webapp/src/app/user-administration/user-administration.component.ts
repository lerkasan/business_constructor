import {Component, OnInit} from '@angular/core';
import {User} from '../model/user';
import {UserService} from '../service/user.service';
import {Response} from '@angular/http';
import {Router} from '@angular/router';
import {Role} from '../model/role';

@Component({
  selector: 'brdo-user-administration',
  templateUrl: './user-administration.component.html',
  styleUrls: ['./user-administration.component.scss']

})

export class UserAdministrationComponent implements OnInit {

  usersList: User[];
  public roles = [
    {value: 'ROLE_USER', display: 'Юзер'},
    {value: 'ROLE_ADMIN', display: 'Адміністратор'},
    {value: 'ROLE_EXPERT', display: 'Експерт'}
  ];

  constructor(private userService: UserService, private router: Router) {
  }

  ngOnInit() {
    this.getRegisteredUsers();
  }

  private getRegisteredUsers() {
    this.userService.getRegisteredUsers()
      .subscribe(
        (response: Response) => {
          if (response.status === 200) {
            this.usersList = response.json() as User[];
          }
        },
        (response: Response) => {
          if (response.status === 403) {
            this.router.navigate(['/login']);
          }
        }
      );
  }

  public isRoleChecked(roles: Role[], roleValue: string): boolean {
    for (let role of roles) {
      if (role.title === roleValue) {
        return true;
      }
    }
    return false;
  }

  public changeRole(user: User, n: number): void {
    let userIndex = this.usersList.indexOf(user);

    for (let oldRole of this.usersList[userIndex].roles) {
      if (oldRole.title === this.roles[n].value) {
        let roleIndex = this.usersList[userIndex].roles.indexOf(oldRole);
        if (roleIndex > -1) {
          this.usersList[userIndex].roles.splice(roleIndex, 1);
        }
        return;
      }
    }
    let role = new Role;
    role.title = this.roles[n].value;
    this.usersList[userIndex].roles.push(role);
  }

  public saveChangedRoles(user: User) {

    this.userService.updateUser(user)
      .subscribe(
        (response: Response) => {
          if (response.status === 200) {
          }
        },
        (response: Response) => {
          console.log(response);
        }
      );
  }
}


