import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MarkerService } from 'src/app/services/marker.service';
import { TruckMarkerService } from '../services/truck-marker.service';

@Component({
  selector: 'app-truck-filter',
  templateUrl: './truck-filter.component.html',
  styleUrls: ['./truck-filter.component.css']
})
export class TruckFilterComponent implements OnInit {

  myForm!: FormGroup;
  formattedMessage!: string;
  
  constructor(private formBuilder: FormBuilder,
              private truckMarkerService: TruckMarkerService)
               {}
  ngOnInit(): void {
    this.myForm = this.formBuilder.group({

      CAR : new FormControl(true),
      FIRE_ENGINE: new FormControl(true),
      PUMPER_TRUCK:     new FormControl(true),
      WATER_TENDER:     new FormControl(true),
      TURNTABLE_LADDER_TRUCK:             new FormControl(true),
      TRUCK:            new FormControl(true),
      ALL:            new FormControl(true),
      WATER:            new FormControl(true),
      WATER_WITH_ADDITIVES:           new FormControl(true),
      CARBON_DIOXIDE:            new FormControl(true),
      POWDER:            new FormControl(true)
    })

    //When one value changes on the fire form
    this.myForm.valueChanges.subscribe(x => {
      //console.log('form changed')
      //console.log(x)
      this.truckMarkerService.setFilter(x);
    });
  }
  }


