import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import $ from 'jquery';

@Injectable({
  providedIn: 'root'
})

export class WebSocketAPIService {

  webSocketEndPoint:string = "server/websocket"
  client: any;
  constructor() {
   }

  connect() {
      let ws = new SockJS(this.webSocketEndPoint);
      this.client = Stomp.over(ws);
      let that = this;
      this.client.connect({}, function(frame) {
        that.client.subscribe("/topic/alert/" + sessionStorage.getItem("idUser"), (message) =>{
          that.onMessageReceived(message);
        });
      });
    }

    disconnect() {
           if (this.client !== null) {
               this.client.disconnect();
           }
           console.log("Disconnected");
       }

    onMessageReceived(message) {
          console.log("Message Recieved from Server :: " + message);
          alert(JSON.stringify(message.body));
      }
}
