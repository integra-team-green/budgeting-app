import {Injectable} from '@angular/core';
import {BehaviorSubject, interval, Observable, startWith, switchMap, tap} from 'rxjs';
import {User} from '../models/user.model';
import {HttpClient} from '@angular/common/http';
import {catchError} from 'rxjs/operators';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root'
})

export class UserService{
  private userSubject = new BehaviorSubject<User | null>(null);
  public user$ = this.userSubject.asObservable();

  constructor(private http: HttpClient, private authService: AuthService) {
    const storedUser = this.authService.getUser();

    if (storedUser) {
      this.userSubject.next(storedUser);
    }

    this.startPeriodicRefresh();
  }

  refreshUserData(): Observable<User> {
    return this.http.get<User>('api/v1/users/by-email', {
      params: {
        email: this.authService.getEmailFromToken()
      }
    }).pipe(
      tap(user => {
        this.userSubject.next(user);
        localStorage.setItem('currentUser', JSON.stringify(user))
      }),
      catchError(err => {
        console.error('Error fetching user data: ', err);
        throw err;
      })
    );
  }

  private startPeriodicRefresh() {
    interval(10000).pipe(
      startWith(0),
      switchMap(() => {
        if (this.authService.getToken()) {
          return this.refreshUserData();
        }
        return [];
      })
    ).subscribe();
  }
}
