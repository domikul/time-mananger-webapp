export class User {

  idUser: number;

  email: string;

  password: string;

  firstName: string;

  lastName: string;

  roleId: number;

  positionId: number;

  active: boolean;

  static makeCopy(user: User): User {
    const copied = new User();
    copied.idUser = user.idUser;
    copied.email = user.email;
    copied.password = '';
    copied.firstName = user.firstName;
    copied.lastName = user.lastName;
    copied.roleId = user.roleId;
    copied.positionId = user.positionId;
    copied.active = user.active;
    return copied;
  }
}
