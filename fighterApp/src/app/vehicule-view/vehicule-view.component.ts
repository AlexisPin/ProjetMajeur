import { Component, OnInit } from '@angular/core';
import { VehicleService } from '../services/vehicle.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-vehicule-view',
  templateUrl: './vehicule-view.component.html',
  styleUrls: ['./vehicule-view.component.css'],
})
export class VehiculeViewComponent implements OnInit {
  constructor(private vehiculeService: VehicleService) {}

  vehicules: any[] = [];
  vehiculeSubscription!: Subscription;
  private flagEdit:boolean[] = Array().fill(false);

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
    this.flagEdit[id] = !this.flagEdit[id];
  }

  getFlagEdit(id:number){
    return this.flagEdit[id];
  }
}

