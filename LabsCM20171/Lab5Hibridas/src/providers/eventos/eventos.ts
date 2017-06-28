import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

/*
  Generated class for the EventosProvider provider.

  See https://angular.io/docs/ts/latest/guide/dependency-injection.html
  for more info on providers and Angular 2 DI.
*/
export class Evento {
  constructor(
    public id?: number,
    public foto?: string,
    public nombre?: string,
    public descripcion?: string,
    public puntuacion?: number,
    public responsable?: string,
    public fecha?: string,
    public latitud?: number,
    public longitd?: number,
    public informacionGeneral?: string
  ) { }
}

const EVENTO: Evento[] = [];
const FETCH_LATENCY = 500;


let event1: Evento = new Evento(1, 'R.drawable.patinaje_artstico', 'Patinaje Artistico', 'Campeonato Mundial Junior 2017', 3, 'Taiwan', '15/03/2017', 25.04776, 121.53185, 'Este campeonato es el segundo en importancia tras los Juegos Olimpicos de Invierno y el Campeonato mundial de patinaje artístico tanto en tamanho como importancia.');
let event2: Evento = new Evento(2, 'R.drawable.tirodeportivo', 'Tiro Deportivo', 'Copa del Mundo de Shotgun', 4, 'Mexico', '19/03/2017', 16.86336, -99.8901, 'La ciudad de Acapulco será la sede de la Copa del Mundo de Shotgun 2017 de la ISSF');
let event3: Evento = new Evento(3, 'R.drawable.australia', 'Formula 1', 'Gran Premio de Australia', 3, 'Alemania', '26/03/2017', -27, 133, 'El Gran Premio de Australia de 2017 será la primera carrera de la temporada 2017 de Fórmula 1, que se disputara en el Circuito de Albert Park, en Melbourne (Australia).');
let event4: Evento = new Evento(4, 'R.drawable.hockey', 'Patinaje sobre hielo', 'Campeonato Mundial femenino 2017', 5, 'EEUU', '31/03/2017', 38, -97, 'Es la maxima competición internacional entre selecciones nacionales de hockey sobre hielo.');
let event5: Evento = new Evento(5, 'R.drawable.balonmano', 'Balonmano', 'XXV Campeonato Mundial de Balonmano Femenino', 2, 'Alemania', '01/12/2017', 51, 9, 'Un total de veinticuatro selecciones nacionales de cuatro confederaciones continentales competiran por el titulo de campeón mundial, cuyo actual portador es el equipo de Noruega, vencedor del Mundial de 2015.');

EVENTO.push(event1);
EVENTO.push(event2);
EVENTO.push(event3);
EVENTO.push(event4);
EVENTO.push(event5);


@Injectable()
export class EventosProvider {

/*
  constructor(public http: Http) {
    console.log('Hello EventosProvider Provider');
  }*/

  constructor() {
    console.log('Hello RecipesProvider Provider');
  }

  addEvento(evento): void {
    let eventoId = EVENTO.reduce((max, evento) => Math.max(evento.id, max), -1) + 1;
    evento.id = eventoId;
    EVENTO.push(evento);
  }

  getEventos(): Promise<Evento[]> {
    return new Promise<Evento[]>(resolve => {
      setTimeout(() => {
        resolve(EVENTO);
      }, FETCH_LATENCY);
    })
  }

  getEvento(id: number | string): Promise<Evento> {
    return new Promise<Evento>(resolve => {
      setTimeout(() => {
        resolve(EVENTO.filter(evento => evento.id === +id)[0]);
      }, FETCH_LATENCY);
    })
  }

  removeEvento(id: number | string): void {
    let eventoIdx = +id - 1;
    EVENTO.splice(eventoIdx, 1);
  }

}
