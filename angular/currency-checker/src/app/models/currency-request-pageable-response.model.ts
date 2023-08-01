import {CurrencyRequest} from "./currency-request.model";

export interface CurrencyRequestResponse {
  content: CurrencyRequest[];
  pageable: any;
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  sort: any;
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}
