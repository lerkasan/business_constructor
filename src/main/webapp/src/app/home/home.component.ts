import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'my-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor() {
  }

  ngOnInit() {
    console.log('Hello Home');
      (function($) {

  	   $(document).ready(function(){
  	    $('[data-toggle="tooltip"]').tooltip(); 

       $(".spoiler-trigger").click(function() {
        $(this).parent().next().collapse('toggle');
      });
    });
    })(jQuery);
  }

}
