import { inject, Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { Http } from '../../../core/api/http';
import { API_ENDPOINTS } from '../../../core/api/endpoints';
import {
  ProviderSortBy,
  SearchProviderRequest,
  SearchProviderResult,
} from '../models/relsult-search.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class SearchProvidersService {
  http = inject(Http);

  searchProvider(params: SearchProviderRequest): Observable<SearchProviderResult> {
    const queryParams = this.buildQueryParams(params);
    return this.http.get<SearchProviderResult>(
      API_ENDPOINTS.PROVIDER.SEARCH,
      queryParams
    );

  }
  buildQueryParams(params:SearchProviderRequest){
    let httpParams=new HttpParams();
    const payload: SearchProviderRequest = {
      ...params,
      sortBy: params.sortBy ?? ProviderSortBy.RATING_DESC,
    };
    Object.entries(payload).forEach(([key, value]) => {
      if (value !== null && value !== undefined && value !== '') {
        httpParams = httpParams.set(key, value as any);
      }
    });
    return httpParams;
  }
}
