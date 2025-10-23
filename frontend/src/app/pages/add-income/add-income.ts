import {Component, OnInit} from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { InputText } from 'primeng/inputtext';
import {InputNumberModule} from 'primeng/inputnumber';
import {DatePickerModule} from 'primeng/datepicker';
import { Select } from 'primeng/select';
import { Button } from 'primeng/button';
import { Textarea } from 'primeng/textarea';
import {IncomeControllerService} from '../../api';

@Component({
  selector: 'app-add-income',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    InputText,
    DatePickerModule,
    Select,
    Button,
    Textarea,
    InputNumberModule,
  ],
  templateUrl: 'add-income.html',
  styleUrls: ['add-income.css']
})
export class AddIncomeComponent {
  incomeForm: FormGroup;
  frequencyOptions = [
    { label: 'One Time', value: 'one time' },
    { label: 'Monthly', value: 'monthly' },
    { label: 'Yearly', value: 'yearly' },
  ];

  constructor(
    private fb: FormBuilder,
    private incomeService: IncomeControllerService,
    private router: Router
  ) {
    this.incomeForm = this.fb.group({
      amount: [null, [Validators.required, Validators.min(0.01)]],
      source: ['', Validators.required],
      date: [null, Validators.required],
      description: [''],
      frequency: ['one time', Validators.required],
      endDate: [null]
    });
  }

  onSubmit() {
    if (this.incomeForm.invalid) {
      this.incomeForm.markAllAsTouched();
      return;
    }

    const income = this.incomeForm.value;

    this.incomeService.createIncome(income).subscribe({
      next: () => {
        alert('Income saved successfully!');
        this.router.navigate(['/income']);
      },
      error: (err) => {
        console.error(err);
        alert('Error saving income.');
      }
    });
  }
}

