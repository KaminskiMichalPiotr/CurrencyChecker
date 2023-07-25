import {NgModule} from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {CurrencyFormComponent} from "./currency-form/currency-form.component";
import {CurrencyRequestHistoryComponent} from "./currency-request-history/currency-request-history.component";


const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: CurrencyFormComponent },
  { path: 'history', component: CurrencyRequestHistoryComponent },
  { path: '**', redirectTo: '/home' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
