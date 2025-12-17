import { Injectable } from "@angular/core";

@Injectable({providedIn:"root"})
export class SearchStateService {

  selectedServiceName!:string;
  selectedCityName!:string;
  selectedServiceId?:number;
  selectedCityId?:number;

  setSearchContext(cityName:string, serviceName:string, cityId?:number, serviceId?:number){
    this.selectedCityName=cityName;
    this.selectedServiceName=serviceName;
    this.selectedCityId=cityId;
    this.selectedServiceId=serviceId;
  }

}
