import {Routes} from '@angular/router';
import {InicioSesion} from './inicio-sesion/inicio-sesion';
import {PanelPrincipal} from './panel-principal/panel-principal';
import {ListaNoticias} from './noticias/lista-noticias/lista-noticias';
import {EditorNoticias} from './noticias/editor-noticias/editor-noticias';
import {ListaHoroscopos} from './horoscopos/lista-horoscopos/lista-horoscopos';
import {EditorHoroscopos} from './horoscopos/editor-horoscopos/editor-horoscopos';
import {ListaUsuarios} from './usuarios/lista-usuarios/lista-usuarios';
import {ComentariosNoticias} from './noticias/comentarios-noticias/comentarios-noticias';
import {LecturaNoticias} from './noticias/lectura-noticias/lectura-noticias';
import {LecturaHoroscopo} from './horoscopos/lectura-horoscopos/lectura-horoscopos';

import {roleGuard} from './guards/role.guard';

/**
 * Arreglo que define la configuración de enrutamiento principal de la aplicación.
 * Asocia rutas (URLs) con sus respectivos componentes y aplica controles de acceso (Guards)
 * basados en los roles de los usuarios mediante la propiedad `data`.
 * @type {Routes}
 */
export const routes: Routes = [
  {path: '', redirectTo: 'inicio-sesion', pathMatch: 'full'},
  {path: 'inicio-sesion', component: InicioSesion},

  {
    path: 'panel-principal',
    component: PanelPrincipal,
    canActivate: [roleGuard],
    data: {roles: ['ADMIN']}
  },
  {
    path: 'usuarios',
    component: ListaUsuarios,
    canActivate: [roleGuard],
    data: {roles: ['ADMIN']}
  },

  {
    path: 'editor-noticias',
    component: EditorNoticias,
    canActivate: [roleGuard],
    data: {roles: ['ADMIN', 'EDITOR']}
  },
  {
    path: 'editor-horoscopos',
    component: EditorHoroscopos,
    canActivate: [roleGuard],
    data: {roles: ['ADMIN', 'EDITOR']}
  },

  {
    path: 'noticias',
    component: ListaNoticias,
    canActivate: [roleGuard]
  },
  {
    path: 'horoscopos',
    component: ListaHoroscopos,
    canActivate: [roleGuard]
  },
  {
    path: 'comentarios',
    component: ComentariosNoticias,
    canActivate: [roleGuard]
  },
  {
    path: 'lectura-noticia',
    component: LecturaNoticias,
    canActivate: [roleGuard]
  },
  {
    path: 'lectura-horoscopo',
    component: LecturaHoroscopo,
    canActivate: [roleGuard]
  },

  {path: '**', redirectTo: 'inicio-sesion'}
];
