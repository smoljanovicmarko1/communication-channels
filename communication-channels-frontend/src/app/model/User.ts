import { Role } from './Role';
import { Post } from './Post';

export class User {
  public id: number;
  public username: string;
  public password: string;
  public firstName: string;
  public lastName: string;
  public dateCreated: Date;
  public email: string;
  public phone: string;
  public role: Role;
  public favorites: Post[] = [];

  public profilePicture;

  getAuthorities(): string[] {
    return this.role.userPermissions.map((value) => value.name);
  }
}
