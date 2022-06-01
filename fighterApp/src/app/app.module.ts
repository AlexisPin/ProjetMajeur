import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, Routes } from '@angular/router';

import { MarkerService } from 'src/services/marker.service';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MapComponent } from './map/map.component';
import { NavbarComponent } from './navbar/navbar.component';
import { VehiculeComponent } from './vehicule/vehicule.component';
import { ViewMapComponent } from './view-map/view-map.component';
import { MapFilterComponent } from './map-filter/map-filter.component';
import { ReactiveFormsModule } from '@angular/forms';


const appRoutes : Routes = [
  {path : 'map', component : ViewMapComponent},
  {path : '', component : ViewMapComponent}
]
@NgModule({
  declarations: [
    AppComponent,
    MapComponent,
    NavbarComponent,
    VehiculeComponent,
    ViewMapComponent,
    MapFilterComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes),
    AppRoutingModule,
    ReactiveFormsModule
  ],
  providers: [MarkerService],
  bootstrap: [AppComponent]
})
export class AppModule { }
