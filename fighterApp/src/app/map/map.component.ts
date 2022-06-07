import { Component, AfterViewInit } from '@angular/core';
import * as L from 'leaflet';
import { MarkerService } from 'src/app/services/marker.service';
import { FacilityMarkerService } from '../services/facility-marker.service';
import { TruckMarkerService } from '../services/truck-marker.service';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css'],
})
export class MapComponent implements AfterViewInit {
  private map: any;

  private initMap(): void {
    this.map = L.map('map', {
      center: [45.75, 4.85],
      zoom: 11,
    });

    const tiles = L.tileLayer(
      'https://api.mapbox.com/styles/v1/mapbox/light-v10/tiles/{z}/{x}/{y}?access_token={accessToken}',
      {
        maxZoom: 18,
        minZoom: 3,
        id: 'mapbox/streets-v11',
        attribution: '© <a href="https://www.mapbox.com/map-feedback/">Mapbox</a> © <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
        accessToken: 'pk.eyJ1IjoiYWRyaWxhcCIsImEiOiJjbDN2Y2N0eGsweWk5M3NueXB0OW50Y29pIn0.J9hlPan0oV4A0KKHgd4CLg'
      }
    );

    tiles.addTo(this.map);
  }

  constructor(
    private markerService: MarkerService,
    private facilityMarkerService: FacilityMarkerService,
    private truckMarkerService: TruckMarkerService,
  ) {}

  ngAfterViewInit(): void {
    this.initMap();
    this.markerService.makeFireMarkers(this.map);
    this.facilityMarkerService.makeFacilityMarkers(this.map);
    this.truckMarkerService.makeTruckMarkers(this.map);
    this.truckMarkerService.getRoute(this.map);
    this.Update();
  }

  Update() : void{
    this.truckMarkerService.updateTruck(this.map);
    this.markerService.updateFire(this.map);
    this.truckMarkerService.getRoute(this.map);
    setTimeout(() => {
      this.Update();
  }, 1000);
  }
}
