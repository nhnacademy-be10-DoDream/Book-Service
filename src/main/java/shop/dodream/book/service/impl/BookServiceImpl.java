package shop.dodream.book.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.dodream.book.config.NaverBookProperties;
import shop.dodream.book.dto.BookRegisterRequest;
import shop.dodream.book.dto.BookRegisterResponse;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookStatus;
import shop.dodream.book.infra.client.NaverBookClient;
import shop.dodream.book.infra.dto.NaverBookResponse;
import shop.dodream.book.repository.BookRepository;
import shop.dodream.book.service.BookService;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final NaverBookClient naverBookClient;
    private final BookRepository bookRepository;
    private final NaverBookProperties properties;


    @Override
    public BookRegisterResponse registerBookByIsbn(BookRegisterRequest request) {
        // 외부 api 호출
        NaverBookResponse naverBookResponse = naverBookClient.searchBook(properties.getClientId(), properties.getClientSecret(), request.getIsbn());
        NaverBookResponse.Item item = naverBookResponse.getItems().get(0);

        // 발행일 문자열 -> localDate 로 변환
        String pubdate = item.getPubdate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.parse(pubdate, formatter);

        // 판매가 (할인가)
        long salePrice = Long.parseLong(item.getDiscount());

        // 정가 계산 (할인율을 직접등록)
        long discountRate = request.getDiscountRate();
        long regularPrice = (discountRate == 0) ? salePrice : Math.round(salePrice * 100.0 / (100 - discountRate));



        Book book = new Book();
        book.setTitle(item.getTitle());
        book.setDescription(request.getDescription()); // // 직접등록 WYSIWYG 에디터 값
        book.setAuthor(removeSpecialChars(item.getAuthor()));
        book.setPublisher(item.getPublisher());
        book.setPublishedAt(localDate);
        book.setIsbn(item.getIsbn());
        book.setRegularPrice(regularPrice); // 할인율을 이용해 직접등록
        book.setStatus(BookStatus.SELL); // 직접등록
        book.setSalePrice(salePrice);
        book.setIsGiftable(true); //직접등록
        book.setCreatedAt(ZonedDateTime.now());
        book.setSearchCount(0); // 직접등록 초기값 검색수 0
        book.setViewCount(0); // 직접 등록 초기값 조회수 0
        book.setBookCount(50); //직접 등록 초기값 수량 50
        book.setBookUrl(item.getImage());
        book.setDiscountRate(discountRate); // 직접등록

        Book savedBook = bookRepository.save(book);

        BookRegisterResponse bookRegisterResponse = new BookRegisterResponse(
                savedBook.getId(),
                savedBook.getTitle(),
                savedBook.getDescription(),
                savedBook.getAuthor(),
                savedBook.getPublisher(),
                savedBook.getPublishedAt(),
                savedBook.getIsbn(),
                savedBook.getRegularPrice(),
                savedBook.getStatus(),
                savedBook.getSalePrice(),
                savedBook.getIsGiftable(),
                savedBook.getCreatedAt(),
                savedBook.getBookCount(),
                savedBook.getDiscountRate()
        );

        return bookRegisterResponse;


    }

    private String removeSpecialChars(String input) {
        if (input == null) return null;
        return input.replaceAll("[^가-힣a-zA-Z0-9\\s]", " "); //한글, 영어 소문자 및 대문자, 숫자, 공백 을 제외한 나머진 공백 으로 대체
    }
}
