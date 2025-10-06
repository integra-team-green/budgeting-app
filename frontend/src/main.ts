import {bootstrapApplication} from '@angular/platform-browser';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {provideRouter} from '@angular/router';
import {importProvidersFrom} from '@angular/core';
import {FormsModule} from '@angular/forms';

import {App} from './app/app';
import {routes} from './app/app.routes';

bootstrapApplication(App, {
  providers: [
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
    importProvidersFrom(FormsModule)
  ]
});
