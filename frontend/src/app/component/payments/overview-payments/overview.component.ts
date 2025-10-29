import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {CommonModule} from '@angular/common';
import {CardModule} from 'primeng/card';
import {ButtonModule} from 'primeng/button';
import {ChartModule} from 'primeng/chart';
import {SidebarPaymentComponent} from '../sidebar/sidebar.component';
import {MenuService} from '../services/menu.service';
import {PaymentControllerService, PaymentDTO} from '../../../api';
import {ToastModule} from 'primeng/toast';
import {ConfirmationService, MessageService} from 'primeng/api';
import {ConfirmDialog} from 'primeng/confirmdialog';

@Component({
  selector: 'app-overview',
  standalone: true,
  imports: [CommonModule, CardModule, ButtonModule, ChartModule, SidebarPaymentComponent, ToastModule, ConfirmDialog],
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.css'],
  providers: [ConfirmationService, MessageService],
})
export class OverviewComponent implements OnInit {
  loading = false;
  transactions: PaymentDTO[] = [];
  paymentChartData: any;
  chartOptions = {
    plugins: { legend: { display: true } },
    scales: { y: { beginAtZero: true } },
  };


  totalPayments = 0;
  paidPayments = 0;
  pendingPayments = 0;
  failedPayments = 0;

  tips = [
    { title: 'Pay bills on time', note: '- Avoid late fees' },
    { title: 'Use cards wisely', note: '- Track your spending' },
    { title: 'Set up auto-transfers', note: '- Save time' },
    { title: 'Check statements', note: '- Spot errors quickly' },
    { title: 'Review subscriptions', note: '- Cancel unused services' },
  ];

  constructor(
    private router: Router,
    private menuService: MenuService,
    private paymentService: PaymentControllerService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.loadPayments();
  }

  loadPayments(): void {
    this.loading = true;
    this.paymentService
      .getAllPayments()
      .subscribe({
        next: (data: any) => {
          const handlePayments = (payments: any[]) => {
            const sorted = payments.sort(
              (a, b) =>
                new Date(b.paymentDate as any).getTime() -
                new Date(a.paymentDate as any).getTime()
            );

            this.transactions = sorted.slice(0, 3);
            this.calculateTotals(payments);
            this.updateChartMonthly(payments);
            this.loading = false;
          };

          if (data instanceof Blob) {
            data.text().then((text) => {
              try {
                const json = JSON.parse(text);
                handlePayments(json);
              } catch (e) {
                console.error('Failed to parse Blob response', e);
                this.loading = false;
              }
            });
          } else if (Array.isArray(data)) {
            handlePayments(data);
          } else {
            console.warn('Unexpected API response format:', data);
            this.loading = false;
          }
        },
        error: (err) => {
          console.error('Failed to load payments', err);
          this.loading = false;
        },
      });
  }

  calculateTotals(payments: PaymentDTO[]): void {
    this.totalPayments = payments.length;
    this.paidPayments = payments.filter((p) => p.status === 'PAID').length;
    this.pendingPayments = payments.filter((p) => p.status === 'PENDING').length;
    this.failedPayments = payments.filter((p) => p.status === 'FAILED').length;
  }

  updateChartMonthly(payments: PaymentDTO[]): void {
    if (!payments || payments.length === 0) {
      this.paymentChartData = {
        labels: [],
        datasets: [{ label: 'Payments per Month', data: [], backgroundColor: '#A16EFF' }],
      };
      return;
    }

    const monthlyTotals: Record<string, number> = {};
    payments.forEach((p) => {
      if (!p.paymentDate) return;
      const date = new Date(p.paymentDate);
      const monthName = date.toLocaleString('en-US', { month: 'short' });
      monthlyTotals[monthName] = (monthlyTotals[monthName] || 0) + 1;
    });

    const orderedMonths = [
      'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
      'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'
    ].filter((m) => monthlyTotals[m]);

    const values = orderedMonths.map((m) => monthlyTotals[m]);

    this.paymentChartData = {
      labels: orderedMonths,
      datasets: [
        {
          label: 'Payments per Month',
          data: values,
          backgroundColor: '#A16EFF',
        },
      ],
    };
  }

  goToAllPayments(): void {
    this.router.navigate(['/all-payments']);
  }

  onMenuSelect(label: string): void {
    this.menuService.navigate(label);
  }

  editPayment(index: number): void {
    const payment = this.transactions[index];
    this.router.navigate(['/edit-payments'], { state: { payment, index } });
  }

  confirmDelete(index: number): void {
    const payment = this.transactions[index];
    if (!payment?.id) return;

    this.confirmationService.confirm({
      message: `Are you sure you want to delete payment "${payment.name}"?`,
      header: 'Confirm Deletion',
      icon: 'pi pi-exclamation-triangle',

      acceptLabel: 'Yes',
      rejectLabel: 'Cancel',
      acceptButtonStyleClass: 'p-button-success p-button-sm',
      rejectButtonStyleClass: 'p-button-text p-button-sm text-purple-700 font-semibold',

      accept: () => {
        this.paymentService.deletePayment(payment.id!).subscribe({
          next: () => {
            this.transactions.splice(index, 1);
            this.loadPayments();
            this.messageService.add({
              severity: 'success',
              summary: 'Deleted',
              detail: `Payment "${payment.name}" deleted successfully.`,
              life: 2500
            });
          },
          error: (err) => {
            console.error('Failed to delete payment', err);
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Failed to delete payment. Please try again.',
            });
          }
        });
      }
    });
  }
}
