import { FavoriteProperty } from "./favorite-property";

export interface UserWithFavorites {
    id: number;
    fullName: string;
    email: string;
    phone?: string;
    profileImageUrl?: string;
    favorites: FavoriteProperty[];
}
