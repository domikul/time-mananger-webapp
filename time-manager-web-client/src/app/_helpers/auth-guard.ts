import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import { AuthenticationService } from '../_services/authentication.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {

    const currentUser = this.authenticationService.currentUserValue;

    if (this.authenticationService.isUserLoggedIn) {

      if(!currentUser.active && state.url !== '/not-activated') {
        this.router.navigate(['/not-activated']);
        return false;
      }
      else if(currentUser.active && state.url === '/not-activated') {
        this.router.navigate(['']);
        return false;
      }

      if(state.url === '/admin-panel' && currentUser.roleId !== 1) {
        this.router.navigate([''], {skipLocationChange: true});
        return false;
      }

      return true;
    }



    // not logged in so redirect to login page with the return url
    this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }


}
