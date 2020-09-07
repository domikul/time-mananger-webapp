import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedBucketsComponent } from './shared-buckets.component';

describe('SharedBucketsComponent', () => {
  let component: SharedBucketsComponent;
  let fixture: ComponentFixture<SharedBucketsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SharedBucketsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SharedBucketsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
