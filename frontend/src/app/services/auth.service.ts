import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import {User} from '../models/user.model';

@Injectable({providedIn: 'root'})
export class AuthService {
  private tokenKey = 'jwt';
  private currentUserKey = 'currentUser';

  constructor(private http: HttpClient) {
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>('/api/auth/login', {email: username, password})
      .pipe(
        tap(res => {
          if (res && res.token) {
            localStorage.setItem(this.tokenKey, res.token);

            this.getCurrentUser().subscribe();
          }
        })
      );
  }

  getCurrentUser(): Observable<User> {
    return this.http.get<User>('api/v1/users/by-email', {
      params: {
        email: this.getEmailFromToken()
      }
    }).pipe(
      tap(user => {
        localStorage.setItem(this.currentUserKey, JSON.stringify(user));
      })
    );
  }

  getEmailFromToken(): string {
    const token = this.getToken();
    console.log("Token:", token);
    const userToken = token ? JSON.parse(atob(token.split('.')[1])).sub : '';
    console.log("Extracted email from token:", userToken);
    return userToken;
  }

  getUser(): User | null {
    const userData = localStorage.getItem(this.currentUserKey);
    return userData ? JSON.parse(userData) as User : null;
  }

  register(name: string, email: string, password: string): Observable<any> {
    return this.http.post<any>('/api/auth/register', {
      name: name,
      email: email,
      password: password
    });
  }


  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.currentUserKey);
  }
}
