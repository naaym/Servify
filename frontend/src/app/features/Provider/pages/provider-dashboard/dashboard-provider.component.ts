import { CommonModule, DatePipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { ProviderBookingService } from '../../services/provider-booking.service';
import { ProviderBookingResponse } from '../../models/provider-booking.model';
import { Status } from '../../../booking/models/status.model';
import { AuthService } from '../../../auth/services/auth.service';
import { ChatService } from '../../../booking/services/chat.service';
import { ChatMessage } from '../../../booking/models/chat-message.model';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, DatePipe, RouterModule],
  templateUrl: './dashboard-provider.html',
  styleUrl: './dashboard.scss',
})
export class ProviderDashboard implements OnInit {
     private readonly authService = inject(AuthService);
    private readonly router = inject(Router);
    private readonly chatService = inject(ChatService);
  bookings: ProviderBookingResponse[] = [];
  errorMessage = '';
  loading = false;
  recentMessages: ChatMessage[] = [];
  messagesError = '';

  constructor(private readonly bookingService: ProviderBookingService) {}

  ngOnInit(): void {
    this.loadBookings();
    this.loadRecentMessages();
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

  loadRecentMessages() {
    this.chatService.getRecentMessages().subscribe({
      next: (messages) => (this.recentMessages = messages),
      error: (err) => (this.messagesError = err.message),
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
      },
      error: (err) => (this.errorMessage = err.message),
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

    logout(){
    this.authService.logout();
    this.router.navigate(['/'])
}
}
