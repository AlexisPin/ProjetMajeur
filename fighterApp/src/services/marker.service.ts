import { Injectable } from '@angular/core';
import * as L from 'leaflet';

@Injectable({
  providedIn: 'root'
})
export class MarkerService {

  firesAPI: string = 'http://vps.cpe-sn.fr:8081/fire';

  constructor() { }

  //Pas certains du L.Map (le m en majuscule)
  makeFireMarkers(map: L.Map): void {
      let context = {
                      method: 'GET'
                    };
      fetch(this.firesAPI, context)
        .then(response => response.json())
          .then(response => callback(response, map))
          .catch(error => err_callback(error));     
  }

  }
  function callback(response:any, map: L.Map){
    for(let id in response){
      const lat = response[id].lat;
      const lon = response[id].lon;
      const marker = L.marker([lat, lon]);
      
      marker.addTo(map);
    }
  }

  function err_callback(error:any) {
    console.log(error)
  }

