import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})

export class SidebarComponent {


  @Output() menuSelect = new EventEmitter<string>();

  menuItems = [
    { label: 'Home', icon: 'pi pi-home', active: false },
    { label: 'User', icon: 'pi pi-user', active: false },
    { label: 'Shared Account', icon: 'pi pi-users', active: false },
    { label: 'Incomes', icon: 'pi pi-dollar',route: '/income',active: false },
    { label: 'Expenses', icon: 'pi pi-minus-circle', active: false },
    { label: 'Payments', icon: 'pi pi-credit-card', active: true },
    { label: 'Savings', icon: 'pi pi-money-bill', active: false },
    { label: 'Settings', icon: 'pi pi-cog', active: false },
    { label: 'Help', icon: 'pi pi-question-circle', active: false },
    { label: 'Log out', icon: 'pi pi-sign-out', active: false }
  ];

  onSelect(item: any) {
    this.menuItems.forEach(i => (i.active = false));
    item.active = true;
    this.menuSelect.emit(item.label);
  }
}
