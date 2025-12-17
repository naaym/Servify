import { Component, EventEmitter, Input, OnChanges, OnDestroy, Output, SimpleChanges, inject } from '@angular/core';
import { finalize, Subject, takeUntil } from 'rxjs';
import { SearchOptionsService } from '../../../Search/services/search-options.service';
import { OptionItem } from '../../../../shared/models/option-item';

@Component({
  selector: 'app-city',
  imports: [],
  templateUrl: './city.modal.html',
  styleUrl: './city.modal.scss',
})
export class CityModal implements OnChanges, OnDestroy {
   @Input({required:true}) open:boolean=false;
   @Input({required:true}) selectedService!:OptionItem|null;
  @Output() close = new EventEmitter();
  @Output() citySelected = new EventEmitter<OptionItem>();
  @Output() retour = new EventEmitter();

   governorates: OptionItem[] = [];
   isLoading = false;
   private readonly destroy$ = new Subject<void>();
   private readonly searchOptionsService = inject(SearchOptionsService);

   ngOnChanges(changes: SimpleChanges): void {
     if ((changes['open']?.currentValue === true) || (changes['selectedService'] && this.open)) {
       this.loadGovernorates();
     }
   }

   ngOnDestroy(): void {
     this.destroy$.next();
     this.destroy$.complete();
   }

   private loadGovernorates(): void {
     this.isLoading = true;
     this.searchOptionsService
       .getAvailableGovernorates(this.selectedService?.id ?? undefined)
       .pipe(
         takeUntil(this.destroy$),
         finalize(() => (this.isLoading = false))
       )
       .subscribe({
         next: (governorates) => (this.governorates = governorates ?? []),
         error: () => (this.governorates = []),
       });
   }

}
