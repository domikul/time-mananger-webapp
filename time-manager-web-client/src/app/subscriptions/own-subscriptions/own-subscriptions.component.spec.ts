import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OwnSubscriptionsComponent } from './own-subscriptions.component';

describe('OwnSubscriptionsComponent', () => {
  let component: OwnSubscriptionsComponent;
  let fixture: ComponentFixture<OwnSubscriptionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OwnSubscriptionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OwnSubscriptionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
