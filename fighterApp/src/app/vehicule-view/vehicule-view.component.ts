import { Component, OnInit } from '@angular/core';
import { VehicleService } from '../services/vehicle.service';
import { Subscription } from 'rxjs';
import { EditVehiculeComponent } from '../edit-vehicule/edit-vehicule.component';

@Component({
  selector: 'app-vehicule-view',
  templateUrl: './vehicule-view.component.html',
  styleUrls: ['./vehicule-view.component.css'],
})
export class VehiculeViewComponent implements OnInit {
  
  constructor(public vehiculeService: VehicleService) {}

  vehicules: any[] = [];
  vehiculeSubscription!: Subscription;
  

  ngOnInit(): void {
    this.vehiculeService.getVehiculeFromServer();
    this.vehiculeSubscription = this.vehiculeService.vehiculeSubject.subscribe({
      next: (vehicules) => (this.vehicules = vehicules),
    });
    this.vehiculeService.emitVehiculeSubject();
  }

  onDelete(vehicle: any) {
    this.vehiculeService.removeVehicule(vehicle);
  }

  onEdit(vehicle: any, id:number) {
    this.vehiculeService.flagEdit[id] = !this.vehiculeService.flagEdit[id];
    EditVehiculeComponent.setPreviousData(vehicle);
    EditVehiculeComponent.setId(id);
  }
}

