import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { MarkerService } from 'src/services/marker.service';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MapComponent } from './map/map.component';
import { MapFilterComponent } from './map-filter/map-filter.component';

@NgModule({
  declarations: [
    AppComponent,
    MapComponent,
    MapFilterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [MarkerService],
  bootstrap: [AppComponent]
})
export class AppModule { }
