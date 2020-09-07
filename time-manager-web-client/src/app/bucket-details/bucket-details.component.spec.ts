import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BucketDetailsComponent } from './bucket-details.component';

describe('BucketDetailsComponent', () => {
  let component: BucketDetailsComponent;
  let fixture: ComponentFixture<BucketDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BucketDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BucketDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
