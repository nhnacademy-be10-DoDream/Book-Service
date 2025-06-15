package shop.dodream.book.service.impl;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.dodream.book.config.BookMapper;
import shop.dodream.book.config.NaverBookProperties;
import shop.dodream.book.dto.*;
import shop.dodream.book.dto.projection.BookListProjection;
import shop.dodream.book.dto.projection.UserBookDetailProjection;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookStatus;
import shop.dodream.book.exception.*;
import shop.dodream.book.infra.client.NaverBookClient;
import shop.dodream.book.infra.dto.NaverBookResponse;
import shop.dodream.book.repository.BookRepository;
import shop.dodream.book.service.BookService;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final NaverBookClient naverBookClient;
    private final BookRepository bookRepository;
    private final NaverBookProperties properties;
    private final BookMapper bookMapper;


    @Override
    @Transactional
    public BookRegisterResponse registerBookByIsbn(BookRegisterRequest request) {
        // 외부 api 호출
        NaverBookResponse naverBookResponse = naverBookClient.searchBook(properties.getClientId(), properties.getClientSecret(), request.getIsbn());
        NaverBookResponse.Item item = naverBookResponse.getItems().get(0);

        // 발행일 문자열 -> localDate 로 변환
        String pubdate = item.getPubdate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.parse(pubdate, formatter);

        // 판매가 (할인가) 문자열 -> Long 타입변환
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
        book.setSearchCount(0L); // 직접등록 초기값 검색수 0
        book.setViewCount(0L); // 직접 등록 초기값 조회수 0
        book.setBookCount(50L); //직접 등록 초기값 수량 50
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


    // 프로젝션으로 처리
    @Override
    @Transactional(readOnly = true)
    public List<BookListProjection> getAllBooks() {

        return bookRepository.findAllBy();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminBookDetailResponse getBookByIdForAdmin(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookIdNotFoundException("해당하는 아이디의 책은 존재하지않습니다."));
        AdminBookDetailResponse adminBookDetailResponse = new AdminBookDetailResponse();
        adminBookDetailResponse.setId(book.getId());
        adminBookDetailResponse.setTitle(book.getTitle());
        adminBookDetailResponse.setAuthor(book.getAuthor());
        adminBookDetailResponse.setDescription(book.getDescription());
        adminBookDetailResponse.setPublisher(book.getPublisher());
        adminBookDetailResponse.setIsbn(book.getIsbn());
        adminBookDetailResponse.setPublishedAt(book.getPublishedAt());
        adminBookDetailResponse.setStatus(book.getStatus());
        adminBookDetailResponse.setSalePrice(book.getSalePrice());
        adminBookDetailResponse.setRegularPrice(book.getRegularPrice());
        adminBookDetailResponse.setIsGiftable(book.getIsGiftable());
        adminBookDetailResponse.setViewCount(book.getViewCount());
        adminBookDetailResponse.setSearchCount(book.getSearchCount());
        adminBookDetailResponse.setCreatedAt(book.getCreatedAt());
        adminBookDetailResponse.setBookCount(book.getBookCount());
        adminBookDetailResponse.setBookUrl(book.getBookUrl());
        adminBookDetailResponse.setDiscountRate(book.getDiscountRate());

        return adminBookDetailResponse;
    }


    // 사용자 페이지에서 보여줄 북 정보 상세 조회
    @Override
    @Transactional(readOnly = true)
    public UserBookDetailProjection getBookByIdForUser(Long bookId) {
        return bookRepository.findBookDetailForUserById(bookId).orElseThrow(()-> new BookIdNotFoundException("해당 하는 아이디의 책은 존재하지 않습니다."));

    }


    @Override
    @Transactional
    public void updateBook(Long bookId, BookUpdateRequest request) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookIdNotFoundException("해당하는 아이디의 책은 존재하지않습니다."));

        // 삭제된 책은 수정 불가
        if (book.getStatus() == BookStatus.REMOVED){
            throw new BookAlreadyRemovedException("삭제된 도서는 수정할수 없습니다.");
        }

        // MapStruct로 기본 필드 매핑 널값은 기존값 유지
        bookMapper.updateBookFromDto(request, book);


        // 정가 or 할인가가 변경 되엇으면 할인율 재계산
        if (request.getRegularPrice() != null || request.getSalePrice() != null){
            Long regularPrice = request.getRegularPrice() != null ? request.getRegularPrice() : book.getRegularPrice();
            Long salePrice = request.getSalePrice() != null ? request.getSalePrice() : book.getSalePrice();

            if (salePrice > regularPrice) {
                throw new InvalidDiscountPriceException("할인가가 정가보다 높을 순 없습니다."); // 커스텀 예외 던짐
            }
            if (regularPrice != null && regularPrice != 0){
                long discountRate = Math.round((1 - (double) salePrice / regularPrice) * 100);
                book.setDiscountRate(discountRate);
            }
        }

        // 상태 자동갱신( 관리자가 직접 상태를 조작 x 수량에 따른 자동으로 상태변경)
        updateStatusByBookCount(book);
        bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new BookIdNotFoundException("해당 하는 아이디의 책은 존재하지 않습니다."));
        book.setStatus(BookStatus.REMOVED);
        bookRepository.save(book);

    }

    @Override
    @Transactional
    public BookCountDecreaseResponse decreaseBookCount(BookCountDecreaseRequest request) {
        Book book = bookRepository.findById(request.getBookId()).orElseThrow(()-> new BookIdNotFoundException("해당 하는 아이디의 책은 존재하지 않습니다."));
        if (book.getStatus() != BookStatus.SELL){
            throw new BookNotOrderableException("해당 도서는 주문할 수없는 상태입니다.");
        }

        Long currentStock = book.getBookCount();
        if (currentStock < request.getBookCount()){
            throw new BookCountNotEnoughException("재고가 부족합니다. 현재 재고: "+currentStock);
        }

        // 차감
        book.setBookCount(currentStock-request.getBookCount());

        updateStatusByBookCount(book);

        BookCountDecreaseResponse decreaseResponse = new BookCountDecreaseResponse(book.getId(), book.getBookCount(), book.getStatus() == BookStatus.SELL);
        return decreaseResponse;

    }

    // 수량에 따른 자동 삭제변경 삭제는 건들지 x
    private void updateStatusByBookCount(Book book) {
        if (book.getStatus() != BookStatus.REMOVED) {
            long count = book.getBookCount();
            if (count == 0) {
                book.setStatus(BookStatus.SOLD_OUT);
            } else if (count <= 5) {
                book.setStatus(BookStatus.LOW_STOCK);
            } else {
                book.setStatus(BookStatus.SELL);
            }
        }
    }




}
