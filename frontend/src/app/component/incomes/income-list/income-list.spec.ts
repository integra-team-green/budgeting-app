import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IncomeListComponent } from './income-list';

describe('IncomeList', () => {
  let component: IncomeListComponent;
  let fixture: ComponentFixture<IncomeListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IncomeListComponent],
    })
    .compileComponents();

    fixture = TestBed.createComponent(IncomeListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
