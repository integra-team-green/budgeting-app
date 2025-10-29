import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class MenuService {

  constructor(private router: Router) {}

  navigate(label: string) {
    switch (label) {
      case 'Home':
        this.router.navigate(['/home']);
        break;
      case 'User':
        this.router.navigate(['/user']);
        break;
      case 'Incomes':
        this.router.navigate(['/incomes']);
        break;
      case 'Expenses':
        this.router.navigate(['/expenses']);
        break;
      case 'Payments':
        this.router.navigate(['/payments']);
        break;
      case 'Savings':
        this.router.navigate(['/savings']);
        break;
      case 'Settings':
        this.router.navigate(['/settings']);
        break;
      case 'Help':
        this.router.navigate(['/help']);
        break;
      case 'Log out':
        console.log('Logging out...');
        break;
    }
  }
}
