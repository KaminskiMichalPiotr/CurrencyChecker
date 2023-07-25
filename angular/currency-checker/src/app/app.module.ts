import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatBadgeModule} from "@angular/material/badge";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {MatToolbarModule} from "@angular/material/toolbar";
import {CurrencyFormComponent} from './currency-form/currency-form.component';
import {CurrencyRequestHistoryComponent} from './currency-request-history/currency-request-history.component';
import {MatTableModule} from "@angular/material/table";
import {HttpClientModule} from "@angular/common/http";
import {AppRoutingModule} from './app-routing.module';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import {CurrencyService} from "./services/currency.service";
import {MatPaginatorModule} from "@angular/material/paginator";

@NgModule({
  declarations: [
    AppComponent,
    CurrencyFormComponent,
    CurrencyRequestHistoryComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatBadgeModule,
    MatSlideToggleModule,
    MatToolbarModule,
    MatTableModule,
    HttpClientModule,
    AppRoutingModule,
    MatFormFieldModule,
    MatSelectModule,
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatPaginatorModule
  ],
  providers: [CurrencyService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
