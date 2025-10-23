import {ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection} from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { providePrimeNG } from 'primeng/config';
import { provideAnimations} from '@angular/platform-browser/animations';

import LaraLightBlue from '@primeuix/themes/aura';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {AuthInterceptor} from './interceptors/auth.interceptor';
import {ErrorInterceptor} from './interceptors/error.interceptor';

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
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([
        AuthInterceptor,
        ErrorInterceptor
      ])
    ),
    provideAnimations()
  ]
};
