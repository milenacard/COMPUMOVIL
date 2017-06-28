import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { Evento, EventosProvider } from '../../providers/eventos/eventos';
import { EventosPage } from '../eventos/eventos';


/**
 * Generated class for the EventoAddPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@IonicPage()
@Component({
  selector: 'page-evento-add',
  templateUrl: 'evento-add.html',
})
export class EventoAddPage {

   evento: Evento = new Evento();

  constructor(public navCtrl: NavController, 
    private eventoService: EventosProvider) {
  }

   save(evento) {
    this.eventoService.addEvento(evento);
    this.navCtrl.push(EventosPage);
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad EventoAddPage');
  }

}
