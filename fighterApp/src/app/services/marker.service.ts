import { analyzeAndValidateNgModules } from '@angular/compiler';
import { Injectable } from '@angular/core';
import * as L from 'leaflet';

@Injectable({
  providedIn: 'root',
})
export class MarkerService {
  private map: any;
  marker_layer : any = L.layerGroup();
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

  markers : any = [];

  constructor() {}

  //Pas certains du L.Map (le m en majuscule)
  makeFireMarkers(map: L.Map): void {
    this.setMap(map);

    let context = {
      method: 'GET',
    };
    fetch(this.firesAPI, context)
      .then((response) => response.json())
      .then((response) => this.callback(response, map, this.filter))
      .catch((error) => this.err_callback(error));
  }

  updateFire(map: L.Map) : void{
    let context = {
      method: 'GET',
    };
    fetch(this.firesAPI, context)
      .then((response) => response.json())
      .then((response) => this.updateCallback(response, map))
      .catch((error) => this.err_callback(error));
  }

  setMap(map: L.Map) {
    this.map = map;
  }

  getMap() {
    return this.map;
  }


  setFilter(x: any) {
    this.filter = x;
    var map: L.Map = this.getMap();
    this.makeFireMarkers(map);
  }
  callback(response: any, map: L.Map, filter: any) {
    var i = 0;
    if (map.hasLayer(this.marker_layer)) {
      this.marker_layer.clearLayers();
  }
    for (let id in response) {
      const fireId = response[id].id;
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
       if (filter[type]){}
          this.createSingleMarker(response[id]);
      }
    }
  }


  createSingleMarker(fire : any ){
    this.markers[fire.id] = L.marker([fire.lat, fire.lon], {
      icon: this.displayIcon(fire.type),
    });
    this.marker_layer.addLayer(this.markers[fire.id]);

    var myPopup = L.DomUtil.create('div', 'info-popup');
    myPopup.innerHTML = `<h2>FIRE ${fire.id}  </h2> 
    <h5>Fire Type : ${fire.type} </h5>
    <h5>Intensity : ${fire.intensity} </h5>
    <h5>Range : ${fire.range} </h5>`;
    this.markers[fire.id].bindPopup(myPopup);
    this.marker_layer.addTo(this.map);
  }

  removeSingleMarker(id : any){
    this.map.removeLayer(this.markers[id])
    this.markers.splice(id,1);
  }

  updateCallback(response: any, map: L.Map){
    if (map.hasLayer(this.marker_layer)) {
      //regarder si l'id des feux dans la réponse est dans la liste marker si c'est pas le cas on le rajoute 
      for(let id in response){
        //if()
      }
      
      this.setMap(map);
      let context = {
        method: 'GET',
      };
      fetch(this.firesAPI, context)
        .then((response) => response.json())
        .then((response) => this.updatePopUp(response, map))
        .catch((error) => this.err_callback(error));
      }
  }

  updatePopUp(response:any, map: L.Map){
    for (let id in response) {
      const fireId = response[id].id;
      const lat = response[id].lat;
      const lon = response[id].lon;
      const intensity = response[id].intensity;
      const range = response[id].range;
      const type = response[id].type;
      var marker = this.markers[fireId];
      var markerLat = marker['_latlng'].lat;
      var markerLon = marker['_latlng'].lng;

      var myPopup = L.DomUtil.create('div', 'info-popup');
        myPopup.innerHTML = `<h2>FIRE ${response[id].id}  </h2> 
        <h5>Fire Type : ${response[id].type} </h5>
        <h5>Intensity : ${response[id].intensity} </h5>
        <h5>Range : ${response[id].range} </h5>`;
        this.markers[fireId].setPopupContent(myPopup);
    }
  }
  
  err_callback(error: any) {
    console.log(error);
  }
  
  displayIcon(type: string) {
    const imageUrl = type.substring(0, 3);
  
    var icon = `./assets/images/fire-${imageUrl}.png`;
    var fireIcon = L.icon({
      iconUrl: icon,
      iconSize: [40, 40], // size of the icon
      popupAnchor: [0, -15],
    });
  
    return fireIcon;
  }
  
}



