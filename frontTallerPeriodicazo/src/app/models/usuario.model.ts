export interface Usuario {
  id?: number;
  username: string;
  password?: string;
  role: string;
  accountNonExpired?: boolean;
  accountNonLocked?: boolean;
  credentialsNonExpired?: boolean;
  enabled?: boolean;
}
