import { Routes } from '@angular/router';
import { InicioSesion } from './inicio-sesion/inicio-sesion';
import { PanelPrincipal } from './panel-principal/panel-principal';
import { ListaNoticias } from './noticias/lista-noticias/lista-noticias';
import { EditorNoticias } from './noticias/editor-noticias/editor-noticias';
import { ListaHoroscopos } from './horoscopos/lista-horoscopos/lista-horoscopos';
import { EditorHoroscopos } from './horoscopos/editor-horoscopos/editor-horoscopos';
import { ListaUsuarios } from './usuarios/lista-usuarios/lista-usuarios';
import { ComentariosNoticias } from './noticias/comentarios-noticias/comentarios-noticias';
import { LecturaNoticias } from './noticias/lectura-noticias/lectura-noticias';
import { LecturaHoroscopo } from './horoscopos/lectura-horoscopos/lectura-horoscopos';

export const routes: Routes = [
  { path: '', redirectTo: 'inicio-sesion', pathMatch: 'full' },
  { path: 'inicio-sesion', component: InicioSesion },
  { path: 'panel-principal', component: PanelPrincipal },
  { path: 'noticias', component: ListaNoticias },
  { path: 'editor-noticias', component: EditorNoticias },
  { path: 'horoscopos', component: ListaHoroscopos },
  { path: 'editor-horoscopos', component: EditorHoroscopos },
  { path: 'usuarios', component: ListaUsuarios },
  { path: 'comentarios', component: ComentariosNoticias },

  // CORREGIDO: Se asignó la clase en plural al componente
  { path: 'lectura-noticia', component: LecturaNoticias },
  { path: 'lectura-horoscopo', component: LecturaHoroscopo },
  { path: '**', redirectTo: 'inicio-sesion' }
];
