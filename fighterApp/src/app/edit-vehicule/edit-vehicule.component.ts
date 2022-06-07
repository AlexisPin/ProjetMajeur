import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { VehicleService } from '../services/vehicle.service';

@Component({
  selector: 'app-edit-vehicule',
  templateUrl: './edit-vehicule.component.html',
  styleUrls: ['./edit-vehicule.component.css']
})
export class EditVehiculeComponent implements OnInit {

  UpdateVehicle!: FormGroup;
  private static vehicle: any;
  private static id:number;
  
  constructor(
    private vehiculeService: VehicleService,
    private formBuilder: FormBuilder) { }



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

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {
    this.UpdateVehicle = this.formBuilder.group({
      vehicleType: ['', [Validators.required]],
      liquidType: ['', [Validators.required]],
    });
  }

  static setPreviousData(vehicle:any){
    this.vehicle = vehicle; 
  }

  static getPreviousData(){
    return this.vehicle;
  }

  static setId(id:number){
    this.id = id;
  }

  static getId(){
    return this.id;
  }

  onSubmit(){
    this.updateVehicle(this.UpdateVehicle.value);
  }

  updateVehicle(vehicle:any){
    let data = {
      id: EditVehiculeComponent.getId(),
      lon: EditVehiculeComponent.vehicle.lon,
      lat: EditVehiculeComponent.vehicle.lat,
      type: vehicle.vehicleType,
      liquidType: vehicle.liquidType,
      liquidQuantity: EditVehiculeComponent.vehicle.liquidQuantity,
      fuel: EditVehiculeComponent.vehicle.fuel,
      crewMember: EditVehiculeComponent.vehicle.crewMember,
      facilityRefID: EditVehiculeComponent.vehicle.facilityRefID,
    };

    this.vehiculeService.updateVehicule(data, EditVehiculeComponent.getId())
      
    }
}

