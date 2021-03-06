import { Inject, Injectable } from '@angular/core';
import * as L from 'leaflet';

@Injectable({
  providedIn: 'root',
})
export class TruckMarkerService {
  vehicleAPI: string = 'http://alexispin.synology.me:9080/vehicle';
  marker_layer: any = L.layerGroup();
  private map: any;

  routeAPI: string = 'http://localhost:8080/routes';

  polylines: any = [];
  polyline: any = [];
  routeControl: any = [];

  private show: boolean = false;

  private route: any = [];

  private idRouteShown: number = 0;

  filter = {
    CAR: true,
    FIRE_ENGINE: true,
    PUMPER_TRUCK: true,
    WATER_TENDER: true,
    TURNTABLE_LADDER_TRUCK: true,
    TRUCK: true,
    ALL: true,
    WATER: true,
    WATER_WITH_ADDITIVES: true,
    CARBON_DIOXIDE: true,
    POWDER: true,
  };

  markers: any = [];

  constructor() {}

  makeTruckMarkers(map: L.Map): void {
    this.setMap(map);
    var vehicle: any;
    var ourVehicle: any;

    fetch(this.vehicleAPI)
      .then(function (response) {
        return response.json();
      })
      .then(function (data) {
        vehicle = data;
        return fetch('http://alexispin.synology.me:9080/own/vehicle');
      })
      .then(function (response) {
        return response.json();
      })
      .then((data) => {
        ourVehicle = data;
        this.callback([ourVehicle, vehicle], map, this.filter);
      })
      .catch(function (error) {
        console.log('Request Truck Failed : ', error);
      });
  }

  updateTruck(map: L.Map): void {
    let context = {
      method: 'GET',
    };
    fetch(this.vehicleAPI, context)
      .then((response) => response.json())
      .then((response) => this.updateCallback(response, map))
      .catch((error) => this.err_callback(error));
  }

  updateCallback(response: any, map: L.Map) {
    if (map.hasLayer(this.marker_layer)) {
      for (let id in response) {
        const lat = response[id].lat;
        const lon = response[id].lon;
        const truckId = response[id].id;
        var marker = this.markers[truckId];
        var markerLat = marker['_latlng'].lat;
        var markerLon = marker['_latlng'].lng;
        if (lat != markerLat && lon != markerLon) {
          var newLatLng = new L.LatLng(lat, lon);
          marker.setLatLng(newLatLng);
          var myPopup = L.DomUtil.create('div', 'info-popup');
          myPopup.innerHTML = `<h2>
          ${response[id].type} ${response[id].id}
          </h2>
          <h5> LiquidType : ${response[id].liquidType}</h5>
          <h5> Qty : ${response[id].liquidQuantity}</h5>
          <h5> Fuel : ${response[id].fuel}</h5>
          <h5> CrewMember : ${response[id].crewMember}</h5>
          <h5> FaciltyID : ${response[id].facilityRefID}</h5>`;
          marker.setPopupContent(myPopup)
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

  removeLayer(map: any) {
    if (map.hasLayer(this.marker_layer)) {
      this.marker_layer.clearLayers();
    }
    this.makeTruckMarkers(map);
  }

  callback(response: any, map: L.Map, filter: any) {
    if (map.hasLayer(this.marker_layer)) {
      this.marker_layer.clearLayers();
  }
    const ourVehicle = response[0];
    const vehicle = response[1];

    const allOurId = ourVehicle.map((d: { id: any }) => d.id);
    let facilityIcon;

    for (let id in vehicle) {
      if (allOurId.includes(vehicle[id].id)) {
        facilityIcon = L.icon({
          iconUrl: `../assets/images/our_${vehicle[id].type.toLowerCase()}.png`,
          iconSize: [40, 40], // size of the icon
          popupAnchor: [0, -15],
        });
      } else {
        facilityIcon = L.icon({
          iconUrl: `../assets/images/oth_${vehicle[id].type.toLowerCase()}.png`,
          iconSize: [40, 40], // size of the icon
          popupAnchor: [0, -15],
        });
      }

      const truckId = vehicle[id].id;
      const lat = vehicle[id].lat;
      const lon = vehicle[id].lon;
      const type = vehicle[id].type;
      const liqtype = vehicle[id].liquidType;
      if (filter[type] && filter[liqtype]) {
        this.markers[truckId] = L.marker([lat, lon], { icon: facilityIcon }).on(
          'click',
          (e) => {
            this.showRoute(truckId);
          }
        );
        this.marker_layer.addLayer(this.markers[truckId]);
        this.markers[truckId].bindPopup(
          `<h2>
            ${vehicle[id].type} ${vehicle[id].id}
            </h2>
            <h5> LiquidType : ${vehicle[id].liquidType}</h5>
            <h5> Qty : ${vehicle[id].liquidQuantity}</h5>
            <h5> Fuel : ${vehicle[id].fuel}</h5>
            <h5> CrewMember : ${vehicle[id].crewMember}</h5>
            <h5> FaciltyID : ${vehicle[id].facilityRefID}</h5>`
        );
      }
      this.marker_layer.addTo(map);
    }
   
  }

  createSingleMarker(truck : any){

  }

  getRoute(map: any) {
    this.setMap(map);
    let context = {
      method: 'GET',
    };
    fetch(this.routeAPI, context)
      .then((response) => response.json())
      .then((response) => this.Route(response))
      .catch((error) => this.err_callback(error));
  }

  Route(response: any) {
    var waypoint = [];
    if (this.polylines[this.idRouteShown] != undefined) {
      //console.log(this.polylines[this.idRouteShown]);
      if (this.map.hasLayer(this.polyline)) {
        this.map.removeLayer(this.polyline);
      }
    }
    for (let id in response) {
      if (this.polylines[id] != undefined) {
      }
      for (let i = 0; i < response[id].length; i++) {
        if (response[id][i].length != 0) {
          waypoint.push(L.latLng(response[id][i][1], response[id][i][0]));
        }
      }
      this.polylines[id] = waypoint;
      waypoint = [];
    }
    //console.log(waypoint);
    if (this.idRouteShown != 0) {
      if (this.polylines[this.idRouteShown] != undefined) {
        if (this.polylines[this.idRouteShown].length != 0) {
          this.route = this.polylines[this.idRouteShown];
          this.polyline = L.polyline(this.route, { color: 'red' }).addTo(
            this.map
          );
          this.map.fitBounds(this.polyline.getBounds());
        }
      }
    }
  }

  showRoute(truckId: any) {
    if (this.map.hasLayer(this.polyline)) {
      this.map.removeLayer(this.polyline);
    }

    this.idRouteShown = truckId;
  }

  hideRoute() {
    if (this.map.hasLayer(this.polyline)) {
      this.map.removeLayer(this.polyline);
      this.idRouteShown = 0;
    }
  }
  err_callback(error: any) {
    console.log(error);
  }
}
