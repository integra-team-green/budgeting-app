import {bootstrapApplication} from '@angular/platform-browser';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {provideRouter} from '@angular/router';
import {importProvidersFrom} from '@angular/core';
import {FormsModule} from '@angular/forms';
import 'primeicons/primeicons.css';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {appConfig} from './app/app.config';

import {App} from './app/app';
import {routes} from './app/app.routes';
import {provideAnimations} from '@angular/platform-browser/animations';
import {AuthInterceptor} from './app/interceptors/auth.interceptor';
import {ErrorInterceptor} from './app/interceptors/error.interceptor';

bootstrapApplication(App, {
  providers: [
    provideAnimationsAsync(),
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([AuthInterceptor, ErrorInterceptor])
    ),
    provideAnimations(),
    importProvidersFrom(FormsModule)
  ]
});

bootstrapApplication(App, appConfig)
  .catch(err => console.error(err));
