import { Component, Inject } from '@angular/core';
import { IonicPage, NavController, ViewController, NavParams } from 'ionic-angular';
import { Evento, EventosProvider } from '../../providers/eventos/eventos';
import * as firebase from 'firebase';


@Component({
  selector: 'page-evento-info',
  templateUrl: 'evento-info.html',
})
export class EventoInfoPage {

	evento:any;
  image:string = '';

  constructor(public navCtrl: NavController, 
  	public navParams: NavParams,
  	public viewCtrl: ViewController) {      
        this.evento = this.navParams.get("evento"); 
        const storageRef = firebase.storage().ref().child(this.evento.photo);
        storageRef.getDownloadURL().then(url => this.image = url);
     }

  ionViewDidLoad() {
    console.log('ionViewDidLoad EventoInfoPage');
  }

  closeModal() {
    this.viewCtrl.dismiss();
  }

}
