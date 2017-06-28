import { Component } from '@angular/core';
import { IonicPage, NavController, ModalController } from 'ionic-angular';
import { EventoAddPage } from '../evento-add/evento-add';
import { EventoInfoPage } from '../evento-info/evento-info';
import { FirebaseListObservable, AngularFireDatabase } from 'angularfire2/database';
import * as firebase from 'firebase';


/**
 * Generated class for the EventosPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@Component({
  selector: 'page-eventos',
  templateUrl: 'eventos.html',
})
export class EventosPage {

	eventos: FirebaseListObservable<any>;
  imagesUrl: Array<string> = [];

  constructor(public navCtrl: NavController,
          public modalCtrl: ModalController, 
          public database:AngularFireDatabase) {
            this.eventos = this.database.list('/Eventos');
            this.eventos.subscribe( eventos => {
              eventos.map((evento, index) => {
                const storageRef = firebase.storage().ref().child(evento.photo);
                storageRef.getDownloadURL().then(url => {
                  console.log(index);
                  this.imagesUrl[index] = url;
                });
              });  
            });

    }

  ionViewDidLoad() {
    console.log('ionViewDidLoad EventosPage');
  }

  getImage(index:number):string{
     return this.imagesUrl.length > 0 ? this.imagesUrl[index] : '';
  }

  add() {
    this.navCtrl.push(EventoAddPage);
  }

   presentRecipeModal(evento) {
    let eventoModal = this.modalCtrl.create(EventoInfoPage, {
      evento: evento
    });
    eventoModal.present();
  }


}
