import { Property } from "./property";

export interface FavoritePropertyDTO {
    propertyId: number;
    property?: Property | null;
}
