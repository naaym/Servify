import { Component, inject, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ClientBookingService } from '../../clientbooking.service';
import { ActivatedRoute } from '@angular/router';
import { BookingResponse } from '../../../../../booking/models/booking-response.model';

@Component({
  selector: 'app-details.component',
  imports: [CommonModule,DatePipe],
  templateUrl: './details.component.html',
  styleUrl: './details.component.scss',
})
export class DetailsComponent implements OnInit {
  private readonly clientbookingservice=inject(ClientBookingService);
  private readonly route=inject(ActivatedRoute)
  bookingDetails:BookingResponse|null=null;
  errorMessage:string="";

  ngOnInit(): void {
    const bookingId=Number(this.route.snapshot.paramMap.get("bookingId"));
    this.loadDetails(bookingId)

  }
  loadDetails(id:number){
    this.clientbookingservice.getBookingsById(id).subscribe({next:res=>{this.bookingDetails=res},
      error:err=>this.errorMessage=err.message})

    }


    cancelRequest() {
      if(this.bookingDetails){
        this.clientbookingservice.bookingService.cancelBooking(this.bookingDetails.id).subscribe({
          next:()=>this.loadDetails(this.bookingDetails!.id),
          error:(err)=>this.errorMessage=err.message
        })
      }
    }

    contactProvider() {
      console.log('Contact provider');
    }
}
