import { Injectable } from '@angular/core';
import { Client, IMessage, Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Subject } from 'rxjs';
import { UserService } from './user.service';
import { Message } from '../interfaces/message';

@Injectable({ providedIn: 'root' })
export class MessageSocketService {
  private stompClient!: Client;
  private messagesSubject = new Subject<Message>();
  messages$ = this.messagesSubject.asObservable();
  private userId!: number;

  constructor(private userService: UserService) { }

  connect(): void {
    this.userService.getProfile().subscribe({
      next: (data) => {
        this.userId = data.id;

        const socket = new SockJS('http://localhost:8084/ws');
        this.stompClient = new Client({
          webSocketFactory: () => socket,
          debug: () => { },
          onConnect: () => {
            console.log('✅ Conectado al WebSocket con id:', this.userId);

            // Suscribirse a los mensajes dirigidos a este usuario
            this.stompClient.subscribe(
              `/user/${this.userId}/queue/messages`,
              (msg: IMessage) => {
                const message: Message = JSON.parse(msg.body);
                this.messagesSubject.next(message); // emite todos los mensajes
              }
            );
          },
          onStompError: (frame) => {
            console.error('❌ Error STOMP:', frame.headers['message'], frame.body);
          }
        });

        this.stompClient.activate();
      }
    });
  }

  sendMessage(content: string, receiverId: number): void {
    if (!this.stompClient?.connected || !this.userId) return;

    const msg: Message = {
      senderId: this.userId,
      receiverId,
      content,
      sentAt: new Date(),
      read: false
    };

    // enviar al backend
    this.stompClient.publish({
      destination: '/app/send',
      body: JSON.stringify(msg)
    });

    // mostrar inmediatamente
    this.messagesSubject.next(msg);
  }

  getUserId(): number {
    return this.userId;
  }

  disconnect(): void {
    this.stompClient?.deactivate();
  }
}
