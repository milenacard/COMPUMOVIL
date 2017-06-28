import { BrowserModule } from '@angular/platform-browser';
import { ErrorHandler, NgModule } from '@angular/core';
import { FormsModule }   from '@angular/forms';
import { IonicApp, IonicErrorHandler, IonicModule} from 'ionic-angular';
import { SplashScreen } from '@ionic-native/splash-screen';
import { StatusBar } from '@ionic-native/status-bar';

import { MyApp } from './app.component';
import { HomePage } from '../pages/home/home';
import { TabsPage } from '../pages/tabs/tabs';
import { EventoAddPage } from '../pages/evento-add/evento-add';
import { EventoInfoPage } from '../pages/evento-info/evento-info';
import { EventosPage } from '../pages/eventos/eventos';
import { RegisterPage } from '../pages/registro/registro';

import { LoginPage } from '../pages/login/login';
import { EventosProvider } from '../providers/eventos/eventos';
import { EventoFormComponent } from '../components/evento-form/evento-form';
import { LogoutPage } from '../pages/logout/logout';
import { AngularFireDatabaseModule } from 'angularfire2/database';
import { AngularFireModule } from 'angularfire2';

export const firebaseConfig= {
      apiKey: "AIzaSyAoG-HKballZspVLRwC6kqyyVG2PbfMT2I",
      authDomain: "lab4fcm-5259d.firebaseapp.com",
      databaseURL: "https://lab4fcm-5259d.firebaseio.com",
      projectId: "lab4fcm-5259d",
      storageBucket: "lab4fcm-5259d.appspot.com",
      messagingSenderId: "544362254856"
      };

@NgModule({
  declarations: [
    MyApp,
    HomePage,
    TabsPage,
    EventoFormComponent,
    EventoAddPage,
    EventoInfoPage,
    EventosPage,
    LoginPage,
    RegisterPage
    //LogoutPage
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AngularFireDatabaseModule,
    AngularFireModule.initializeApp(firebaseConfig),
    IonicModule.forRoot(MyApp),
    
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    HomePage,
    TabsPage,
    EventoAddPage,
    EventoInfoPage,
    LoginPage,
    EventosPage,
    RegisterPage
    //LogoutPage
  ],
  providers: [
    StatusBar,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    EventosProvider
  ]
})
export class AppModule {}
