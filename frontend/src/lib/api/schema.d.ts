/**
 * This file was auto-generated by openapi-typescript.
 * Do not make direct changes to the file.
 */

export interface paths {
    "/api/listings/{id}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get: operations["getListing"];
        put: operations["updateListing"];
        post?: never;
        delete: operations["deleteListing"];
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/api/categories/{id}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get: operations["getCategory"];
        put: operations["updateCategory"];
        post?: never;
        delete: operations["deleteCategory"];
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/api/listings": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get: operations["getPaginatedAndSortedListings"];
        put?: never;
        post: operations["createListing"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/api/chat/read/{listingId}/{otherUserId}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        post: operations["markMessagesAsRead"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/api/categories": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get: operations["listAllCategories"];
        put?: never;
        post: operations["createCategory"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/api/bookmarks": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get: operations["listAllBookmarksForUser"];
        put?: never;
        post: operations["createBookmark"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/api/auth/register": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        post: operations["register"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/api/auth/login": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        post: operations["login"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/api/chat/conversations": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get: operations["getConversations"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/api/chat/conversation/{listingId}/{otherUserId}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get: operations["getConversation"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/api/auth/ping": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get: operations["ping"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/api/auth/me": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get: operations["me"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/api/bookmarks/{id}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        post?: never;
        delete: operations["deleteBookmark"];
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
}
export type webhooks = Record<string, never>;
export interface components {
    schemas: {
        CreateOrUpdateListingRequestDto: {
            title: string;
            /** Format: int64 */
            categoryId: number;
            /** @enum {string} */
            condition: "NEW" | "LIKE_NEW" | "VERY_GOOD" | "GOOD" | "ACCEPTABLE";
            /** Format: double */
            price: number;
            /** Format: double */
            originalPrice?: number;
            description: string;
            modelYear?: string;
            manufacturer?: string;
            model?: string;
            serialNumber?: string;
            purchaseDate?: string;
            usageDuration?: string;
            defects?: string[];
            modifications?: string[];
            reasonForSelling?: string;
            images?: string[];
        };
        AddressDto: {
            streetName: string;
            streetNumber: string;
            postalCode: string;
            city: string;
            country: string;
        };
        ListingDto: {
            /** Format: int64 */
            id: number;
            title: string;
            /** Format: int64 */
            categoryId: number;
            /** @enum {string} */
            condition: "NEW" | "LIKE_NEW" | "VERY_GOOD" | "GOOD" | "ACCEPTABLE";
            /** Format: double */
            price: number;
            /** Format: double */
            originalPrice?: number;
            description: string;
            seller: components["schemas"]["UserDto"];
            modelYear?: string;
            manufacturer?: string;
            model?: string;
            serialNumber?: string;
            purchaseDate?: string;
            usageDuration?: string;
            defects: string[];
            modifications: string[];
            reasonForSelling?: string;
            /** Format: date-time */
            createdAt: string;
            images: string[];
        };
        UserDto: {
            /** Format: int64 */
            id: number;
            firstName: string;
            lastName: string;
            profileImageUrl?: string;
            address?: components["schemas"]["AddressDto"];
        };
        CreateOrUpdateCategoryRequestDto: {
            name: string;
            description: string;
            translationString: string;
            icon: string;
        };
        CategoryDto: {
            /** Format: int64 */
            id: number;
            name: string;
            description: string;
            translationString: string;
            icon: string;
        };
        CreateListingBookmarkRequestDto: {
            /** Format: int64 */
            listingId: number;
        };
        ListingBookmarkResponseDto: {
            /** Format: int64 */
            id: number;
            listing: components["schemas"]["ListingDto"];
        };
        RegisterRequestDto: {
            firstName: string;
            lastName: string;
            email: string;
            password: string;
            phoneNumber: string;
        };
        AuthResponseDto: {
            accessToken: string;
            message: string;
        };
        LoginRequestDto: {
            email: string;
            password: string;
        };
        PageListingDto: {
            /** Format: int64 */
            totalElements?: number;
            /** Format: int32 */
            totalPages?: number;
            first?: boolean;
            last?: boolean;
            /** Format: int32 */
            size?: number;
            content?: components["schemas"]["ListingDto"][];
            /** Format: int32 */
            number?: number;
            sort?: components["schemas"]["SortObject"];
            /** Format: int32 */
            numberOfElements?: number;
            pageable?: components["schemas"]["PageableObject"];
            empty?: boolean;
        };
        PageableObject: {
            /** Format: int64 */
            offset?: number;
            sort?: components["schemas"]["SortObject"];
            paged?: boolean;
            /** Format: int32 */
            pageNumber?: number;
            /** Format: int32 */
            pageSize?: number;
            unpaged?: boolean;
        };
        SortObject: {
            empty?: boolean;
            sorted?: boolean;
            unsorted?: boolean;
        };
        ConversationSummaryDto: {
            user: components["schemas"]["UserDto"];
            /** Format: int64 */
            listingId: number;
            listingTitle: string;
            /** Format: int64 */
            unreadCount: number;
            lastMessage?: components["schemas"]["LastMessageDto"];
        };
        LastMessageDto: {
            content: string;
            /** Format: date-time */
            timestamp: string;
            isFromCurrentUser: boolean;
        };
        PageConversationSummaryDto: {
            /** Format: int64 */
            totalElements?: number;
            /** Format: int32 */
            totalPages?: number;
            first?: boolean;
            last?: boolean;
            /** Format: int32 */
            size?: number;
            content?: components["schemas"]["ConversationSummaryDto"][];
            /** Format: int32 */
            number?: number;
            sort?: components["schemas"]["SortObject"];
            /** Format: int32 */
            numberOfElements?: number;
            pageable?: components["schemas"]["PageableObject"];
            empty?: boolean;
        };
        ChatMessageDto: {
            sender: components["schemas"]["UserDto"];
            recipient: components["schemas"]["UserDto"];
            /** Format: int64 */
            listingId: number;
            content: string;
            /** Format: date-time */
            timestamp: string;
            read: boolean;
        };
        PageChatMessageDto: {
            /** Format: int64 */
            totalElements?: number;
            /** Format: int32 */
            totalPages?: number;
            first?: boolean;
            last?: boolean;
            /** Format: int32 */
            size?: number;
            content?: components["schemas"]["ChatMessageDto"][];
            /** Format: int32 */
            number?: number;
            sort?: components["schemas"]["SortObject"];
            /** Format: int32 */
            numberOfElements?: number;
            pageable?: components["schemas"]["PageableObject"];
            empty?: boolean;
        };
        FullUserDto: {
            /** Format: int64 */
            id: number;
            firstName: string;
            lastName: string;
            email: string;
            phoneNumber: string;
            profileImageUrl?: string;
            address?: components["schemas"]["AddressDto"];
        };
    };
    responses: never;
    parameters: never;
    requestBodies: never;
    headers: never;
    pathItems: never;
}
export type $defs = Record<string, never>;
export interface operations {
    getListing: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "*/*": components["schemas"]["ListingDto"];
                };
            };
        };
    };
    updateListing: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: number;
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["CreateOrUpdateListingRequestDto"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "*/*": components["schemas"]["ListingDto"];
                };
            };
        };
    };
    deleteListing: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description No Content */
            204: {
                headers: {
                    [name: string]: unknown;
                };
                content?: never;
            };
        };
    };
    getCategory: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "*/*": components["schemas"]["CategoryDto"];
                };
            };
        };
    };
    updateCategory: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: number;
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["CreateOrUpdateCategoryRequestDto"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "*/*": components["schemas"]["CategoryDto"];
                };
            };
        };
    };
    deleteCategory: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description No Content */
            204: {
                headers: {
                    [name: string]: unknown;
                };
                content?: never;
            };
        };
    };
    getPaginatedAndSortedListings: {
        parameters: {
            query?: {
                page?: number;
                size?: number;
                sortBy?: string;
                direction?: "ASC" | "DESC";
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "*/*": components["schemas"]["PageListingDto"];
                };
            };
        };
    };
    createListing: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["CreateOrUpdateListingRequestDto"];
            };
        };
        responses: {
            /** @description Created */
            201: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "*/*": components["schemas"]["ListingDto"];
                };
            };
        };
    };
    markMessagesAsRead: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                listingId: number;
                otherUserId: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content?: never;
            };
        };
    };
    listAllCategories: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "*/*": components["schemas"]["CategoryDto"][];
                };
            };
        };
    };
    createCategory: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["CreateOrUpdateCategoryRequestDto"];
            };
        };
        responses: {
            /** @description Created */
            201: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "*/*": components["schemas"]["CategoryDto"];
                };
            };
        };
    };
    listAllBookmarksForUser: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "*/*": components["schemas"]["ListingBookmarkResponseDto"][];
                };
            };
        };
    };
    createBookmark: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["CreateListingBookmarkRequestDto"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "*/*": components["schemas"]["ListingBookmarkResponseDto"];
                };
            };
        };
    };
    register: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["RegisterRequestDto"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "*/*": components["schemas"]["AuthResponseDto"];
                };
            };
        };
    };
    login: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["LoginRequestDto"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "*/*": components["schemas"]["AuthResponseDto"];
                };
            };
        };
    };
    getConversations: {
        parameters: {
            query?: {
                page?: number;
                size?: number;
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "*/*": components["schemas"]["PageConversationSummaryDto"];
                };
            };
        };
    };
    getConversation: {
        parameters: {
            query?: {
                page?: number;
                size?: number;
            };
            header?: never;
            path: {
                listingId: number;
                otherUserId: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "*/*": components["schemas"]["PageChatMessageDto"];
                };
            };
        };
    };
    ping: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "*/*": string;
                };
            };
        };
    };
    me: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "*/*": components["schemas"]["FullUserDto"];
                };
            };
        };
    };
    deleteBookmark: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content?: never;
            };
        };
    };
}
