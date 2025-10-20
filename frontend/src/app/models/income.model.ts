export interface Income {
  id: number;
  source: string;
  amount: number;
  date: string;
  description?: string;
  frequency: 'one time' | 'monthly' | 'yearly';
  endDate?: string;
}
