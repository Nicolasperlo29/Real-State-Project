export interface Appointment {
    clientId: number;
    propertyId?: number | null;
    type: 'CONSULTATION' | 'PROPERTY_VISIT';
    dateTime: string; // ISO string
    durationMinutes?: number;
}
