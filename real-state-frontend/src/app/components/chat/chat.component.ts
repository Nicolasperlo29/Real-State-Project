import { AfterViewChecked, ChangeDetectorRef, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';
import { MessageSocketService } from '../../services/message-socket.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Message } from '../../interfaces/message';
import { User } from '../../interfaces/user';
import { UsuariosService } from '../../services/usuarios.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.css'
})
export class ChatComponent implements OnInit, OnDestroy, AfterViewChecked {
  @ViewChild('chatBox') private chatBox!: ElementRef;

  usuarios: User[] = [];
  messages: Message[] = [];
  newMessage = '';
  receiverId = 0;
  selectedUser: User | null = null;
  isTyping = false;
  private sub!: Subscription;
  private shouldScrollToBottom = false;

  constructor(
    public messageService: MessageSocketService,
    private usuariosService: UsuariosService,
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
  ) { }

  ngOnInit(): void {
    this.loadUsers();
    this.messageService.connect();

    this.sub = this.messageService.messages$.subscribe({
      next: (msg) => {
        // Solo agregar si el mensaje es relevante para el chat actual
        const currentUserId = this.messageService.getUserId();
        if ((msg.senderId === this.receiverId && msg.receiverId === currentUserId) ||
          (msg.senderId === currentUserId && msg.receiverId === this.receiverId)) {
          this.messages.push(msg);
          this.shouldScrollToBottom = true;
          this.cdr.detectChanges();
        }
      }
    });
  }

  ngAfterViewChecked(): void {
    if (this.shouldScrollToBottom) {
      this.scrollToBottom();
      this.shouldScrollToBottom = false;
    }
  }

  private scrollToBottom(): void {
    try {
      if (this.chatBox) {
        this.chatBox.nativeElement.scrollTop = this.chatBox.nativeElement.scrollHeight;
      }
    } catch (err) {
      console.error('Error al hacer scroll:', err);
    }
  }

  loadUsers() {
    const tryLoad = () => {
      const currentUserId = this.messageService.getUserId();

      if (!currentUserId) {
        // Esperar un poco y volver a intentar
        setTimeout(tryLoad, 100);
        return;
      }

      this.usuariosService.getAllUsers().subscribe({
        next: (data) => {
          this.usuarios = data.filter(u => u.id !== currentUserId);
          console.log('Usuarios:', this.usuarios);
        },
        error: (err) => console.error('Error al cargar usuarios:', err)
      });
    };

    tryLoad();
  }

  seleccionar(user: User) {
    this.receiverId = user.id;
    this.selectedUser = user;
    this.loadChatHistory();
  }

  loadChatHistory() {
    const currentUserId = this.messageService.getUserId();
    if (!currentUserId) return;

    this.http.get<Message[]>(`http://localhost:8084/api/messages/${currentUserId}`)
      .subscribe({
        next: (data) => {
          this.messages = data.filter(
            m =>
              (m.senderId === currentUserId && m.receiverId === this.receiverId) ||
              (m.senderId === this.receiverId && m.receiverId === currentUserId)
          );
          this.shouldScrollToBottom = true;
        }
      });
  }

  send() {
    if (!this.newMessage.trim() || !this.receiverId) return;

    this.messageService.sendMessage(this.newMessage, this.receiverId);
    this.newMessage = '';
    this.shouldScrollToBottom = true;
  }

  get filteredMessages(): Message[] {
    const currentUserId = this.messageService.getUserId();
    if (!this.receiverId) return [];
    return this.messages.filter(
      m => (m.senderId === this.receiverId && m.receiverId === currentUserId) ||
        (m.senderId === currentUserId && m.receiverId === this.receiverId)
    );
  }

  getMessageTime(timestamp?: string | Date): string {
    if (!timestamp) return '';
    const date = new Date(timestamp);
    return date.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
    this.messageService.disconnect();
  }
}
