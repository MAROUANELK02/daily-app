import {User} from "./user.model";

export class Task {
  taskId!: number;
  title!: string;
  description!: string;
  status!: string;
  priority!: string;
  createdAt!: string;
  updatedAt!: string;
  userDTO!: User;
  dropdownOpen: boolean = false;
}
