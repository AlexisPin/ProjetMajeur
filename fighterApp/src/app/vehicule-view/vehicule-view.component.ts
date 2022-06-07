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
  ourVehicules: any[] = [];

  ngOnInit(): void {
    this.vehiculeService.getVehiculeFromServer();
    this.vehiculeService.getOurVehiculeFromServer();
    this.vehiculeSubscription = this.vehiculeService.vehiculeSubject.subscribe({
      next: (vehicules) => (this.vehicules = vehicules),
    });
    this.vehiculeService.emitVehiculeSubject();
  }

  onDelete(vehicle: any) {
    this.vehiculeService.removeVehicule(vehicle);
  }

  onEdit() {}
}
