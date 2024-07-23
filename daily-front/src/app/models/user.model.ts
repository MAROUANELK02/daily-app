export class User {
  userId!: number;
  firstname!: string;
  lastname!: string;
  email!: string;
  username!: string;
  password!: string;
  jobTitle!: string;
  image!: string;
  tasksCount!: number;
  createdAt!: string;
  updatedAt!: string;
  roleDTOS!: RoleDtos[];
}

export class RoleDtos {
  roleId!: number;
  roleName!: string;
}
