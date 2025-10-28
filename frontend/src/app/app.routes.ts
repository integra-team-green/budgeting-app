import {Routes} from '@angular/router';
import {LoginComponent} from './component/login/login.component';
import {DashboardComponent} from './component/dashboard/dashboard.component';
import {AuthGuard} from './guards/auth.guard';
import {RegisterComponent} from './component/register/register.component';
import { IncomeListComponent } from './component/incomes/income-list/income-list';
import { AddIncomeComponent } from './component/incomes/add-income/add-income';
import { IncomeComponent } from './component/incomes/income_overview/income';
import {MainLayoutComponent} from './component/main-layout/main-layout';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  // {
  //   path: 'dashboard',
  //   component: DashboardComponent,
  //   canActivate: [AuthGuard],
  //   children: [
  {
      path: '',
      component: MainLayoutComponent,
      children: [
        { path: 'income', component: IncomeComponent },
        { path: 'income/list', component: IncomeListComponent },
        { path: 'income/add', component: AddIncomeComponent },
        { path: 'income/edit/:id', component: AddIncomeComponent },
        { path: '', redirectTo: '/income', pathMatch: 'full' },
    ],
  },

  //   ]
  // },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' },
];
