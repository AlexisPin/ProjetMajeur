import { Injectable } from '@angular/core';
import * as L from 'leaflet';

@Injectable({
  providedIn: 'root'
})
export class TruckMarkerService {
  facilityAPI: string = 'http://vps.cpe-sn.fr:8081/vehicle';
  marker_layer : any = L.layerGroup();
  private map: any;

  filter = {
      CAR : true,
      FIRE_ENGINE : true,
      PUMPER_TRUCK : true,
      WATER_TENDER : true,
      TURNTABLE_LADDER_TRUCK : true,
      TRUCK : true,
      ALL : true,
      WATER : true,
      WATER_WITH_ADDITIVES : true,
      CARBON_DIOXIDE : true,
      POWDER : true,
  };



  constructor() {}

  makeTruckMarkers(map: L.Map): void {
    let context = {
      method: 'GET',
    };
    fetch(this.facilityAPI, context)
      .then((response) => response.json())
      .then((response) => this.callback(response, map, this.filter))
      .catch((error) => this.err_callback(error));
  }

  updateTruck(map: L.Map): void{
    let context = {
      method: 'GET',
    };
    fetch(this.facilityAPI, context)
      .then((response) => response.json())
      .then((response) => this.updateCallback(response, map))
      .catch((error) => this.err_callback(error));
  }  

  updateCallback(response: any, map: L.Map){
    if (map.hasLayer(this.marker_layer)) {
      const markers = this.marker_layer.getLayers();
      if (markers.length != response.length){
        this.makeTruckMarkers(map);
      }
      for(let id in response){
        const lat = response[id].lat;
        const lon = response[id].lon;
        var newLatLng = new L.LatLng(lat, lon);
        var marker = markers[id];
        marker.setLatLng(newLatLng);     
      }
    }
  }

  getMap() {
    return this.map;
  }
  
  setFilter(x: any) {
    this.filter = x;
    console.log(this.filter);
    var map: L.Map = this.getMap();
    this.makeTruckMarkers(map);
  }

  callback(response: any, map: L.Map, filter : any) {
    var markers = new Array();
    var i = 0;
    if (map.hasLayer(this.marker_layer)) {
      this.marker_layer.clearLayers();
  }
    for (let id in response) {      
      let facilityIcon = L.icon({
        iconUrl:'../assets/images/vehicule.png',
        iconSize: [40, 40], // size of the icon
        popupAnchor: [0, -15],
      });
      const lat = response[id].lat;
      const lon = response[id].lon;
      const type = response[id].type;
      const liqtype = response[id].liquidType
      if (filter[type] && filter[liqtype]){
        markers[i] = L.marker([lat, lon], { icon: facilityIcon });
        this.marker_layer.addLayer(markers[i]);
        markers[i].bindPopup(
          `<h2>
            ${response[id].type} ${response[id].id}
            </h2>
            <h5> LiquidType : ${response[id].liquidType}</h5>
            <h5> Qty : ${response[id].liquidQuantity}</h5>
            <h5> Fuel : ${response[id].fuel}</h5>
            <h5> CrewMember : ${response[id].crewMember}</h5>
            <h5> FaciltyID : ${response[id].facilityRefID}</h5>`
        );
        i++;
      }
    }
    this.marker_layer.addTo(map);
    markers.splice(0);
  }
  
  
  
  err_callback(error: any) {
    console.log(error);
  }
}



  