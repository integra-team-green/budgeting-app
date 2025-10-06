import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';

export const ErrorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = '';

      if (error.error instanceof Blob && error.error.type.includes('text')) {
        // For text responses from Spring Boot
        return new Promise<any>((resolve) => {
          const reader = new FileReader();
          reader.onload = () => {
            errorMessage = reader.result as string;
            console.log('Extracted error message from blob:', errorMessage);
            resolve(throwError(() => ({status: error.status, error: errorMessage, message: errorMessage})));
          };
          reader.readAsText(error.error);
        });
      } else if (typeof error.error === 'string') {
        errorMessage = error.error;
      } else if (error.error?.message) {
        errorMessage = error.error.message;
      }

      // If no specific message was extracted, use a default
      if (!errorMessage) {
        errorMessage = 'An error occurred. Please try again.';
      }

      console.log('Error intercepted:', errorMessage);

      // Return throwError with the extracted message
      return throwError(() => ({
        status: error.status,
        error: errorMessage,
        message: errorMessage
      }));
    })
  );
};


