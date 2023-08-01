import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CurrencyRequest} from "../models/currency-request.model";
import {CurrencyService} from "../services/currency.service";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-currency-request-history',
  templateUrl: './currency-request-history.component.html',
  styleUrls: ['./currency-request-history.component.css']
})
export class CurrencyRequestHistoryComponent implements OnInit {

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  pageEvent: PageEvent | undefined;

  dataSource!: MatTableDataSource<CurrencyRequest>;
  displayedColumns: string[] = ['name', 'currency', 'date', 'value'];

  defaultPageSize = 5;

  constructor(private http: HttpClient, private currencyService: CurrencyService) {
  }

  ngOnInit() {
    this.currencyService.getCurrencyRequestsByPage(0, this.defaultPageSize).subscribe(data => {
      this.dataSource = new MatTableDataSource<CurrencyRequest>(data.content.reverse());
      this.paginator.length = data.totalElements;
    })
  }

  handlePageEvent(e: PageEvent) {
    console.log(e.pageSize);
    this.currencyService.getCurrencyRequestsByPage(e.pageIndex, e.pageSize).subscribe(data => {
      this.dataSource = new MatTableDataSource<CurrencyRequest>(data.content.reverse());
    })
  }




}
