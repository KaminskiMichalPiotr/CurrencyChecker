import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CurrencyRequestHistoryComponent} from './currency-request-history.component';
import {CurrencyService} from "../services/currency.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {MatTableModule} from "@angular/material/table";
import {MatPaginatorModule} from "@angular/material/paginator";
import {CurrencyRequest} from "../models/currency-request.model";
import {of} from "rxjs";

describe('CurrencyRequestHistoryComponent', () => {
  let component: CurrencyRequestHistoryComponent;
  let fixture: ComponentFixture<CurrencyRequestHistoryComponent>;
  let mockCurrencyService: jasmine.SpyObj<CurrencyService>;

  beforeEach(async () => {
    const currencyServiceSpy = jasmine.createSpyObj('CurrencyService', ['getCurrencyRequests']);

    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MatTableModule, MatPaginatorModule],
      declarations: [CurrencyRequestHistoryComponent],
      providers: [
        {provide: CurrencyService, useValue: currencyServiceSpy}
      ]
    }).compileComponents();

    mockCurrencyService = TestBed.inject(CurrencyService) as jasmine.SpyObj<CurrencyService>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CurrencyRequestHistoryComponent);
    component = fixture.componentInstance;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch currency requests on initialization and set the data source and paginator', () => {
    const mockCurrencyRequests: CurrencyRequest[] = [
      {name: 'Currency 1', currency: 'USD', date: new Date(), value: 1.2345},
      {name: 'Currency 2', currency: 'EUR', date: new Date(), value: 0.9876}
    ];

    mockCurrencyService.getCurrencyRequests.and.returnValue(of(mockCurrencyRequests));

    component.ngOnInit();

    expect(component.dataSource).toBeDefined();
    expect(component.dataSource.data).toEqual(mockCurrencyRequests);
  });
});
