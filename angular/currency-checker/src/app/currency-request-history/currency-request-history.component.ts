import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CurrencyRequest} from "../models/currency-request.model";
import {CurrencyService} from "../services/currency.service";
import {MatPaginator} from "@angular/material/paginator";
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-currency-request-history',
  templateUrl: './currency-request-history.component.html',
  styleUrls: ['./currency-request-history.component.css']
})
export class CurrencyRequestHistoryComponent implements OnInit {

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  dataSource!: MatTableDataSource<CurrencyRequest>;
  displayedColumns: string[] = ['name', 'currency', 'date', 'value'];

  constructor(private http: HttpClient, private currencyService: CurrencyService) {
  }

  ngOnInit() {
    this.currencyService.getCurrencyRequests().subscribe(data => {
      this.dataSource = new MatTableDataSource<CurrencyRequest>(data);
      this.dataSource.paginator = this.paginator;
    })
  }

}
