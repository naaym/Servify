import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../../../core/api/endpoints';
import { OptionItem } from '../../../shared/models/option-item';

@Injectable({ providedIn: 'root' })
export class SearchOptionsService {
  private http = inject(HttpClient);

  getAvailableServices(): Observable<OptionItem[]> {
    return this.http.get<OptionItem[]>(`${API_ENDPOINTS.BASE}/${API_ENDPOINTS.SEARCH.OPTIONS.SERVICES}`);
  }

  getAvailableGovernorates(serviceId?: number): Observable<OptionItem[]> {
    const params = serviceId ? new HttpParams().set('serviceId', serviceId) : undefined;
    return this.http.get<OptionItem[]>(`${API_ENDPOINTS.BASE}/${API_ENDPOINTS.SEARCH.OPTIONS.GOVERNORATES}`, {
      params,
    });
  }
}
