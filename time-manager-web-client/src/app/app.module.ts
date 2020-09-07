import { BrowserModule } from '@angular/platform-browser';
import {LOCALE_ID, NgModule} from '@angular/core';

import { AppComponent } from './app.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {AuthInterceptor} from './_helpers/auth-interceptor';
import {AppRoutingModule} from './app-routing.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatTabsModule} from '@angular/material/tabs';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {LoginComponent} from './login/login.component';
import {HomeComponent} from './home/home.component';
import {MatCardModule} from '@angular/material/card';
import {FlexLayoutModule} from '@angular/flex-layout';
import {MatDialogModule} from '@angular/material/dialog';
import { OwnBucketsComponent } from './own-buckets/own-buckets.component';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import { BucketDetailsComponent } from './bucket-details/bucket-details.component';
import { BucketListComponent } from './bucket-list/bucket-list.component';
import { SharedBucketsComponent } from './shared-buckets/shared-buckets.component';
import { NewBucketDialogComponent } from './new-bucket-dialog/new-bucket-dialog.component';
import { AlertDialogComponent } from './alert-dialog/alert-dialog.component';
import {MatIconModule} from '@angular/material/icon';
import {TaskListComponent} from './task-list/task-list.component';
import {SharedTasksComponent} from './shared-tasks/shared-tasks.component';
import {TaskComponent} from './task/task.component';
import {TimerComponent} from './task/timer/timer.component';
import {TimeStringPipe} from './_helpers/time-string.pipe';
import {SpacedCapitalsPipe} from './_helpers/spaced-capitals.pipe';
import { TaskActionDialogComponent } from './task-action-dialog/task-action-dialog.component';
import {HttpErrorInterceptor} from './_helpers/error.interceptor';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatSelectModule} from '@angular/material/select';
import { SharesDialogComponent } from './shares-dialog/shares-dialog.component';
import { AdminPanelComponent } from './admin-panel/admin-panel.component';
import { UserComponent } from './admin-panel/user/user.component';
import {HistoryComponent} from "./history/history.component";
import {MatTableModule} from "@angular/material/table";
import { UserActionDialogComponent } from './admin-panel/user-action-dialog/user-action-dialog.component';
import {MatCheckboxModule} from '@angular/material/checkbox';
import { SubscriptionsComponent } from './subscriptions/subscriptions.component';
import { OwnSubscriptionsComponent } from './subscriptions/own-subscriptions/own-subscriptions.component';
import { SubscriptionsListComponent } from './subscriptions/subscriptions-list/subscriptions-list.component';
import { GivenSubscriptionsComponent } from './subscriptions/given-subscriptions/given-subscriptions.component';
import { NewSubscriptionDialogComponent } from './task/new-subscription-dialog/new-subscription-dialog.component';
import { EmailsComponent } from './subscriptions/emails/emails.component';
import { EndRequestDialogComponent } from './task/end-request-dialog/end-request-dialog.component';
import { NotActivatedComponent } from './not-activated/not-activated.component';
import { SettingsComponent } from './settings/settings.component';
import { LogoutAlertDialogComponent } from './settings/logout-alert-dialog/logout-alert-dialog.component';
import { UsersListComponent } from './admin-panel/users-list/users-list.component';
import { InactiveBucketsComponent } from './admin-panel/inactive-buckets/inactive-buckets.component';
import {MatExpansionModule} from '@angular/material/expansion';
import { InactiveUserHolderComponent } from './admin-panel/inactive-buckets/inactive-user-holder/inactive-user-holder.component';
import {MatProgressBarModule} from "@angular/material/progress-bar";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    OwnBucketsComponent,
    BucketDetailsComponent,
    BucketListComponent,
    SharedBucketsComponent,
    NewBucketDialogComponent,
    AlertDialogComponent,
    TaskListComponent,
    SharedTasksComponent,
    TaskComponent,
    TimerComponent,
    TimeStringPipe,
    SpacedCapitalsPipe,
    TaskActionDialogComponent,
    SharesDialogComponent,
    AdminPanelComponent,
    UserComponent,
    HistoryComponent,
    UserActionDialogComponent,
    SubscriptionsComponent,
    OwnSubscriptionsComponent,
    SubscriptionsListComponent,
    GivenSubscriptionsComponent,
    NewSubscriptionDialogComponent,
    EmailsComponent,
    EndRequestDialogComponent,
    NotActivatedComponent,
    SettingsComponent,
    LogoutAlertDialogComponent,
    UsersListComponent,
    InactiveBucketsComponent,
    InactiveUserHolderComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        ReactiveFormsModule,
        FormsModule,
        HttpClientModule,
        BrowserAnimationsModule,
        MatToolbarModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        FlexLayoutModule,
        MatTabsModule,
        MatButtonToggleModule,
        MatDialogModule,
        MatIconModule,
        MatSnackBarModule,
        MatSelectModule,
        MatTableModule,
        MatCheckboxModule,
        MatProgressBarModule,
        MatExpansionModule
    ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
