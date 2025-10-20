import {Component, OnInit, OnDestroy} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {User} from '../../models/user.model';
import {Subscription} from 'rxjs';
import {UserService} from '../../services/user.service';
import {CardModule} from 'primeng/card';
import {ButtonModule} from 'primeng/button';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  standalone: true,
  imports: [
    CommonModule,
    CardModule,
    ButtonModule
  ]
})
export class DashboardComponent implements OnInit, OnDestroy {
  currentUser: User | null = null;
  private subscription: Subscription = new Subscription();

  constructor(
    private auth: AuthService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit() {
    this.subscription = this.userService.user$.subscribe(user => {
      if (user) {
        this.currentUser = user;
      } else {
        this.userService.refreshUserData().subscribe({
          error: () => this.logout()
        });
      }
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
