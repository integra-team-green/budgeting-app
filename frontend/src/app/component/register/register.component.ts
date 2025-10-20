import {Component} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {AuthService} from '../../services/auth.service';
import {Router, RouterLink} from '@angular/router';
import {InputTextModule} from 'primeng/inputtext';
import {PasswordModule} from 'primeng/password';
import {ButtonModule} from 'primeng/button';
import {MessageModule} from 'primeng/message';

@Component({
  selector: 'app-register',
  templateUrl: 'register.component.html',
  styleUrls: ["register.component.css"],
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    RouterLink,
    InputTextModule,
    PasswordModule,
    ButtonModule,
    MessageModule
  ]
})
export class RegisterComponent {
  name = '';
  email = '';
  password = '';
  confirm_password = '';
  error = '';
  isLoading = false;
  successMessage = '';

  fieldErrors = {
    name: '',
    email: '',
    password: '',
    confirmPassword: ''
  };

  constructor(
    private auth: AuthService,
    private router: Router
  ) {
    this.auth.logout();
  }

  register() {
    // Reset all errors
    this.resetErrors();

    if (this.password !== this.confirm_password) {
      this.fieldErrors.confirmPassword = 'Passwords do not match.';
      return;
    }

    this.isLoading = true;

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
          this.parseErrors(err);
        } else if (err.error && typeof err.error === 'string') {
          this.parseErrors(err.error);
        } else {
          this.error = err.message || 'Registration failed. Please try again.';
        }

        console.error('Register error:', err);
      }
    });
  }

  private resetErrors() {
    this.error = '';
    this.fieldErrors = {
      name: '',
      email: '',
      password: '',
      confirmPassword: ''
    };
  }

  private parseErrors(errorString: string) {
    // Handle comma-separated field errors from backend
    if (errorString.includes(': ')) {
      const errorParts = errorString.split(', ');

      errorParts.forEach(part => {
        const [field, message] = part.split(': ');

        switch(field.toLowerCase()) {
          case 'name':
            this.fieldErrors.name = message;
            break;
          case 'email':
            this.fieldErrors.email = message;
            break;
          case 'password':
            this.fieldErrors.password = message;
            break;
          default:
            // For any other errors, add to general error message
            this.error += (this.error ? ', ' : '') + part;
        }
      });
    } else {
      // If not in field: message format, treat as general error
      this.error = errorString;
    }
  }
}
