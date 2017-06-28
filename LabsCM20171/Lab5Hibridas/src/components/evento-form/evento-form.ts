import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Evento, EventosProvider } from '../../providers/eventos/eventos';

/**
 * Generated class for the EventoFormComponent component.
 *
 * See https://angular.io/docs/ts/latest/api/core/index/ComponentMetadata-class.html
 * for more info on Angular Components.
 */
@Component({
  selector: 'evento-form',
  templateUrl: 'evento-form.html'
})
export class EventoFormComponent {

  @Input() evento: Evento;
  @Input() isUpdate: boolean;
  @Output() fireAction: EventEmitter<Evento> = new EventEmitter<Evento>();
  submitBtn: string;

  constructor() {
    console.log('Hello EventoFormComponent Component');
  }

   ngOnInit() {
    this.submitBtn = this.isUpdate ? "Update" : "Save";
  }

  processRecipe(): void {
    this.fireAction.emit(this.evento);
  }

}
