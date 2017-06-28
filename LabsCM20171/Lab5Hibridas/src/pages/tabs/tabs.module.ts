import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { TabsPage } from './tabs';
import { PerfilPage } from '../perfil/perfil';

@NgModule({
  declarations: [
    TabsPage,
    PerfilPage,
  ],
  imports: [
    IonicPageModule.forChild(TabsPage),
  ]
})
export class TabsPageModule {}
