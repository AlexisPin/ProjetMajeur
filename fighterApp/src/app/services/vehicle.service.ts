import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

interface Vehicule {
  lon: number;
  lat: number;
  type: string;
  liquidType: string;
  liquidQuantity: number;
  fuel: number;
  crewMember: number;
  facilityRefID: number;
}

@Injectable({
  providedIn: 'root',
})
export class VehicleService {
  vehiculeSubject = new Subject<Vehicule[]>();

  private vehicules: Vehicule[] = [];

  constructor() {}

  emitVehiculeSubject() {
    this.vehiculeSubject.next(this.vehicules.slice());
  }

  addVehicule(vehicule: Vehicule) {
    this.vehicules.push(vehicule);
    this.emitVehiculeSubject();
    this.saveVehiculeToServer(vehicule);
  }

  saveVehiculeToServer(vehicule: Vehicule) {
    console.log(vehicule);

    const vehicleUrl =
      'http://vps.cpe-sn.fr:8081/vehicle/3e84503c-ce82-476b-b702-b380cb6b43d8';
    let context = {
      method: 'POST',
      body: JSON.stringify(vehicule),
      headers: {
        'Content-Type': 'application/json',
      },
    };
    fetch(vehicleUrl, context)
      .then((response) => response.json())
      .then((data) => {
        console.log('data');
      });
  }

  getVehiculeFromServer() {
    const vehicleUrl = 'http://alexispin.synology.me:9080/own/vehicle';
    let context = {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    };
    fetch(vehicleUrl, context)
      .then((response) => response.json())
      .then((data) => {
        (this.vehicules = data), this.emitVehiculeSubject();
      })
      .catch((error) => {
        console.log(error);
      });
  }

  removeVehicule(vehicule: Vehicule) {
    const vehicleUrl =
      'http://vps.cpe-sn.fr:8081/vehicle/3e84503c-ce82-476b-b702-b380cb6b43d8';
    let context = {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
    };
    fetch(vehicleUrl, context)
      .then((response) => response.json())
      .then((data) => {
        console.log('data');
      });

    const indexToRemove = this.vehicules.findIndex((v) => v === vehicule);
    this.vehicules.splice(indexToRemove, 1);
    this.emitVehiculeSubject();
  }

  updateVehicule(vehicle: Vehicule, id:number) {
    console.log(vehicle)
    console.log(id)
    
    /*
    const vehicleUrl =
      `http://vps.cpe-sn.fr:8081/vehicle/3e84503c-ce82-476b-b702-b380cb6b43d8/${id}`;
    let context = {
      method: 'PUT',
      body: JSON.stringify(vehicle),
      headers: {
        'Content-Type': 'application/json',
      },
    };
    fetch(vehicleUrl, context)
      .then((response) => response.json())
      .then((data) => {
        console.log('data');
      });
    */
    
  }
}
