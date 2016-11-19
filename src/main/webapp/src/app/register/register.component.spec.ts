import { TestBed } from '@angular/core/testing';

import { RegisterComponent } from './register.component';

describe('Register Component', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({declarations: [RegisterComponent]});
  });

  it('should ...', () => {
    const fixture = TestBed.createComponent(RegisterComponent);
    fixture.detectChanges();
    expect(fixture.nativeElement.children[0].textContent).toContain('Register Works!');
  });

});
