import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProviderCardComponent } from '../components/provider-card.component/provider-card.component';
import { ActivatedRoute, Router } from '@angular/router';
import { SearchProviderService } from '../services/provider-search.service';
import { SearchProviderRequest } from '../models/relsult-search.model';
import { Provider } from '../models/provider.model';
import { Header } from '../../../shared/header/header';

@Component({
  selector: 'app-search-results.component',
  standalone: true,
  imports: [CommonModule, ProviderCardComponent,Header],
  templateUrl: './search-results.component.html',
  styleUrl: './search-results.component.scss',
})
export class SearchResultsComponent implements OnInit {
  activatedRoute = inject(ActivatedRoute);
  searchService = inject(SearchProviderService);
  router = inject(Router);

  serviceCategory = '';
  city = '';
  delegation = '';
  minPrice?: number;
  maxPrice?: number;
  minRating?: number;
  sort: SearchProviderRequest['sortBy'] = 'RATING_DESC';

  providers: Provider[] = [];
  total = 0;

  ngOnInit(): void {
    this.serviceCategory =
      this.activatedRoute.snapshot.queryParamMap.get('service') || '';
    this.city = this.activatedRoute.snapshot.queryParamMap.get('city') || '';
    this.delegation =
      this.activatedRoute.snapshot.queryParamMap.get('delegation') || '';
    this.fetchResults();
  }

  fetchResults() {
    const request: SearchProviderRequest = {
      serviceCategory: this.serviceCategory,
      governorate: this.city,
      delegation: this.delegation,
      minPrice: this.minPrice,
      maxPrice: this.maxPrice,
      minRating: this.minRating,
      sortBy: this.sort,
    };

    this.searchService.searchProvider(request).subscribe((resp) => {
      this.providers = resp.providers;
      this.total = resp.total;
    });
  }

  showProviderDetails(id: number) {
    this.router.navigate(['/search/providers', id]);
  }

  newBooking(id: number) {
    this.router.navigate(['/providers', id, 'booking']);
  }

  onMinPriceChange(event: Event) {
    const input = event.target as HTMLInputElement;
    this.minPrice = input.value ? +input.value : undefined;
    this.fetchResults();
  }

  onMaxPriceChange(event: Event) {
    const input = event.target as HTMLInputElement;
    this.maxPrice = input.value ? +input.value : undefined;
    this.fetchResults();
  }

  onRatingChange(rate: string) {
    this.minRating = rate ? +rate : undefined;
    this.fetchResults();
  }

  onSortChange(event: Event) {
    const input = event.target as HTMLSelectElement;
    const value = input.value as SearchProviderRequest['sortBy'];
    this.sort = value;
    this.fetchResults();
  }

  resetFilters() {
    this.sort = 'RATING_DESC';
    this.minPrice = undefined;
    this.maxPrice = undefined;
    this.minRating = undefined;
    this.fetchResults();
  }























}


