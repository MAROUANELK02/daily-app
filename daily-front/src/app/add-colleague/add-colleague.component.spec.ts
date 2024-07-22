import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddColleagueComponent } from './add-colleague.component';

describe('AddColleagueComponent', () => {
  let component: AddColleagueComponent;
  let fixture: ComponentFixture<AddColleagueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddColleagueComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddColleagueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
