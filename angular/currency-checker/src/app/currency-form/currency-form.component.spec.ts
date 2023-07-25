import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CurrencyFormComponent} from './currency-form.component';
import {CurrencyService} from "../services/currency.service";
import {of} from "rxjs";
import {CurrencyRate} from "../models/currency-rate.model";
import {CurrencyClientRequest} from "../models/currency-client-request.model";
import {ReactiveFormsModule} from "@angular/forms";
import {MatFormFieldModule} from "@angular/material/form-field";

describe('CurrencyFormComponent', () => {
  let component: CurrencyFormComponent;
  let fixture: ComponentFixture<CurrencyFormComponent>;
  let mockCurrencyService: jasmine.SpyObj<CurrencyService>;

  beforeEach(async () => {
    const currencyServiceSpy = jasmine.createSpyObj('CurrencyService', ['getCurrencyCodes', 'getCurrencyRate']);

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, MatFormFieldModule],
      declarations: [CurrencyFormComponent],
      providers: [
        {provide: CurrencyService, useValue: currencyServiceSpy}
      ]
    }).compileComponents();

    mockCurrencyService = TestBed.inject(CurrencyService) as jasmine.SpyObj<CurrencyService>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CurrencyFormComponent);
    component = fixture.componentInstance;
  });

  afterEach(() => {
    mockCurrencyService.getCurrencyCodes.calls.reset();
    mockCurrencyService.getCurrencyRate.calls.reset();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch currency codes on initialization', () => {
    const mockCurrencyCodes: string[] = ['USD', 'EUR', 'GBP'];

    mockCurrencyService.getCurrencyCodes.and.returnValue(of(mockCurrencyCodes));

    component.ngOnInit();

    expect(component.options).toEqual(mockCurrencyCodes);
  });

  it('should show the rate when form is submitted with valid input', () => {
    const mockCurrencyRate: CurrencyRate = {value: 1.2345};
    const mockFormValue: CurrencyClientRequest = {name: 'Joe Denver', currency: 'USD'};

    component.form.setValue(mockFormValue);
    component.rate = '';
    component.showResult = false;

    mockCurrencyService.getCurrencyRate.and.returnValue(of(mockCurrencyRate));

    component.onSubmit();

    expect(component.rate).toEqual('1 USD = 1.2345 PLN');
    expect(component.showResult).toBe(true);
  });

  it('should not show the rate when form is submitted with invalid input', () => {
    const mockFormValue: CurrencyClientRequest = {name: '', currency: ''};

    component.form.setValue(mockFormValue);
    component.rate = '';
    component.showResult = false;

    component.onSubmit();

    expect(mockCurrencyService.getCurrencyRate).not.toHaveBeenCalled();
    expect(component.rate).toEqual('');
    expect(component.showResult).toBe(false);
  });
});
