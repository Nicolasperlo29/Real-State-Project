export interface Message {
    content: string;
    senderId: number;
    receiverId: number;
    sentAt: Date;
    read: boolean;
}
