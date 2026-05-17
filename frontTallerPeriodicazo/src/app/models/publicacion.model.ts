export interface Publicacion {
  id?: number;
  tipo: string;
  titulo: string;
  contenido: string;
  autor?: string;
  editorial?: string;
  fecha?: string;
  categoria?: string;
  signo?: string;
  elemento?: string;
}
