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

  markers : any = [];

  constructor() {}

  makeTruckMarkers(map: L.Map): void {
    this.setMap(map);
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
      for(let id in response){
        const lat = response[id].lat;
        const lon = response[id].lon;
        const truckId = response[id].id;
        var marker = this.markers[truckId];
        var markerLat = marker['_latlng'].lat;
        var markerLon = marker['_latlng'].lng;
        if (lat != markerLat && lon != markerLon){
          var newLatLng = new L.LatLng(lat, lon);
          marker.setLatLng(newLatLng);  
        }   
      }
    }
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
    this.removeLayer(map);
    
  }

  removeLayer(map : any){
    if (map.hasLayer(this.marker_layer)) {
      this.marker_layer.clearLayers();
   }
   this.makeTruckMarkers(map);
  }

  callback(response: any, map: L.Map, filter : any) {
    var i = 0;

    for (let id in response) {      
      let facilityIcon = L.icon({
        iconUrl:'../assets/images/vehicule.png',
        iconSize: [40, 40], // size of the icon
        popupAnchor: [0, -15],
      });
      const truckId = response[id].id;
      const lat = response[id].lat;
      const lon = response[id].lon;
      const type = response[id].type;
      const liqtype = response[id].liquidType;
      this.markers[truckId] = L.marker([lat, lon], { icon: facilityIcon });
      if (filter[type] && filter[liqtype]){
        this.marker_layer.addLayer(this.markers[truckId]);
        this.markers[truckId].bindPopup(
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
    this.marker_layer.addTo(map);
  }
  
  
  
  err_callback(error: any) {
    console.log(error);
  }
}



  