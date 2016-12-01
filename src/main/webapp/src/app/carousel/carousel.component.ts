import { Component, OnInit, ElementRef } from '@angular/core';

@Component({
  selector: 'my-carousel',
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