import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { MessageModule } from 'primeng/message';
import { SidebarPaymentComponent } from '../sidebar/sidebar.component';
import { MenuService } from '../services/menu.service';
import { PaymentControllerService, PaymentDTO } from '../../../api';
import { CheckboxModule } from 'primeng/checkbox';


@Component({
  selector: 'app-add-payment',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    ToastModule,
    ButtonModule,
    MessageModule,
    SidebarPaymentComponent,
    CheckboxModule
  ],
  providers: [MessageService],
  templateUrl: './add-payments.component.html',
  styleUrls: ['./add-payments.component.css']
})
export class AddPaymentComponent {
  payment: PaymentDTO = {
    name: '',
    status: undefined,
    paymentDate: '',
    expenseId: undefined
  };

  addMore = false;
  submitted = false;
  isLoading = false;

  isNameInvalid = false;
  isStatusInvalid = false;
  isDateInvalid = false;
  isExpenseIdInvalid = false;
  expenseErrorMessage = '';

  constructor(
    private router: Router,
    private messageService: MessageService,
    private menuService: MenuService,
    private paymentService: PaymentControllerService
  ) {}

  onNameInput() {
    this.isNameInvalid = !this.payment.name?.trim();
  }

  onStatusChange() {
    this.isStatusInvalid = !this.payment.status;
  }

  onDateInput() {
    this.isDateInvalid = !this.payment.paymentDate;
  }

  onExpenseIdInput(event: Event) {
    const input = event.target as HTMLInputElement;
    const value = input.value.trim();
    const num = Number(value);

    if (!value) {
      this.expenseErrorMessage = 'Please provide a valid expense ID.';
      this.isExpenseIdInvalid = true;
    } else if (isNaN(num) || num <= 0) {
      this.expenseErrorMessage = 'Expense ID must be a positive number.';
      this.isExpenseIdInvalid = true;
    } else {
      this.payment.expenseId = num;
      this.isExpenseIdInvalid = false;
      this.expenseErrorMessage = '';
    }
  }


  savePayment(form: NgForm) {
    this.submitted = true;


    this.isNameInvalid = !this.payment.name?.trim();
    this.isStatusInvalid = !this.payment.status;
    this.isDateInvalid = !this.payment.paymentDate;
    this.isExpenseIdInvalid = !this.payment.expenseId || this.payment.expenseId <= 0;

    if (this.isExpenseIdInvalid && !this.payment.expenseId) {
      this.expenseErrorMessage = 'Please provide a valid expense ID.';
    } else if (this.isExpenseIdInvalid && this.payment.expenseId! <= 0) {
      this.expenseErrorMessage = 'Expense ID must be positive.';
    }

    if (this.isNameInvalid || this.isStatusInvalid || this.isDateInvalid || this.isExpenseIdInvalid) {
      return;
    }

    if (this.payment.paymentDate && typeof this.payment.paymentDate !== 'string') {
      this.payment.paymentDate = (this.payment.paymentDate as any)
        .toISOString()
        .split('T')[0];
    }


    const payload: PaymentDTO = {
      name: this.payment.name!.trim(),
      status: this.payment.status!,
      paymentDate: this.payment.paymentDate!,
      expenseId: this.payment.expenseId!
    };

    console.log('📤 Sending payment payload:', payload);
    this.isLoading = true;

    this.paymentService.createPayment(payload).subscribe({
      next: () => {
        this.isLoading = false;
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Payment added successfully!',
          life: 3000,
        });

        setTimeout(() => {
          if (this.addMore) {
            form.resetForm({
              name: '',
              status: '',
              paymentDate: '',
              expenseId: ''
            });
            this.submitted = false;
          } else {
            this.router.navigate(['/all-payments']);
          }
        }, 1200);
      },
      error: (err) => {
        this.isLoading = false;
        console.error('Payment creation failed:', err);

        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to add payment. Please check the data or try again later.',
          life: 4000,
        });
      },
    });
  }


  cancelEdit() {
    this.router.navigate(['/all-payments']);
  }


  onMenuSelect(label: string) {
    this.menuService.navigate(label);
  }
}
