import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {CurrencyClientRequest} from "../models/currency-client-request.model";
import {CurrencyService} from "../services/currency.service";

@Component({
  selector: 'app-currency-form',
  templateUrl: './currency-form.component.html',
  styleUrls: ['./currency-form.component.css']
})
export class CurrencyFormComponent implements OnInit {

  options: String[] = [];

  form = new FormGroup({
    "name": new FormControl("", Validators.required),
    "currency": new FormControl("", Validators.required),
  });
  showResult = false;
  rate = '';

  constructor(private currencyService: CurrencyService) {
  }


  ngOnInit(): void {
    this.currencyService.getCurrencyCodes().subscribe(data => this.options = data);
  }

  onSubmit() {
    if (this.form.valid) {
      let body = this.form.value as CurrencyClientRequest;
      this.currencyService.getCurrencyRate(body)
        .subscribe(r => {
          this.rate = "1 " + body.currency + " = " + r.value + " PLN";
          this.showResult = true;
        })
    }
  }

}
