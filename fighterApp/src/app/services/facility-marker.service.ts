import { Injectable } from '@angular/core';
import * as L from 'leaflet';

@Injectable({
  providedIn: 'root',
})
export class FacilityMarkerService {
  facilityAPI: string = 'http://vps.cpe-sn.fr:8081/facility';

  constructor() {}

  makeFacilityMarkers(map: L.Map): void {
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
      iconUrl:
        response[id].name.substring(3, 4) == 2
          ? '../assets/images/owncaserne.svg'
          : '../assets/images/caserne.svg',
      iconSize: [40, 40], // size of the icon
      popupAnchor: [0, -15],
    });
    const lat = response[id].lat;
    const lon = response[id].lon;
    const marker = L.marker([lat, lon], { icon: facilityIcon });

    marker.addTo(map);
    marker.bindPopup(
      `<h2>
        ${response[id].name}
        </h2>
        <h5> maxVehicleSpace : ${response[id].maxVehicleSpace}</h5>
        <h5> peopleCapacity : ${response[id].peopleCapacity}</h5>
        <h5> vehicleIdSet : ${response[id].vehicleIdSet}</h5>
        <h5> peopleIdSet : ${response[id].peopleIdSet}</h5>
        <h5> teamUuid : ${response[id].teamUuid}</h5>
        <button type="button" class="btn btn-outline-danger">Delete</button>`
    );
  }
}

function err_callback(error: any) {
  console.log(error);
}
