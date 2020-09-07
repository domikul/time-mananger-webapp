import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HomeComponent} from './home/home.component';
import {LoginComponent} from './login/login.component';
import {AuthGuard} from './_helpers/auth-guard';
import {CommonModule} from '@angular/common';
import {AdminPanelComponent} from './admin-panel/admin-panel.component';
import {SubscriptionsComponent} from './subscriptions/subscriptions.component';
import {NotActivatedComponent} from './not-activated/not-activated.component';
import {SettingsComponent} from './settings/settings.component';


const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'admin-panel', component: AdminPanelComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'subscriptions', component: SubscriptionsComponent, canActivate: [AuthGuard] },
  { path: 'not-activated', component: NotActivatedComponent, canActivate: [AuthGuard] },
  { path: 'settings', component: SettingsComponent, canActivate: [AuthGuard] },

  // otherwise redirect to home
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes), CommonModule],
  exports: [RouterModule]
})
export class AppRoutingModule { }
