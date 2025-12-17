import { inject, Injectable } from "@angular/core";
import { HttpParams } from '@angular/common/http';
import { Http } from "../../../core/api/http";
import { API_ENDPOINTS } from "../../../core/api/endpoints";
import { catchError, throwError } from "rxjs";
import { BookingRequest } from "../models/booking-request.model";
import { BookingResponse } from "../models/booking-response.model";

@Injectable({providedIn:"root"})
export class BookingService {

  http=inject(Http);
  createBooking(dto:BookingRequest){
    return this.http.post<BookingResponse>(`${API_ENDPOINTS.BOOKING.BASE}`,dto)
    .pipe(catchError((err)=>{
      const normalized={
        message:err.message}
      return throwError(()=>normalized)
  }))
  }

  getClientBookings(status?: string | null) {
  let params = new HttpParams();

  if (status) {
    params = params.set('status', status);
  }
    return this.http.get<BookingResponse[]>(`${API_ENDPOINTS.BOOKING.CLIENT}`,{params})
    .pipe(catchError((err)=>{
      const normalized={message:err.message};
      return throwError(()=>normalized)
    }))
  }

  getProviderBookings(status?:string){
    return this.http.get<BookingResponse[]>(`${API_ENDPOINTS.BOOKING.PROVIDER}`,{status})
    .pipe(catchError((err)=>{
      const normalized={message:err.message};
      return throwError(()=>normalized)
    }))
  }

  getBookingById(id:number){
    return this.http.getOne<BookingResponse>(`${API_ENDPOINTS.BOOKING.BASE}`,id)
    .pipe(catchError((err)=>{
      const normalized={message:err.message};
      return throwError(()=>normalized)
    }))
  }

  acceptBooking(id:number){
    return this.http.patch<BookingResponse>(`${API_ENDPOINTS.BOOKING.BASE}/${id}/accept`,{})
  }

  rejectBooking(id:number){
    return this.http.patch<BookingResponse>(`${API_ENDPOINTS.BOOKING.BASE}/${id}/reject`,{})
  }

  cancelBooking(id:number){
    return this.http.patch<BookingResponse>(`${API_ENDPOINTS.BOOKING.BASE}/${id}/cancel`,{})
  }



}
