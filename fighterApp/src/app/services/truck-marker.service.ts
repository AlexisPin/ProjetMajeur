import { Injectable } from '@angular/core';
import * as L from 'leaflet';

@Injectable({
  providedIn: 'root'
})
export class TruckMarkerService {
  facilityAPI: string = 'http://vps.cpe-sn.fr:8081/vehicle';

  constructor() {}

  makeTruckMarkers(map: L.Map): void {
    let context = {
      method: 'GET',
    };
    fetch(this.facilityAPI, context)
      .then((response) => response.json())
      .then((response) => callback(response, map))
      .catch((error) => err_callback(error));
  }
}

function callback(response: any, map: L.Map) {
  for (let id in response) {
    let facilityIcon = L.icon({
      iconUrl:'../assets/images/vehicule.png',
      iconSize: [30, 30], // size of the icon
      popupAnchor: [0, -15],
    });
    const lat = response[id].lat;
    const lon = response[id].lon;
    const marker = L.marker([lat, lon], { icon: facilityIcon });

    marker.addTo(map);
    marker.bindPopup(
      `<h2>
        ${response[id].type} ${response[id].id}
        </h2>
        <h5> LiquidType : ${response[id].liquidType}</h5>
        <h5> Qty : ${response[id].liquidQuantity}</h5>
        <h5> Fuel : ${response[id].fuel}</h5>
        <h5> CrewMember : ${response[id].crewMember}</h5>
        <h5> FaciltyID : ${response[id].facilityRefID}</h5>`
    );
  }
}

function err_callback(error: any) {
  console.log(error);
}

  