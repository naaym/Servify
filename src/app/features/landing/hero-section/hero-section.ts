import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { LucideAngularModule, SearchIcon } from 'lucide-angular';
import { OptionItem } from '../../../shared/models/option-item';
import { SearchStateService } from '../../../shared/services/serachstate.service';
import { ServiceModal } from '../modals/service.modal/service.modal';
import { CityModal } from '../modals/city.modal/city.modal';

@Component({
  selector: 'app-hero-section',
  imports: [LucideAngularModule, ServiceModal, CityModal],
  templateUrl: './hero-section.html',
  styleUrl: './hero-section.scss',
})
export class HeroSection {
  private readonly router = inject(Router);
  private readonly searchStateService = inject(SearchStateService);
  icon = SearchIcon;

  isServiceModalOpen = false;
  isCityModalOpen = false;
  selectedService: OptionItem | null = null;

  backToServiceModal() {
    this.toggleCityModal(false);
    this.toggleServiceModal(true);
  }

  toggleServiceModal(open: boolean) {
    this.isServiceModalOpen = open;
  }
  toggleCityModal(open: boolean) {
    this.isCityModalOpen = open;
  }

  onServiceChosen(service: OptionItem) {
    this.selectedService = service;
    this.toggleServiceModal(false);
    this.toggleCityModal(true);
  }

  onCityChosen(city: OptionItem) {
    const queryParams: Record<string, string | number> = {};

    if (this.selectedService?.id) {
      queryParams['serviceId'] = this.selectedService.id;
    }
    if (city?.id) {
      queryParams['governorateId'] = city.id;
    }

    this.searchStateService.setSearchContext(
      city?.name ?? '',
      this.selectedService?.name ?? '',
      city?.id,
      this.selectedService?.id
    );

    this.router.navigate(['/search'], {
      queryParams,
    });
  }
}
