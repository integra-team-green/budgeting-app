import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { MessageModule } from 'primeng/message';
import { ToastModule } from 'primeng/toast';
import { SidebarPaymentComponent } from '../sidebar/sidebar.component';
import { MenuService } from '../services/menu.service';
import { PaymentControllerService, PaymentDTO } from '../../../api';

@Component({
  selector: 'app-edit-payment',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    ToastModule,
    ButtonModule,
    MessageModule,
    SidebarPaymentComponent,
  ],
  providers: [MessageService],
  templateUrl: './edit-payments.component.html',
  styleUrls: ['./edit-payments.component.css'],
})
export class EditPaymentComponent implements OnInit {
  payment: PaymentDTO = {};
  submitted = false;

  isNameInvalid = false;
  isStatusInvalid = false;
  isDateInvalid = false;
  isExpenseIdInvalid = false;
  expenseErrorMessage = '';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService,
    private menuService: MenuService,
    private paymentService: PaymentControllerService
  ) {}

  ngOnInit(): void {
    const nav = history.state;

    if (nav && nav.payment) {
      this.payment = { ...nav.payment };
      console.log('Loaded payment from navigation state:', this.payment);
      return;
    }

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadPaymentById(+id);
    }
  }


  loadPaymentById(id: number): void {
    this.paymentService
      .getPaymentById(id, 'body', false, { httpHeaderAccept: '*/*' })
      .subscribe({
        next: (data: any) => {
          if (data instanceof Blob) {
            data.text().then((text) => {
              try {
                this.payment = JSON.parse(text);
              } catch (e) {
                console.error('Error parsing payment', e);
              }
            });
          } else {
            this.payment = data;
          }
        },
        error: (err) => {
          console.error('Failed to load payment', err);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Could not load payment data.',
          });
          this.router.navigate(['/all-payments']);
        },
      });
  }


  onNameInput(): void {
    this.isNameInvalid = !this.payment.name?.trim();
  }

  onStatusChange(): void {
    this.isStatusInvalid = !this.payment.status;
  }

  onDateInput(): void {
    this.isDateInvalid = !this.payment.paymentDate;
  }

  onExpenseIdInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const value = input.value.trim();
    const num = Number(value);

    if (!value) {
      this.payment.expenseId = 0;
      this.expenseErrorMessage = 'Please provide a valid expense id.';
      this.isExpenseIdInvalid = true;
    } else if (isNaN(num) || num <= 0) {
      this.payment.expenseId = 0;
      this.expenseErrorMessage = 'Expense Id must be positive.';
      this.isExpenseIdInvalid = true;
    } else {
      this.payment.expenseId = num;
      this.isExpenseIdInvalid = false;
      this.expenseErrorMessage = '';
    }
  }


  savePayment(form: NgForm): void {
    this.submitted = true;


    this.isNameInvalid = !this.payment.name?.trim();
    this.isStatusInvalid = !this.payment.status;
    this.isDateInvalid = !this.payment.paymentDate;
    this.isExpenseIdInvalid = !this.payment.expenseId || this.payment.expenseId <= 0;

    if (this.isExpenseIdInvalid && !this.payment.expenseId) {
      this.expenseErrorMessage = 'Please provide a valid expense id.';
    } else if (this.isExpenseIdInvalid && this.payment.expenseId! <= 0) {
      this.expenseErrorMessage = 'Expense Id must be positive.';
    }

    if (this.isNameInvalid || this.isStatusInvalid || this.isDateInvalid || this.isExpenseIdInvalid) {
      return;
    }

    //TODO: add call to backend + error handling
    this.paymentService.updatePayment(this.payment.id!, this.payment).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Payment updated successfully!',
          life: 3000,
        });
        setTimeout(() => this.router.navigate(['/all-payments']), 1200);
      },
      error: (err) => {
        console.error('Failed to update payment', err);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to update payment.',
        });
      },
    });
  }

  cancelEdit(): void {
    this.router.navigate(['/all-payments']);
  }

  onMenuSelect(label: string): void {
    this.menuService.navigate(label);
  }
}
