import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OwnBucketsComponent } from './own-buckets.component';

describe('OwnBucketsComponent', () => {
  let component: OwnBucketsComponent;
  let fixture: ComponentFixture<OwnBucketsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OwnBucketsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OwnBucketsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
