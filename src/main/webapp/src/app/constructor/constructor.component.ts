import { Component } from '@angular/core';

@Component({
  selector: 'constructor-panel',
  templateUrl: './constructor.component.html',
  styleUrls: ['./constructor.component.scss']

})


export class ConstructorComponent{
	constructor() {
  		
	}
	showForm (){
		var parent = document.getElementById('constructor');
		var button = document.getElementById('startButton');
		var form = document.getElementById('formQustionare');
		form.removeAttribute('hidden');
		parent.replaceChild(form, button);
	}

}


