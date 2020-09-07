import {Component, OnDestroy} from '@angular/core';
import {User} from './_models/user';
import {Subscription} from 'rxjs';
import {Router} from '@angular/router';
import {AuthenticationService} from './_services/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnDestroy {
  currentUser: User;
  userSub: Subscription;

  constructor(
    private router: Router,
    public authenticationService: AuthenticationService
  ) {
    this.userSub = this.authenticationService.currentUser.subscribe(x => this.currentUser = x);

  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.userSub.unsubscribe();
  }
}
