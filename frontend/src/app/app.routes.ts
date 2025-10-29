import {Routes} from '@angular/router';
import {LoginComponent} from './component/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {RegisterComponent} from './component/register/register.component';
import {IncomeListComponent} from './component/incomes/income-list/income-list';
import {AddIncomeComponent} from './component/incomes/add-income/add-income';
import {IncomeComponent} from './component/incomes/income_overview/income';

//import { TestComponent } from './component/test-component/test-component';
import {OverviewComponent} from './component/payments/overview-payments/overview.component';
import {AllPaymentsComponent} from './component/payments/list-payments/all-payments.component';
import {AddPaymentComponent} from './component/payments/add-payments/add-payments.component';
import {EditPaymentComponent} from './component/payments/edit-payments/edit-payments.component';
import {SidebarPaymentComponent} from './component/payments/sidebar/sidebar.component';

import {UserComponent} from './component/user-ui/user.component';
import {LandingComponent} from './component/landing/landing.component';

export const routes: Routes = [
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  {
    path: '',
    redirectTo: 'payments',
    pathMatch: 'full'
  },
  {
    path: 'payments',
    component: OverviewComponent
  },
  {
    path: 'all-payments',
    component: AllPaymentsComponent
  },
  {
    path: 'add-payments',
    component: AddPaymentComponent
  },
  {
    path: 'edit-payments',
    component: EditPaymentComponent
  },
  {
    path: 'sidebar-payments',
    component: SidebarPaymentComponent
  },
  {path: '', redirectTo: '/landing-page', pathMatch: 'full'},
  {path: 'landing-page', component: LandingComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  { path: 'income', component: IncomeComponent , canActivate: [AuthGuard]},
  { path: 'income/list', component: IncomeListComponent , canActivate: [AuthGuard]},
  { path: 'income/add', component: AddIncomeComponent , canActivate: [AuthGuard]},
  { path: 'income/edit/:id', component: AddIncomeComponent , canActivate: [AuthGuard]},
  {path: 'user', component: UserComponent, canActivate: [AuthGuard]},
];
