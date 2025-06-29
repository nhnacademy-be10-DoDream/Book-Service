package shop.dodream.book.dto;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;

public enum BookSortType {
    POPULARITY,
    RECENT,
    LOW_PRICE,
    HIGH_PRICE,
    RATING,
    REVIEW,
    NONE;

    public SortOptions toSortOption(){
        return switch (this) {
            case POPULARITY -> new SortOptions.Builder().field(f -> f.field("viewCount").order(SortOrder.Desc)).build();
            case RECENT     -> new SortOptions.Builder().field(f -> f.field("publishedAt").order(SortOrder.Desc)).build();
            case LOW_PRICE  -> new SortOptions.Builder().field(f -> f.field("salePrice").order(SortOrder.Asc)).build();
            case HIGH_PRICE -> new SortOptions.Builder().field(f -> f.field("salePrice").order(SortOrder.Desc)).build();
            case RATING     -> new SortOptions.Builder().field(f -> f.field("ratingAvg").order(SortOrder.Desc)).build();
            case REVIEW     -> new SortOptions.Builder().field(f -> f.field("reviewCount").order(SortOrder.Desc)).build();
            case NONE       -> null;
        };
    }


}

