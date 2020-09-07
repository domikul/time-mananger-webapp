import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-not-activated',
  template: `
    <p>
      You are not activated. Ask administrators to activate You.
    </p>
  `,
  styles: [
    `p {
      font-size: 20pt;
      text-align: center;
      margin-top: 50px;
    }`
  ]
})
export class NotActivatedComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
