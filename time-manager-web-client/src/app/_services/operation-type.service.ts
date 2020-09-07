import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {OperationType} from "../_models/operation-type";
import {BehaviorSubject, Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {map, skipWhile} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class OperationTypeService {

  private operationTypes: BehaviorSubject<OperationType[]> = new BehaviorSubject<OperationType[]>([]);
  private operationTypes$: Observable<OperationType[]> = this.operationTypes.asObservable();
  private pending = false;

  constructor(private http: HttpClient) {
  }


  getOperationTypes(): Observable<OperationType[]> {
    if (this.operationTypes.value.length === 0 && !this.pending) {
      this.pending = true;
      this.http.get<OperationType[]>(`${environment.apiUrl}/operation`).subscribe(x => {
        this.operationTypes.next(x);
        this.pending = false;
      });
    }
    return this.operationTypes$;
  }

  getOperationType(idOperationType: number): Observable<OperationType> {
    this.getOperationTypes();
    return this.operationTypes$.pipe(skipWhile(x => x.length === 0)).pipe(map(x => {
      return x.find(s => s.idOperationType === idOperationType);
    }));

  }

}
