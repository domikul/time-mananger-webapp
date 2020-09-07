import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SharesDialogComponent } from './shares-dialog.component';

describe('SharesDialogComponent', () => {
  let component: SharesDialogComponent;
  let fixture: ComponentFixture<SharesDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SharesDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SharesDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
