import { Component, OnInit, ElementRef } from '@angular/core';

declare var $: any;

@Component({
  selector: 'my-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
    constructor(private _elmRef: ElementRef) { }

    ngOnInit() { 
         $(this._elmRef.nativeElement).find('[data-toggle="tooltip"]').tooltip();
          $(this._elmRef.nativeElement).find(".spoiler-trigger").click(function() {
           $(this).parent().next().collapse('toggle')});
    }
}
