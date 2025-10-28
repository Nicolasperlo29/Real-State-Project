import { PropertyImage } from "./property-image";

export interface Property {
    id: number;
    title: string;
    description: string;
    city: string;
    address: string;
    price: number;
    areaCubierta: number;
    userId: number;
    areaTotal: number;
    rooms: number;
    bathrooms: number;
    type: string;
    status: string;
    latitude: number;
    longitude: number;
    images: PropertyImage[];
    currentImageIndex?: number;
}
