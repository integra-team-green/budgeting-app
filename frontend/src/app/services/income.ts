import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Income } from '../models/income.model';

@Injectable({ providedIn: 'root' })
export class IncomeService {
  private baseUrl = 'http://localhost:8080/api/v1/income';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Income[]> {
    return this.http.get<Income[]>(this.baseUrl);
  }

  create(income: Income): Observable<Income> {
    return this.http.post<Income>(this.baseUrl, income);
  }

  update(id: number, income: Income): Observable<Income> {
    return this.http.put<Income>(`${this.baseUrl}/${id}`, income);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
