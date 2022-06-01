import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MarkerService } from 'src/app/services/marker.service';

@Component({
  selector: 'app-map-filter',
  templateUrl: './map-filter.component.html',
  styleUrls: ['./map-filter.component.css']
})
export class MapFilterComponent implements OnInit  {
  myForm!: FormGroup;
  formattedMessage!: string;
  
  constructor(private formBuilder: FormBuilder,
              private markerService: MarkerService)
               {}

  ngOnInit(): void {
    this.myForm = this.formBuilder.group({
      inputIntensityMin: new FormControl(1),
      inputIntensityMax: new FormControl(100),
      inputRangeMin:     new FormControl(1),
      inputRangeMax:     new FormControl(100),
      fireA:             new FormControl(true),
      fireBg:            new FormControl(true),
      fireBa:            new FormControl(true),
      fireBp:            new FormControl(true),
      fireCfg:           new FormControl(true),
      fireDm:            new FormControl(true),
      fireEe:            new FormControl(true)
    })

    this.myForm.valueChanges.subscribe(x => {
      console.log('form changed')
      //console.log(x)
      this.markerService.setFilter(x);
    });
  }

  onChanges(): void {
    this.myForm.valueChanges.subscribe(val => {
      this.formattedMessage = `je suis ${val}`
    });
  }

}
