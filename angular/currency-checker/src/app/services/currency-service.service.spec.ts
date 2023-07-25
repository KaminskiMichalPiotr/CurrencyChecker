import {TestBed} from '@angular/core/testing';

import {CurrencyService} from './currency.service';
import {CurrencyClientRequest} from "../models/currency-client-request.model";
import {CurrencyRate} from "../models/currency-rate.model";
import {CurrencyRequest} from "../models/currency-request.model";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";

describe('CurrencyService', () => {
  let service: CurrencyService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CurrencyService]
    });
    service = TestBed.inject(CurrencyService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get currency codes', () => {
    const mockCurrencyCodes: string[] = ['USD', 'EUR', 'GBP'];

    service.getCurrencyCodes().subscribe((currencyCodes) => {
      expect(Array.isArray(currencyCodes)).toBeTrue();
    });

    const req = httpTestingController.expectOne('http://localhost:8080/currencies/codes');
    expect(req.request.method).toBe('GET');

    req.flush(mockCurrencyCodes);
  });

  it('should get currency requests', () => {
    const mockCurrencyRequests: CurrencyRequest[] = [
      {name: 'Joe Denver', currency: 'USD', date: new Date(), value: 1.2345},
      {name: 'Alice Powell', currency: 'EUR', date: new Date(), value: 0.9876}
    ];

    service.getCurrencyRequests().subscribe((currencyRequests: CurrencyRequest[]) => {
      expect(currencyRequests).toEqual(mockCurrencyRequests);
    });

    const req = httpTestingController.expectOne('http://localhost:8080/currencies/requests');
    expect(req.request.method).toBe('GET');

    req.flush(mockCurrencyRequests);

  });

  it('should get currency rate', () => {
    const mockClientRequest: CurrencyClientRequest = {name: 'Joe Doe', currency: 'USD'};
    const mockCurrencyRate: CurrencyRate = {value: 1.2345};

    service.getCurrencyRate(mockClientRequest).subscribe((currencyRate: CurrencyRate) => {
      expect(currencyRate).toEqual(mockCurrencyRate);
      console.log(currencyRate);
    });

    const req = httpTestingController.expectOne('http://localhost:8080/currencies/get-current-currency-value-command');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockClientRequest);

    req.flush(mockCurrencyRate);
  });
});
