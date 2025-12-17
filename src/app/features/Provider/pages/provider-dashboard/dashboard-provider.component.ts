import { CommonModule, DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ProviderBookingService } from '../../services/provider-booking.service';
import { ProviderBookingResponse } from '../../models/provider-booking.model';
import { Status } from '../../../booking/models/status.model';
import { ClientBookingDetails } from '../../client/pages/bookings/clientbookingdetail.model';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, DatePipe],
  templateUrl: './dashboard-provider.html',
  styleUrl: './dashboard.scss',
})
export class ProviderDashboard implements OnInit {
  bookings: ProviderBookingResponse[] = [];
  errorMessage = '';
  loading = false;
  detailsLoading = false;
  selectedBooking: ClientBookingDetails | null = null;

  constructor(private readonly bookingService: ProviderBookingService) {}

  ngOnInit(): void {
    this.loadBookings();
  }

  loadBookings() {
    this.loading = true;
    this.bookingService.getProviderBookings().subscribe({
      next: (bookings) => {
        this.bookings = bookings;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err.message;
        this.loading = false;
      },
    });
  }

  onUpdateStatus(bookingId: number, status: Status) {
    this.bookingService.updateStatus(bookingId, status).subscribe({
      next: (updated) => {
        this.bookings = this.bookings.map((booking) =>
          booking.bookingId === bookingId
            ? { ...booking, status: updated.status as Status }
            : booking
        );
        if (this.selectedBooking?.bookingId === bookingId) {
          this.selectedBooking = { ...this.selectedBooking, status: updated.status };
        }
      },
      error: (err) => (this.errorMessage = err.message),
    });
  }

  viewDetails(bookingId: number) {
    this.detailsLoading = true;
    this.errorMessage = '';
    this.bookingService.getBookingDetails(bookingId).subscribe({
      next: (details) => {
        this.selectedBooking = details;
        this.detailsLoading = false;
      },
      error: (err) => {
        this.errorMessage = err.message;
        this.detailsLoading = false;
      },
    });
  }

  get pendingCount() {
    return this.bookings.filter((b) => b.status === 'PENDING').length;
  }

  get acceptedCount() {
    return this.bookings.filter((b) => b.status === 'ACCEPTED').length;
  }

  get completedCount() {
    return this.bookings.filter((b) => b.status === 'DONE').length;
  }
}
