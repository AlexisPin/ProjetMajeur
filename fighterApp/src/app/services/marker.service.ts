import { Injectable } from '@angular/core';
import * as L from 'leaflet';

@Injectable({
  providedIn: 'root',
})
export class MarkerService {
  private map: any;
  firesAPI: string = 'http://vps.cpe-sn.fr:8081/fire';
  filter = {
    inputIntensityMin: 1,
    inputIntensityMax: 100,
    inputRangeMin: 1,
    inputRangeMax: 100,
    A: true,
    B_Gasoline: true,
    B_Alcohol: true,
    B_Plastics: true,
    C_Flammable_Gases: true,
    D_Metals: true,
    E_Electric: true,
  };

  constructor() {}

  //Pas certains du L.Map (le m en majuscule)
  makeFireMarkers(map: L.Map): void {
    this.setMap(map);
    let context = {
      method: 'GET',
    };
    fetch(this.firesAPI, context)
      .then((response) => response.json())
      .then((response) => callback(response, map, this.filter))
      .catch((error) => err_callback(error));
  }

  setMap(map: L.Map) {
    this.map = map;
  }

  getMap() {
    return this.map;
  }

  setFilter(x: any) {
    this.filter = x;
    console.log(this.filter);
    var map: L.Map = this.getMap();
    this.makeFireMarkers(map);
  }
}
var marker_layer = L.layerGroup();
function callback(response: any, map: L.Map, filter: any) {
  var markers = new Array();
  var i = 0;
  if (map.hasLayer(marker_layer)) {
    marker_layer.clearLayers();
}
  for (let id in response) {
    const lat = response[id].lat;
    const lon = response[id].lon;
    const intensity = response[id].intensity;
    const range = response[id].range;
    const type = response[id].type;
    if (
      intensity <= filter.inputIntensityMax &&
      intensity >= filter.inputIntensityMin &&
      range <= filter.inputRangeMax &&
      range >= filter.inputRangeMin
    ) {
     if (filter[type]){
      markers[i] = L.marker([lat, lon], {
        icon: displayIcon(response[id].type),
      });
      marker_layer.addLayer(markers[i]);
      var myPopup = L.DomUtil.create('div', 'info-popup');
      myPopup.innerHTML = `<h2>FIRE ${response[id].id}  </h2> 
      <h5>Fire Type : ${response[id].type} </h5>
      <h5>Intensity : ${response[id].intensity} </h5>
      <h5>Range : ${response[id].range} </h5>`;
      markers[i].bindPopup(myPopup);
     }
      
    }
    i++;
  }
  marker_layer.addTo(map);
  markers.splice(0);
}
function deleteMarket(marker: any) {
  marker.remove();
}

function err_callback(error: any) {
  console.log(error);
}

function displayIcon(type: string) {
  const imageUrl = type.substring(0, 3);

  var icon = `./assets/images/fire-${imageUrl}.svg`;
  var fireIcon = L.icon({
    iconUrl: icon,
    iconSize: [40, 40], // size of the icon
    popupAnchor: [0, -15],
  });

  return fireIcon;
}
