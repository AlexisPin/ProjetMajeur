import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, Routes } from '@angular/router';

import { MarkerService } from 'src/services/marker.service';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MapComponent } from './map/map.component';
import { NavbarComponent } from './navbar/navbar.component';
import { VehiculeComponent } from './vehicule/vehicule.component';


const appRoutes : Routes = [
  {path : 'map', component : MapComponent},
  {path : '', component : MapComponent}
]
@NgModule({
  declarations: [
    AppComponent,
    MapComponent,
    NavbarComponent,
    VehiculeComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes),
    AppRoutingModule
  ],
  providers: [MarkerService],
  bootstrap: [AppComponent]
})
export class AppModule { }
