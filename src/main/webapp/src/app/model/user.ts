export class User {
  accountNonExpired: boolean;
  accountNonLocked: boolean;
  creationDate: Date;
  credentialsNonExpired: boolean;
  email: string;
  enabled: boolean;
  id: number;
  roles: string[];
  username: string;
  password: string;
  rawPassword: string;
  firstname: string;
  midlename: string;
  lastname: string;


    constructor() {
  }

}
