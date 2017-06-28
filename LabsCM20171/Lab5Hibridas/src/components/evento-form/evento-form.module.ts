import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { EventoFormComponent } from './evento-form';

@NgModule({
  declarations: [
    EventoFormComponent,
  ],
  imports: [
    IonicPageModule.forChild(EventoFormComponent),
  ],
  exports: [
    EventoFormComponent
  ]
})
export class EventoFormComponentModule {}
