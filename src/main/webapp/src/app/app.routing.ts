import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { CarouselComponent } from './carousel/carousel.component';
import { AdminComponent } from './admin/admin.component';
import { ConstructorComponent } from './constructor/constructor.component';
import { UserAdministrationComponent } from './user-administration/user-administration.component';
import { UserStatisticsComponent } from './user-statistics/user-statistics.component';


const routes: Routes = [
  { path: '', component: HomeComponent},
  { path: 'register', component: RegisterComponent},
  { path: 'login', component: LoginComponent},
  { path: 'carousel', component: CarouselComponent},
  { path: 'admin', component: AdminComponent},
  { path: 'constructor', component: ConstructorComponent},
  { path: 'user-administration', component: UserAdministrationComponent},
  { path: 'user-statistics', component: UserStatisticsComponent},
  { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(routes);

