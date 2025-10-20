import {Routes} from '@angular/router';
import {LoginComponent} from './component/login/login.component';
import {DashboardComponent} from './component/dashboard/dashboard.component';
import {AuthGuard} from './guards/auth.guard';
import {RegisterComponent} from './component/register/register.component';
import { Routes } from '@angular/router';
import { IncomeListComponent } from './pages/income-list/income-list';
import { AddIncomeComponent } from './pages/add-income/add-income';
import { IncomeComponent } from './pages/income/income';

export const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard]},
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  { path: '', redirectTo: 'income', pathMatch: 'full' },
  { path: 'income', component: IncomeComponent },
  { path: 'income/list', component: IncomeListComponent },
  { path: 'income/add', component: AddIncomeComponent },
  { path: '**', redirectTo: 'income' },
];
