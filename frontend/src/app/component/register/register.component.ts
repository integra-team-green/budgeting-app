import {Component} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {AuthService} from '../../services/auth.service';
import {Router, RouterLink} from '@angular/router';

@Component({
    selector: 'app-register',
    templateUrl: 'register.component.html',
    styleUrls: ["register.compnent.css"],
    standalone: true,
    imports: [FormsModule, CommonModule, RouterLink]
  }
)
export class RegisterComponent {
  name = '';
  email = '';
  password = '';
  confirm_password = '';
  error = '';
  isLoading = false;
  successMessage = '';

  constructor(
    private auth: AuthService,
    private router: Router
  ) {
    this.auth.logout();
  }

  register() {

    if (this.password !== this.confirm_password) {
      this.error = 'Passwords do not match.';
      return;
    }

    this.isLoading = true;
    this.error = '';
    this.successMessage = '';

    this.auth.register(this.name, this.email, this.password).subscribe({
        next: (response) => {
          this.isLoading = false;
          this.successMessage = response.message || 'Registration successful!';

          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000)
        },
        error: (err) => {
          this.isLoading = false;

          if (typeof err === 'string') {
            this.error = err;
          } else if (err.error && typeof err.error === 'string') {
            this.error = err.error;
          } else {
            this.error = err.message || 'Registration failed. Please try again.';
          }

          console.error('Register error:', err);

        }
      }
    );
  }
}
