import { Component, OnInit } from '@angular/core';
import {IncomeDTO} from '../../api';
import {FormBuilder, FormGroup, FormsModule, Validators} from '@angular/forms';
import {RouterLink} from '@angular/router';
import {ButtonModule} from 'primeng/button';
import { TableModule } from 'primeng/table';
import { DatePickerModule } from 'primeng/datepicker';
import { InputTextModule } from 'primeng/inputtext';
import {IncomeControllerService} from '../../api';

@Component({
  selector: 'app-income-list',
  templateUrl: 'income-list.html',
  imports: [
    RouterLink,
    FormsModule,
    ButtonModule,
    TableModule,
    DatePickerModule,
    InputTextModule,
  ],
  styleUrls: ['income-list.css']
})
export class IncomeListComponent implements OnInit {

  incomes: IncomeDTO[] = [];
  filteredIncomes: IncomeDTO[] = [];

  filterDate: string = '';
  filterSource: string = '';
  loading = false;

 constructor(private incomeService: IncomeControllerService) {}

  ngOnInit(): void {
    this.loadIncomes();
  }

  loadIncomes(): void {
    this.loading = true;
    this.incomeService.getAllIncomes().subscribe({
      next: (data) => {
        this.incomes = data;
        this.filteredIncomes = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading incomes:', err);
        this.loading = false;
      }
    });
  }

  filterIncomes(): void {
    const dateFilter = this.filterDate?.toString().trim();
    const sourceFilter = this.filterSource?.toLowerCase().trim();

    this.filteredIncomes = this.incomes.filter(income => {
      const matchesDate = dateFilter ? income.date?.includes(dateFilter) : true;
      const matchesSource = sourceFilter ? income.source?.toLowerCase().includes(sourceFilter) : true;
      return matchesDate && matchesSource;
    });
  }

  deleteIncome(id: number): void {
    if (confirm('Are you sure you want to delete this income?')) {
      this.incomeService.deleteIncome(id).subscribe(() => {
        next:()=> {
          this.incomes = this.incomes.filter(i => i.id !== id);
          this.filteredIncomes = this.filteredIncomes.filter(i => i.id !== id);
        }
        error: (err: any) => console.error('Error deleting income:', err)
      });
    }
  }

}
