import {Component} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {Router, RouterModule} from '@angular/router';
import {InputTextModule} from 'primeng/inputtext';
import {PasswordModule} from 'primeng/password';
import {ButtonModule} from 'primeng/button';
import {MessageModule} from 'primeng/message';
import {Checkbox} from 'primeng/checkbox';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    RouterModule,
    InputTextModule,
    PasswordModule,
    ButtonModule,
    MessageModule
  ]
})
export class LoginComponent {
  username = '';
  password = '';
  error = '';
  isLoading = false;

  constructor(
    private auth: AuthService,
    private router: Router
  ) {
    this.auth.logout();
  }

  submit() {
    if (!this.username || !this.password) {
      this.error = 'Please enter both email and password.';
      return;
    }

    this.isLoading = true;
    this.error = '';

    this.auth.login(this.username, this.password).subscribe({
      next: () => {
        this.isLoading = false;
        console.log('Login successful. Token saved in localStorage');
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.isLoading = false;

        if (typeof err === 'string') {
          this.error = err;
        } else if (err.error && typeof err.error === 'string') {
          this.error = err.error;
        } else {
          this.error = err.message || 'Login failed. Please try again.';
        }

        console.error('Login error:', err);
      }
    });
  }
}
