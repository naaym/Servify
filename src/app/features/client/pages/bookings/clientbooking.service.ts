import { inject, Injectable } from '@angular/core';
import { BookingResponse } from '../../../booking/models/booking-response.model';
import { BookingService } from '../../../booking/services/booking.service';
import { map } from 'rxjs';
import { StatsBooking } from './statsbooking.model';

@Injectable({ providedIn: 'root' })
export class ClientBookingService {
  bookingService = inject(BookingService);

  getMyBookings(status?: string) {
    return this.bookingService.getClientBookings(status);
  }

  getBookingsById(id: number) {
    return this.bookingService.getBookingById(id);
  }

  getMyStats() {
    return this.getMyBookings().pipe(
      map((bookings: BookingResponse[]): StatsBooking => {
        const baseStats: StatsBooking = {
          totalRequests: bookings.length,
          totalPending: 0,
          totalAccepted: 0,
          totalRejected: 0,
          totalCancelled: 0,
          totalDone: 0,
        };

        bookings.forEach((booking) => {
          switch (booking.status) {
            case 'PENDING':
              baseStats.totalPending += 1;
              break;
            case 'ACCEPTED':
              baseStats.totalAccepted += 1;
              break;
            case 'REJECTED':
              baseStats.totalRejected += 1;
              break;
            case 'CANCELLED':
              baseStats.totalCancelled += 1;
              break;
          }
        });

        return baseStats;
      })
    );
  }
}
