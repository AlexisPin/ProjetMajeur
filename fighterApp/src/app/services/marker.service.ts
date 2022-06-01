import { Injectable } from '@angular/core';
import * as L from 'leaflet';

@Injectable({
  providedIn: 'root'
})
export class MarkerService {
  private map:any;

  firesAPI: string = 'http://vps.cpe-sn.fr:8081/fire';
  filter = {
      "inputIntensityMin": 1,
      "inputIntensityMax": 100,
      "inputRangeMin":     1,
      "inputRangeMax":     100,
      "fireA":             true,
      "fireBg":            true,
      "fireBa":            true,
      "fireBp":            true,
      "fireCfg":           true,
      "fireDm":            true,
      "fireEe":            true
  }
  
  constructor() { }

  //Pas certains du L.Map (le m en majuscule)
  makeFireMarkers(map: L.Map): void {
      this.setMap(map);
      let context = {
                      method: 'GET'
                    };
      fetch(this.firesAPI, context)
        .then(response => response.json())
          .then(response => callback(response, map, this.filter))
          .catch(error => err_callback(error));     
  }

  setMap(map: L.Map){
    this.map = map;
  }

  getMap(){
    return this.map;
  }

  setFilter(x:any) {
    this.filter = x;
    console.log(this.filter)
    var map: L.Map = this.getMap();
  }

  }
  function callback(response:any, map: L.Map, filter:any){
    var fires = L.layerGroup();
    map.addLayer(fires);
    
    var fireIcon = L.icon({
      iconUrl: '../assets/fireIcon.png',
      iconSize: [40, 40], // size of the icon
      popupAnchor: [0,-15]
      });
      
    for(let id in response){
      const lat = response[id].lat;
      const lon = response[id].lon;
      const intensity = response[id].intensity;
      const range = response[id].range;
      const type = response[id].type;

      if (intensity <= filter.inputIntensityMax && intensity >= filter.inputIntensityMin
        && range <= filter.inputRangeMax && range >= filter.inputRangeMin) {
          //if (filter.$type){
            const marker = L.marker([lat, lon], {icon: fireIcon});
            marker.addTo(fires);
            marker.bindPopup("<h1>Feu " + response[id].id + "</h1> </br> <h2> Type : </h2>" 
                       + response[id].type + "</br> <h2> Intensity : </h2>"
                       + response[id].intensity + "</br> <h2> Range : </h2>"
                       + response[id].range,
                      );
         
        //}
        }
      
    }
  }

  function err_callback(error:any) {
    console.log(error)
  }

