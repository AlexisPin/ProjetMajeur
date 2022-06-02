import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, Routes } from '@angular/router';

import { MarkerService } from 'src/app/services/marker.service';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MapComponent } from './map/map.component';
import { NavbarComponent } from './navbar/navbar.component';
import { VehiculeComponent } from './vehicule/vehicule.component';
import { ViewMapComponent } from './view-map/view-map.component';
import { MapFilterComponent } from './map-filter/map-filter.component';
import { ReactiveFormsModule } from '@angular/forms';
import { FacilityMarkerService } from './services/facility-marker.service';
import { VehiculeViewComponent } from './vehicule-view/vehicule-view.component';
import { TruckMarkerService } from './services/truck-marker.service';
import { TruckFilterComponent } from './truck-filter/truck-filter.component';

const appRoutes: Routes = [
  { path: 'map', component: ViewMapComponent },
  { path: 'vehicule', component: VehiculeComponent },
  { path: 'vehicules', component: VehiculeViewComponent },
  { path: '', component: ViewMapComponent },
  { path: '**', redirectTo: '/map' },
];
@NgModule({
  declarations: [
    AppComponent,
    MapComponent,
    NavbarComponent,
    VehiculeComponent,
    ViewMapComponent,
    MapFilterComponent,
    VehiculeViewComponent,
    TruckFilterComponent,
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes),
    AppRoutingModule,
    ReactiveFormsModule,
  ],
  providers: [MarkerService, FacilityMarkerService, TruckMarkerService],
  bootstrap: [AppComponent],
})
export class AppModule {}
