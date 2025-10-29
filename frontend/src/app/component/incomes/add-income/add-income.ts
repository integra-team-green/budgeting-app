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
import {AuthControllerService, IncomeControllerService, UserControllerService} from '../../../api';
import { ActivatedRoute } from '@angular/router';
import {TokenService} from '../../../services/token.service';

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
export class AddIncomeComponent implements OnInit {
  incomeForm: FormGroup;
  frequencyOptions = [
    { label: 'One Time', value: 'ONE_TIME' },
    { label: 'Monthly', value: 'MONTHLY' },
    { label: 'Yearly', value: 'YEARLY' },
  ];

  incomeId?: number;
  private userId?: number;

  constructor(
    private fb: FormBuilder,
    private incomeService: IncomeControllerService,
    private userService: UserControllerService,
    private tokenService: TokenService,
    private router: Router,
    private route: ActivatedRoute
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

  ngOnInit(): void {
    this.loadCurrentUser();
    this.incomeId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.incomeId) {
      this.loadIncomeForEdit(this.incomeId);
    }
  }

  private loadCurrentUser(): void {
    const email = this.tokenService.getEmailFromToken();
    if (email) {
      this.userService.getUserByEmail(email).subscribe({
        next: (user) => {
          this.userId = user.id;
        },
        error: (err) => console.error('Error loading user:', err)
      });
    }
  }

  loadIncomeForEdit(id: number): void {
    this.incomeService.getIncomeById(id).subscribe({
      next: (income) => {
        this.incomeForm.patchValue({
          amount: income.amount,
          source: income.source,
          date: new Date(income.date ?? ''),
          description: income.description,
          frequency: income.frequency,
          endDate: income.endDate ? new Date(income.endDate) : null,
        });
      },
      error: (err) => console.error('Error loading income:', err)
    });
  }



  onSubmit() {
    if (this.incomeForm.invalid) {
      this.incomeForm.markAllAsTouched();
      return;
    }

    if (!this.userId) {
      alert('User not loaded yet. Please try again.');
      return;
    }

    const income = {
      ...this.incomeForm.value,
      userId: this.userId
    };

    if (this.incomeId) {
      this.incomeService.updateIncome(this.incomeId, income).subscribe({
        next: () => {
          alert('Income updated successfully!');
          this.router.navigate(['/income']);
        },
        error: (err) => {
          console.error(err);
          alert('Error updating income.');
        }
      });
    } else {
      this.incomeService.createIncome(income).subscribe({
        next: () => {
          alert('Income added successfully!');
          this.router.navigate(['/income']);
        },
        error: (err) => {
          console.error(err);
          alert('Error adding income.');
        }
      });
    }

  }
}

