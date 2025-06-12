package shop.dodream.book.config;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import shop.dodream.book.dto.BookUpdateRequest;
import shop.dodream.book.entity.Book;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {

    void updateBookFromDto(BookUpdateRequest request, @MappingTarget Book book);
}
