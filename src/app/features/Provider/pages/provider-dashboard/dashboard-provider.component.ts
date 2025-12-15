import { CommonModule, DatePipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { BookingService } from '../../../booking/services/booking.service';
import { BookingResponse } from '../../../booking/models/booking-response.model';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule,DatePipe],
  templateUrl: './dashboard-provider.html',
  styleUrl: './dashboard.scss',
})
export class ProviderDashboard implements OnInit {
  private readonly bookingService=inject(BookingService);
  incoming:BookingResponse[]=[];

  ngOnInit(): void {
    this.loadPending();
  }

  loadPending(){
    this.bookingService.getProviderBookings('PENDING').subscribe({
      next:(res)=>this.incoming=res,
      error:(err)=>console.log(err.message)
    })
  }

  accept(id:number){
    this.bookingService.acceptBooking(id).subscribe({next:()=>this.loadPending()});
  }

  reject(id:number){
    this.bookingService.rejectBooking(id).subscribe({next:()=>this.loadPending()});
  }
}
