import { Status } from "./status.model";

export interface BookingResponse {
  id: number;
  providerId: number;
  clientId: number;
  serviceCategory?: string;
  date: string;
  time: string;
  description: string;
  status: Status;
  attachments?: string[];
  createdAt: string;
  updatedAt: string;
}
