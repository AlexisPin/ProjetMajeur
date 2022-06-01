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
    var fireIcon = L.icon({
      iconUrl: '../assets/fireIcon.png',
      iconSize: [40, 40], // size of the icon
      popupAnchor: [0,-15]
      });

    for(let id in response){
      const lat = response[id].lat;
      const lon = response[id].lon;
      const marker = L.marker([lat, lon], {icon: fireIcon});

      marker.addTo(map);
      marker.bindPopup("<h1>Feu " + response[id].id + "</h1> </br> <h2> Type : </h2>" 
                       + response[id].type + "</br> <h2> Intensity : </h2>"
                       + response[id].intensity + "</br> <h2> Range : </h2>"
                       + response[id].range,
                      );
    }
  }

  function err_callback(error:any) {
    console.log(error)
  }

