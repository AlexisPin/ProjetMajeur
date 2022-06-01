import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, NgForm } from '@angular/forms';

@Component({
  selector: 'app-vehicule',
  templateUrl: './vehicule.component.html',
  styleUrls: ['./vehicule.component.css']
})
export class VehiculeComponent implements OnInit {

  AddVehicle!: FormGroup;
  Fire : any = ["CAR","FIRE_ENGINE","PUMPER_TRUCK","WATER_TENDER","TURNTABLE_LADDER_TRUCK","TRUCK"];
  Liquid : any = ["ALL","WATER","WATER_WITH_ADDITIVES","CARBON_DIOXIDE","POWDER"];
  Facility : any = [];
  form : boolean = true;
  vehicules : any = [];
  
  constructor(private formBuilder: FormBuilder,) {}

  ngOnInit(): void {
    this.initForm();
    this.getVehicle();
    let facilityUrl = "http://vps.cpe-sn.fr:8081/facility/80";
    let context = {
      method: 'GET'
    };
   fetch(facilityUrl, context)
   .then(response => response.json())
    .then(response => this.callback(response))
  }

  onSubmit(): void {
      console.log(this.AddVehicle.value);
      this.addVehicle(this.AddVehicle.value);
  }

  initForm() {
    this.AddVehicle = this.formBuilder.group({
      vehicleType : [''],
      liquidType : [''],
      facility : [''],
    });
  }

  callback(response : any){
    this.Facility.push({name : response.name, id : response.id, lon : response.lon, lat : response.lat});
  }

  addVehicle(vehicle : any){
    let data = {
      "lon": vehicle.facility.lon,
      "lat": vehicle.facility.lat,
      "type": vehicle.vehicleType,
      "liquidType": vehicle.liquidType,
      "liquidQuantity": 0.0,
      "fuel": 0.0,
      "crewMember": 0,
      "facilityRefID": vehicle.facility.id
    }

    const vehicleUrl = "http://vps.cpe-sn.fr:8081/vehicle/bd4dd8f2-c28d-46ba-a342-9d9b99259a67";
    let context = {
      method: "POST",
      body: JSON.stringify(data),
      headers: {
        "Content-Type": "application/json",
      },
    };
    fetch(vehicleUrl, context)
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
      });

  }

  onForm(){
    this.form = true;
  }

  onList(){
    this.form = false;
  }

  getVehicle(){
    const vehicleUrl = "http://vps.cpe-sn.fr:8081/vehicle";
    let context = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };
    fetch(vehicleUrl, context)
      .then((response) => response.json())
      .then((response) => {
        this.listVehicle(response);
      });
  }

  listVehicle(response : any){
    this.vehicules.push(response);
    console.log(this.vehicules[0]);
  }
}
