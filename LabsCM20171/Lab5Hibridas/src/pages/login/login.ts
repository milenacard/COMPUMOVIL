import { Component, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { FirebaseListObservable, AngularFireDatabase } from 'angularfire2/database';
import { EventosPage } from '../eventos/eventos';
import { RegisterPage } from '../registro/registro';

import 'rxjs/add/operator/toPromise';


@Component({
  selector: 'page-login',
  templateUrl: 'login.html',
})
export class LoginPage {
  public password:string = '';
  public email:string = '';

  constructor(public navCtrl: NavController, public database:AngularFireDatabase) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad LoginPage');
  }

  loginUser():void{
    this.database.list('/Usuarios').subscribe( usuarios => {
        usuarios.map((value, index) => {
          if(value.email ==  this.email && value.password == this.password){
            this.navCtrl.push(EventosPage);
          }
        });
        console.log('no encontrado');
    });
    
  }

  goToSignup():void{
    this.navCtrl.push(RegisterPage);
  }
}


