import {
  ApplicationConfig,
  importProvidersFrom,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection
} from '@angular/core';
import {provideRouter} from '@angular/router';
import {provideAnimations} from '@angular/platform-browser/animations';
import {routes} from './app.routes';
import {providePrimeNG} from 'primeng/config';
import {MessageService} from 'primeng/api';
import {ToastModule} from 'primeng/toast';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {AuthInterceptor} from './interceptors/auth.interceptor';
import {ErrorInterceptor} from './interceptors/error.interceptor';
import {BASE_PATH} from './api';
import {FormsModule} from '@angular/forms';
import LaraLightBlue from '@primeuix/themes/aura';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    providePrimeNG({
      theme: {
        preset: LaraLightBlue, // theme can be changed
        options: {
          darkModeSelector: null,
        }
      }
    }),
    { provide: BASE_PATH, useValue: '' },
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([
        (req, next) => {
          const token = localStorage.getItem('jwt');
          if (token) {
            req = req.clone({
              setHeaders: {Authorization: `Bearer ${token}`}
            });
          }
          return next(req);
        }
      ])
    ),
    importProvidersFrom(FormsModule),
    provideZoneChangeDetection({eventCoalescing: true}),
    provideHttpClient(
      withInterceptors([
        AuthInterceptor,
        ErrorInterceptor
      ])
    ),

    provideAnimations(),

    importProvidersFrom(ToastModule),

    MessageService,
    { provide: BASE_PATH, useValue: '' }
  ]
};
