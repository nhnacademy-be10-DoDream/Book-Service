package shop.dodream.book.config;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import shop.dodream.book.dto.BookUpdateRequest;
import shop.dodream.book.entity.Book;
// nullValuePropertyMappingStrategy = IGNORE -> DTO 에서 null인 필드 값은 엔티티에 매핑하지 않음 즉 덮어쓰지 x
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {

    void updateBookFromDto(BookUpdateRequest request, @MappingTarget Book book);
}
