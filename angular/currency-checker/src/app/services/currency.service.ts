import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {CurrencyRequest} from "../models/currency-request.model";
import {CurrencyClientRequest} from "../models/currency-client-request.model";
import {CurrencyRate} from "../models/currency-rate.model";

@Injectable({
  providedIn: 'root'
})
export class CurrencyService {

  apiURL = 'http://localhost:8080/currencies';

  constructor(private http: HttpClient) { }

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    }),
  };

  getCurrencyCodes(): Observable<string[]>{
    return this.http.get<string[]>(this.apiURL + "/codes");
  }

  getCurrencyRequests(): Observable<CurrencyRequest[]>{
    return this.http.get<CurrencyRequest[]>(this.apiURL + "/requests")
  }

  getCurrencyRate(body: CurrencyClientRequest): Observable<CurrencyRate>{
    return this.http.post<CurrencyRate>(this.apiURL + "/get-current-currency-value-command", body, this.httpOptions);
  }


}
