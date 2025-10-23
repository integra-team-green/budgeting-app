import {Component, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';
import { ChartModule } from 'primeng/chart';
import {ButtonModule} from 'primeng/button';
import { CardModule } from 'primeng/card';
import {IncomeControllerService} from '../../api';
import {IncomeDTO} from '../../api/model/incomeDTO';

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
  incomes: IncomeDTO[] = [];
  totalIncome = 0;
  incomeByCategory: { category: string; amount: number; }[] = [];
  recentTransactions: any[] = [];
  incomeTips: any[] = [];

  chartData: any;
  chartOptions: any;

  constructor(private incomeService: IncomeControllerService) {}

  ngOnInit() {
    this.loadIncomes();

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
  }

  loadIncomes(): void {
    this.incomeService.getAllIncomes().subscribe({
      next: (data) => {
        this.incomes = data;
        this.totalIncome = data.reduce((sum, inc) => sum + (inc.amount ?? 0), 0);
        const groups: Record<string, number> = {};
        for (const inc of data) {
          const key = inc.source ?? 'Other';
          groups[key] = (groups[key] || 0) + (inc.amount ?? 0);
        }


        this.incomeByCategory = Object.keys(groups).map((key, index) => ({
          category: key,
          amount: groups[key],
        }));

        this.recentTransactions = [...data]
          .sort((a, b) => new Date(b.date ?? '').getTime() - new Date(a.date ?? '').getTime())
          .slice(0, 3);

        this.setupChart();
      },
      error: (err) => console.error('Error loading incomes:', err)
    });
  }

  private setupChart(): void {
    this.chartData = {
      labels: this.incomeByCategory.map(c => c.category),
      datasets: [
        {
          label: 'Income by Category',
          data: this.incomeByCategory.map(c => c.amount),
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
            font: {weight: 500},
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
  deleteTransaction(id?: number) {
    if (!id) return;
    this.incomeService.deleteIncome(id).subscribe(() => {
      this.incomes = this.incomes.filter(t => t.id !== id);
      this.loadIncomes();
    });
  }

}
