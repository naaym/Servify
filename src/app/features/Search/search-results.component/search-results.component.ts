import { CommonModule } from '@angular/common';
import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Header } from '../../../shared/header/header';
import { ProviderCardComponent } from '../components/provider-card.component/provider-card.component';
import {
  ProviderSortBy,
  SearchProviderRequest,
  SearchProviderResult,
} from '../models/relsult-search.model';
import { SearchProvidersService } from '../services/provider-search.service';

@Component({
  selector: 'app-search-results.component',
  imports: [CommonModule, ProviderCardComponent, Header],
  templateUrl: './search-results.component.html',
  styleUrl: './search-results.component.scss',
})
export class SearchResultsComponent implements OnInit, OnDestroy {
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly searchService = inject(SearchProvidersService);
  private queryParamsSub?: Subscription;

  filters: SearchProviderRequest = {
    serviceCategory: '',
    governorate: '',
    page: 0,
    size: 10,
    sortBy: ProviderSortBy.RATING_DESC,
  };

  result?: SearchProviderResult;
  isLoading = false;

  readonly sortOptions: { label: string; value: ProviderSortBy }[] = [
    { label: 'Meilleure note', value: ProviderSortBy.RATING_DESC },
    { label: 'Moins cher', value: ProviderSortBy.PRICE_ASC },
    { label: 'Plus cher', value: ProviderSortBy.PRICE_DESC },
    { label: "Nombre d'avis", value: ProviderSortBy.REVIEWS_DESC },
  ];

  ngOnInit(): void {
    this.queryParamsSub = this.activatedRoute.queryParams.subscribe((params) => {
      this.filters = {
        serviceCategory: params['serviceCategory'] ?? '',
        governorate: params['governorate'] ?? '',
        minPrice: this.parseNumber(params['minPrice']),
        maxPrice: this.parseNumber(params['maxPrice']),
        minRating: this.parseNumber(params['minRating']),
        page: this.parseNumber(params['page'], 0) ?? 0,
        size: this.parseNumber(params['size'], 10) ?? 10,
        sortBy: (params['sortBy'] as ProviderSortBy) || ProviderSortBy.RATING_DESC,
      };
      this.fetchResults();
    });
  }

  ngOnDestroy(): void {
    this.queryParamsSub?.unsubscribe();
  }

  fetchResults(): void {
    if (!this.filters.serviceCategory || !this.filters.governorate) {
      this.result = undefined;
      return;
    }
    this.isLoading = true;
    this.searchService.searchProvider(this.filters).subscribe({
      next: (resp) => {
        this.result = resp;
        this.isLoading = false;
      },
      error: () => {
        this.result = undefined;
        this.isLoading = false;
      },
    });
  }

  updateQueryParams(partial: Partial<SearchProviderRequest>): void {
    const updated: any = {
      ...this.filters,
      ...partial,
    };

    const queryParams: Record<string, any> = {};
    Object.entries(updated).forEach(([key, value]) => {
      if (value !== null && value !== undefined && value !== '') {
        queryParams[key] = value;
      }
    });

    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams,
    });
  }

  onServiceCategoryChange(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.updateQueryParams({ serviceCategory: value, page: 0 });
  }

  onGovernorateChange(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.updateQueryParams({ governorate: value, page: 0 });
  }

  onMinPriceChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.updateQueryParams({ minPrice: this.parseNumber(input.value), page: 0 });
  }

  onMaxPriceChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.updateQueryParams({ maxPrice: this.parseNumber(input.value), page: 0 });
  }

  onRatingChange(rate: string): void {
    this.updateQueryParams({ minRating: this.parseNumber(rate), page: 0 });
  }

  onSortChange(event: Event): void {
    const input = event.target as HTMLSelectElement;
    this.updateQueryParams({ sortBy: input.value as ProviderSortBy });
  }

  resetFilters(): void {
    this.updateQueryParams({
      minPrice: undefined,
      maxPrice: undefined,
      minRating: undefined,
      sortBy: ProviderSortBy.RATING_DESC,
      page: 0,
      size: 10,
    });
  }

  nextPage(): void {
    this.updateQueryParams({ page: (this.filters.page ?? 0) + 1 });
  }

  prevPage(): void {
    const currentPage = this.filters.page ?? 0;
    this.updateQueryParams({ page: Math.max(currentPage - 1, 0) });
  }

  showProviderDetails(id: number): void {
    this.router.navigate(['/search/providers', id]);
  }

  newBooking(id: number): void {
    this.router.navigate(['/providers', id, 'booking']);
  }

  private parseNumber(value: any, fallback?: number): number | undefined {
    if (value === undefined || value === null || value === '') {
      return fallback;
    }
    const parsed = Number(value);
    if (Number.isNaN(parsed)) {
      return fallback;
    }
    return parsed;
  }
}
