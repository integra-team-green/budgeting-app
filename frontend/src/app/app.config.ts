import {
  ApplicationConfig,
  importProvidersFrom,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection
} from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { providePrimeNG } from 'primeng/config';
import {provideAnimations} from '@angular/platform-browser/animations';

import LaraLightBlue from '@primeuix/themes/aura';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {AuthInterceptor} from './interceptors/auth.interceptor';
import {ErrorInterceptor} from './interceptors/error.interceptor';
import {FormsModule} from '@angular/forms';
import {BASE_PATH} from './api';

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
  ]
};
