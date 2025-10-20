import {Component, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';
import { ChartModule } from 'primeng/chart';
import {ButtonModule} from 'primeng/button';
import { CardModule } from 'primeng/card';
@Component({
  selector: 'app-income',
  imports: [
    RouterLink,
    ChartModule,
    ButtonModule,
    CardModule,
  ],
  templateUrl: './income.html',
  styleUrl: './income.css'
})
export class IncomeComponent implements OnInit {
  totalIncome = 0;
  incomeByCategory: { category: string; amount: number; color: string }[] = [];
  recentTransactions: any[] = [];
  incomeTips: any[] = [];

  chartData: any;
  chartOptions: any;

  ngOnInit() {
    // 🔹 Demo data
    this.incomeByCategory = [
      { category: 'Salary', amount: 3200, color: '#a78bfa' },
      { category: 'Freelance', amount: 1400, color: '#c084fc' },
      { category: 'Other', amount: 600, color: '#f472b6' },
    ];

    this.totalIncome = this.incomeByCategory.reduce((sum, c) => sum + c.amount, 0);

    this.recentTransactions = [
      { id: 1, source: 'Company Salary', timestamp: 'Oct 3, 2025', icon: 'pi pi-briefcase' },
      { id: 2, source: 'Freelance Project', timestamp: 'Oct 10, 2025', icon: 'pi pi-desktop' },
      { id: 3, source: 'Gift', timestamp: 'Oct 12, 2025', icon: 'pi pi-inbox' },
    ];

    this.incomeTips = [
      {
        icon: 'pi pi-chart-line',
        title: 'Track Regularly',
        description: 'Review your income sources weekly to identify growth opportunities.'
      },
      {
        icon: 'pi pi-wallet',
        title: 'Diversify Income',
        description: 'Explore freelance or side gigs to increase your total income.'
      },
      {
        icon: 'pi pi-calendar',
        title: 'Plan Ahead',
        description: 'Set reminders for recurring income and financial goals.'
      }
    ];

    // 🔹 Setup chart
    this.chartData = {
      labels: this.incomeByCategory.map(c => c.category),
      datasets: [
        {
          label: 'Income by Category',
          data: this.incomeByCategory.map(c => c.amount),
          backgroundColor: this.incomeByCategory.map(c => c.color),
          borderRadius: 6,
        },
      ],
    };

    this.chartOptions = {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: false,
        },
      },
      scales: {
        x: {
          ticks: {
            color: '#6b7280',
            font: { weight: 500 },
          },
          grid: {
            color: 'transparent',
            drawBorder: false,
          },
        },
        y: {
          ticks: {
            color: '#6b7280',
            stepSize: 1000,
          },
          grid: {
            color: '#e5e7eb',
            drawBorder: false,
          },
        },
      },
    };
  }

  // 🔹 Helpers
  getSalaryAmount(): number {
    return this.getAmountByCategory('Salary');
  }

  getFreelanceAmount(): number {
    return this.getAmountByCategory('Freelance');
  }

  getOtherAmount(): number {
    return this.getAmountByCategory('Other');
  }

  private getAmountByCategory(category: string): number {
    const item = this.incomeByCategory.find(c => c.category === category);
    return item ? item.amount : 0;
  }

  // 🔹 Actions
  editTransaction(transaction: any) {
    alert(`Editing transaction: ${transaction.source}`);
  }

  deleteTransaction(id: number) {
    this.recentTransactions = this.recentTransactions.filter(t => t.id !== id);
    alert('Transaction deleted');
  }

}
