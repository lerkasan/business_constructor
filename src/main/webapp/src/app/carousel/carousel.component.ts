import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'brdo-carousel',
  templateUrl: './carousel.component.html',
  styleUrls: ['./carousel.component.scss']
})
export class CarouselComponent implements OnInit {

  constructor() {
    // Do stuff
  }

  ngOnInit() {
    console.log('Hello header');
  }
}
