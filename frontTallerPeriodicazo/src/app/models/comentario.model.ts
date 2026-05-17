export interface Comentario {
  id?: number;
  contenido: string;
  nombreComentador?: string;
  fecha?: string;
  publicacionId: number;
  nombrePublicacion?: string;
}
