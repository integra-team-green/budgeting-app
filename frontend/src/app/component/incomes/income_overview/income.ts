import {Component, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';
import { ChartModule } from 'primeng/chart';
import {ButtonModule} from 'primeng/button';
import { CardModule } from 'primeng/card';
import {IncomeControllerService} from '../../../api';
import {IncomeDTO} from '../../../api/model/incomeDTO';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-income',
  imports: [
    RouterLink,
    ChartModule,
    ButtonModule,
    CardModule,
    CommonModule,
  ],
  templateUrl: './income.html',
  styleUrl: './income.css'
})
export class IncomeComponent implements OnInit {
  incomes: IncomeDTO[] = [];
  totalIncome = 0;
  salaryIncome = 0;
  otherIncome = 0;
  freelanceIncome = 0;
  recentTransactions: any[] = [];


  chartData: any;
  chartOptions: any;

  constructor(private incomeService: IncomeControllerService) {}

  ngOnInit() {
    this.loadIncomes();
  }

  loadIncomes(): void {
    this.incomeService.getAllIncomes().subscribe({
      next: (data:IncomeDTO[]) => {
        this.incomes = data;
        this.totalIncome = data.reduce(
          (sum: number, inc: IncomeDTO) => sum + (inc.amount ?? 0),
          0
        );

        this.salaryIncome = data
          .filter(inc => inc.source?.toLowerCase() === 'salary')
          .reduce((sum, inc) => sum + (inc.amount ?? 0), 0);

        this.freelanceIncome = data
          .filter(inc => inc.source?.toLowerCase() === 'freelance')
          .reduce((sum, inc) => sum + (inc.amount ?? 0), 0);

        this.otherIncome = data
          .filter(inc => inc.source?.toLowerCase() !== 'salary' && inc.source?.toLowerCase() !== 'freelance')
          .reduce((sum, inc) => sum + (inc.amount ?? 0), 0);


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
      labels: ['Salary', 'Freelance', 'Other'],
      datasets: [
        {
          label: 'Income by Category',
          data: [this.salaryIncome, this.freelanceIncome, this.otherIncome],
          backgroundColor: ['#a78bfa', '#af1301', '#ec4899'],
          borderRadius: 6,
        },
      ],
    };

    this.chartOptions = {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: true,
        },
      },
      scales: {
        x: {
          ticks: {
            color: '#000000',
            font: { weight: 500 },
          },
          grid: {
            color: 'transparent',
            drawBorder: false,
          },
        },
        y: {
          ticks: {
            color: '#000000',
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
    if (confirm('Are you sure you want to delete this income?')){
      if (!id) return;
      this.incomeService.deleteIncome(id).subscribe(() => {
        this.incomes = this.incomes.filter(t => t.id !== id);
        this.loadIncomes();
      });
  }
  }

}
