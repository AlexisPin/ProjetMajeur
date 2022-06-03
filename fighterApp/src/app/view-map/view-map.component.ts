import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-view-map',
  templateUrl: './view-map.component.html',
  styleUrls: ['./view-map.component.css']
})
export class ViewMapComponent implements OnInit {


  filter : number = 0;
  constructor() { }

  ngOnInit(): void {
  }

  showFilterVehicle() : void{
    this.filter = 1;
    console.log(this.filter);
  }

  showFilterFire() : void{
    this.filter = 0;
    console.log(this.filter);
  }
}
