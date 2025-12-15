export enum ProviderSortBy {
  RATING_DESC = 'RATING_DESC',
  PRICE_ASC = 'PRICE_ASC',
  PRICE_DESC = 'PRICE_DESC',
  REVIEWS_DESC = 'REVIEWS_DESC',
}

export interface ProviderSummary {
  id: number;
  name: string;
  serviceCategory: string;
  governorate: string;
  delegation: string;
  hourlyRate: number;
  averageRating: number;
  reviewsCount: number;
  status: string;
}

export interface SearchProviderResult {
  content: ProviderSummary[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface SearchProviderRequest {
  serviceCategory: string;
  governorate: string;
  minPrice?: number;
  maxPrice?: number;
  minRating?: number;
  sortBy?: ProviderSortBy;
  page: number;
  size: number;
}
