export interface BookingRequest {
  date: string;
  time: string;
  description: string;
  attachments?: string[];
  providerId: number;
  serviceCategory?: string;
}



