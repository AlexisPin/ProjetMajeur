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
      A:             new FormControl(true),
      B_Gasoline:            new FormControl(true),
      B_Alcohol:            new FormControl(true),
      B_Plastics:            new FormControl(true),
      C_Flammable_Gases:           new FormControl(true),
      D_Metals:            new FormControl(true),
      E_Electric:            new FormControl(true)
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
