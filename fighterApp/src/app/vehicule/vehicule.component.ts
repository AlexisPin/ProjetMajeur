import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { VehicleService } from '../services/vehicle.service';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
@Component({
  selector: 'app-vehicule',
  templateUrl: './vehicule.component.html',
  styleUrls: ['./vehicule.component.css'],
})
export class VehiculeComponent implements OnInit {
  AddVehicle!: FormGroup;
  Fire: any = [
    'CAR',
    'FIRE_ENGINE',
    'PUMPER_TRUCK',
    'WATER_TENDER',
    'TURNTABLE_LADDER_TRUCK',
    'TRUCK',
  ];
  Liquid: any = [
    'ALL',
    'WATER',
    'WATER_WITH_ADDITIVES',
    'CARBON_DIOXIDE',
    'POWDER',
  ];
  Facility: any = [];
  vehicules: any = [];
  vehiculeSubscription!: Subscription;

  constructor(
    private formBuilder: FormBuilder,
    private vehiculeService: VehicleService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.vehiculeService.getVehiculeFromServer();
    this.vehiculeSubscription = this.vehiculeService.vehiculeSubject.subscribe({
      next: (vehicules) => (this.vehicules = vehicules),
    });
    this.vehiculeService.emitVehiculeSubject();

    let facilityUrl = 'http://vps.cpe-sn.fr:8081/facility/80';
    let context = {
      method: 'GET',
    };
    fetch(facilityUrl, context)
      .then((response) => response.json())
      .then((response) => this.callback(response));
  }

  onSubmit(): void {
    this.addVehicle(this.AddVehicle.value);
    this.router.navigate(['/vehicules']);
  }

  initForm() {
    this.AddVehicle = this.formBuilder.group({
      vehicleType: ['', [Validators.required]],
      liquidType: ['', [Validators.required]],
      facility: ['', [Validators.required]],
    });
  }

  callback(response: any) {
    this.Facility.push({
      name: response.name,
      id: response.id,
      lon: response.lon,
      lat: response.lat,
    });
  }

  addVehicle(vehicle: any) {
    let data = {
      lon: vehicle.facility.lon,
      lat: vehicle.facility.lat,
      type: vehicle.vehicleType,
      liquidType: vehicle.liquidType,
      liquidQuantity: 0.0,
      fuel: 0.0,
      crewMember: 0,
      facilityRefID: vehicle.facility.id,
    };

    this.vehiculeService.addVehicule(data);
  }
}
