// import { Injectable } from '@angular/core';
// import { Client, IMessage, Message, Stomp } from '@stomp/stompjs';
// import SockJS from 'sockjs-client';
// import { Subject } from 'rxjs';

// @Injectable({
//   providedIn: 'root'
// })
// export class ChatService {
//   private stompClient: Client;
//   private messages: any[] = [];

//   constructor() {
//     this.stompClient = new Client({
//       webSocketFactory: () => new SockJS('http://localhost:8084/ws'),
//     });

//     this.stompClient.onConnect = () => {
//       this.stompClient.subscribe('/topic/messages', (message: Message) => {
//         this.messages.push(JSON.parse(message.body));
//       });
//     };

//     this.stompClient.activate();
//   }

//   sendMessage(senderId: number, receiverId: number, content: string) {
//     this.stompClient.publish({
//       destination: '/app/send',
//       body: JSON.stringify({ senderId, receiverId, content }),
//     });
//   }

//   getMessages() {
//     return this.messages;
//   }
// }
