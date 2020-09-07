import {MatDialogConfig} from '@angular/material/dialog';
import {AlertDialogComponent} from '../alert-dialog/alert-dialog.component';
import {SharesDialogComponent} from '../shares-dialog/shares-dialog.component';
import {TaskActionDialogComponent} from '../task-action-dialog/task-action-dialog.component';
import {UserActionDialogComponent} from '../admin-panel/user-action-dialog/user-action-dialog.component';
import {NewSubscriptionDialogComponent} from '../task/new-subscription-dialog/new-subscription-dialog.component';
import {EndRequestDialogComponent} from '../task/end-request-dialog/end-request-dialog.component';
import {LogoutAlertDialogComponent} from '../settings/logout-alert-dialog/logout-alert-dialog.component';

export class DialogConfigFactory {

  static for(dialog: any, withData?: any): MatDialogConfig {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = withData;
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;

    switch(dialog) {
      case LogoutAlertDialogComponent:
        dialogConfig.width = '415px';
        dialogConfig.height = 'auto';
        break;

      case AlertDialogComponent:
        dialogConfig.height = '215px';
        dialogConfig.width = '415px';
        break;

      case UserActionDialogComponent:
      case SharesDialogComponent:
      case TaskActionDialogComponent:
        dialogConfig.height = 'auto';
        dialogConfig.width = '80%';
        dialogConfig.maxWidth = '700px';
        break;

      case EndRequestDialogComponent:
      case NewSubscriptionDialogComponent:
        dialogConfig.height = 'auto';
        dialogConfig.width = '80%';
        dialogConfig.maxWidth = '550px';
        break;
    }

    return dialogConfig;

  }

}
