import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GivenSubscriptionsComponent } from './given-subscriptions.component';

describe('GivenSubscriptionsComponent', () => {
  let component: GivenSubscriptionsComponent;
  let fixture: ComponentFixture<GivenSubscriptionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GivenSubscriptionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GivenSubscriptionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
