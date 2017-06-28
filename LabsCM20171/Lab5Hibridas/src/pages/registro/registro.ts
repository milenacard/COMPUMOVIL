import { Component } from '@angular/core';
import { FirebaseListObservable, AngularFireDatabase } from 'angularfire2/database';
import { NavController } from 'ionic-angular';
import { EventosPage } from '../eventos/eventos';

@Component({
    selector: 'register-user',
    templateUrl: 'registro.html'
})
export class RegisterPage{
    email:string = '';
    password:string = '';
    listUsers: FirebaseListObservable<any>;

    constructor(private database:AngularFireDatabase, private navCtrl: NavController){
        this.listUsers = this.database.list('/Usuarios');
    }

    registerUser(){
        this.listUsers.push({
            password: this.password,
            email: this.email
        }).then( () => {
            this.navCtrl.push(EventosPage);
        }).catch( () => {
            console.log("Error Guardando");
        });
    }


}