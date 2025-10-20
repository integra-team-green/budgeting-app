import { Component, OnInit } from '@angular/core';
import { IncomeService } from '../../services/income';
import { Income } from '../../models/income.model';
import {FormBuilder, FormGroup, FormsModule, Validators} from '@angular/forms';
import {RouterLink} from '@angular/router';
import {ButtonModule} from 'primeng/button';
import { TableModule } from 'primeng/table';
import { DatePickerModule } from 'primeng/datepicker';
import { InputTextModule } from 'primeng/inputtext';

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

  incomes: Income[] = [];
  filteredIncomes: Income[] = [];

  filterDate: string = '';
  filterSource: string = '';
  incomeForm!: FormGroup;
  editMode = false;
  selectedId?: number;

 constructor(private incomeService: IncomeService) {}

  ngOnInit(): void {
    this.loadIncomes();
    //this.initForm();
  }
//
//   // initForm() {
//   //   this.incomeForm = this.fb.group({
//   //     source: ['', Validators.required],
//   //     amount: [0, [Validators.required, Validators.min(0.01)]],
//   //     date: ['', Validators.required],
//   //     description: [''],
//   //     frequency: ['one time', Validators.required]
//   //   });
//   // }
//
  loadIncomes() {
    this.incomeService.getAll().subscribe(data => {
      this.incomes = data;
      this.filteredIncomes = data;
    });
  }
//

  filterIncomes(): void {
    this.filteredIncomes = this.incomes.filter(income => {
      const matchesDate = this.filterDate ? income.date.includes(this.filterDate) : true;
      const matchesSource = this.filterSource
        ? income.source.toLowerCase().includes(this.filterSource.toLowerCase())
        : true;
      return matchesDate && matchesSource;
    });
  }
//   onSubmit() {
//     if (this.incomeForm.invalid) return;
//
//     const formValue = this.incomeForm.value;
//
//     if (this.editMode && this.selectedId) {
//       this.incomeService.update(this.selectedId, formValue).subscribe(() => {
//         this.loadIncomes();
//         this.resetForm();
//       });
//     } else {
//       this.incomeService.create(formValue).subscribe(() => {
//         this.loadIncomes();
//         this.resetForm();
//       });
//     }
//   }
//
//   editIncome(income: Income) {
//     this.incomeForm.patchValue(income);
//     this.editMode = true;
//     this.selectedId = income.id;
//   }
//
  deleteIncome(id: number): void {
    if (confirm('Are you sure you want to delete this income?')) {
      this.incomeService.delete(id).subscribe(() => {
        this.incomes = this.incomes.filter(i => i.id !== id);
        this.filteredIncomes = this.filteredIncomes.filter(i => i.id !== id);
      });
    }
  }
//
//   resetForm() {
//     this.editMode = false;
//     this.selectedId = undefined;
//     this.incomeForm.reset({ frequency: 'one time' });
//   }
}
