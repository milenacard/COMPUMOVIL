import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { EventoAddPage } from './evento-add';

@NgModule({
  declarations: [
    EventoAddPage,
  ],
  imports: [
    IonicPageModule.forChild(EventoAddPage),
  ],
  exports: [
    EventoAddPage
  ]
})
export class EventoAddPageModule {}
