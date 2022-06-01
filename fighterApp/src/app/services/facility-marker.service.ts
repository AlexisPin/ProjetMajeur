import { Injectable } from '@angular/core';
import * as L from 'leaflet';

@Injectable({
  providedIn: 'root'
})
export class FacilityMarkerService {

  facilityAPI: string = 'http://vps.cpe-sn.fr:8081/facility';

  constructor() { }


  makeFacilityMarkers(map: L.Map): void {
    let context = {
                    method: 'GET'
                  };
    fetch(this.facilityAPI, context)
      .then(response => response.json())
        .then(response => callback(response, map))
        .catch(error => err_callback(error));     
}

}
  function callback(response:any, map: L.Map){
    var fireIcon = L.icon({
      iconUrl: '../assets/fireStationIcon.png',
      iconSize: [40, 40], // size of the icon
      popupAnchor: [0,-15]
      });
      
    for(let id in response){
      const lat = response[id].lat;
      const lon = response[id].lon;
      const marker = L.marker([lat, lon], {icon: fireIcon});

      marker.addTo(map);
      marker.bindPopup("oui");
    }
  }

  function err_callback(error:any) {
    console.log(error)
  }

