import {Observable} from 'rxjs';
import {Share} from '../_models/share';
import {User} from '../_models/user';

export interface ShareableService {

  getShares(shareableId: number): Observable<Share[]>;

  addNewShares(users: { idUser: number }[], shareableId: number): Observable<Share[]>;

  deleteShare(userId: number, shareableId: number): Observable<any>;

}
