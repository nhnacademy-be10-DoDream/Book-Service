package shop.dodream.book.service;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import shop.dodream.book.core.event.BookImageDeleteEvent;
import shop.dodream.book.core.properties.AladdinBookProperties;
import shop.dodream.book.core.properties.MinIOProperties;
import shop.dodream.book.dto.*;
import shop.dodream.book.dto.projection.BookAdminListResponseRecord;
import shop.dodream.book.dto.projection.BookDetailResponse;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookStatus;
import shop.dodream.book.entity.Image;
import shop.dodream.book.exception.BookCountNotEnoughException;
import shop.dodream.book.exception.BookNotFoundException;
import shop.dodream.book.exception.BookNotOrderableException;
import shop.dodream.book.exception.DuplicateIsbnException;
import shop.dodream.book.infra.client.AladdinBookClient;
import shop.dodream.book.infra.dto.AladdinBookResponse;
import shop.dodream.book.repository.BookRepository;
import shop.dodream.book.service.impl.BookServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private AladdinBookClient aladdinBookClient;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private FileService fileService;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private AladdinBookProperties properties;
    @Mock
    private MinIOProperties minIOProperties;
    @Mock
    private RedisTemplate<String, Long> redisTemplate;
    @Mock
    private ValueOperations<String, Long> valueOperations;

    @Captor
    ArgumentCaptor<BookImageDeleteEvent> eventCaptor;


    private BookRegisterRequest createRequest() {
        return new BookRegisterRequest(
                "Effective Java", "자바 베스트 셀러", "조슈아 블로크", "인사이트",
                LocalDate.of(2018, 5, 1), "9788966261204",
                45000L, 40000L, true, 50L,
                "cover.jpg"
        );
    }

    private Book createBook() {
        Book book = new Book(
                "제목", "설명", "저자", "출판사",
                LocalDate.now(), "1234567890123", 10000L,
                BookStatus.SELL, 9000L, true, 0L, 100L
        );
        return book;
    }



    @Test
    @DisplayName("알라딘 책 조회 테스트")
    void getAladdinBookList_success() {
        // given

        when(properties.getTtbkey()).thenReturn("dummy-key");

        AladdinBookResponse.Item item = new AladdinBookResponse.Item();
        item.setTitle("Effective Java");
        item.setDescription("자바 프로그래밍 모범 사례를 담은 책");
        item.setAuthor("조슈아 블로크");
        item.setPublisher("인사이트");
        item.setIsbn13("9788966261204");
        item.setPubDate(LocalDate.of(2018, 5, 1));
        item.setPriceStandard(45000L);
        item.setPriceSales(40000L);
        item.setCover("cover.jpg");

        AladdinBookResponse mockResponse = new AladdinBookResponse();
        mockResponse.setErrorCode(null);
        mockResponse.setErrorMessage(null);
        mockResponse.setTotalResults(1);
        mockResponse.setItem(List.of(item));

        when(aladdinBookClient.searchBook(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(mockResponse);


        // when
        AladdinBookSearchResult result = bookService.getAladdinBookList("java", 10, 1);

        // then
        assertEquals(1, result.getTotalResults());
        assertFalse(result.getItems().isEmpty());

        BookItemResponse bookItem = result.getItems().getFirst();
        assertEquals("Effective Java", bookItem.getTitle());
        assertEquals("조슈아 블로크", bookItem.getAuthor());
        assertEquals("9788966261204", bookItem.getIsbn());
    }

    @Test
    @DisplayName("registerFromAladdin - 성공")
    void registerFromAladdin_success() {
        // given
        BookRegisterRequest request = createRequest();
        when(bookRepository.existsByIsbn(request.getIsbn())).thenReturn(false);
        when(fileService.uploadBookImageFromUrl(request.getImageUrl()))
                .thenReturn("uploaded/image.jpg");

        // when
        bookService.registerFromAladdin(request);

        // then
        verify(bookRepository).save(any(Book.class));
        verify(eventPublisher, never()).publishEvent(any());
    }



    @Test
    @DisplayName("registerFromAladdin - 저장 중 예외 발생 시 이미지 삭제 이벤트 발생")
    void registerFromAladdin_exception_ImageDeleteEvent() {
        // given
        BookRegisterRequest request = createRequest();

        when(bookRepository.existsByIsbn("9788966261204")).thenReturn(false);
        when(fileService.uploadBookImageFromUrl("cover.jpg"))
                .thenReturn("uploaded/image.jpg");
        doThrow(new RuntimeException("DB error")).when(bookRepository).save(any());

        // when
        assertThrows(RuntimeException.class, () -> bookService.registerFromAladdin(request));

        // then
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        BookImageDeleteEvent captured = eventCaptor.getValue();
        assertEquals(List.of("uploaded/image.jpg"), captured.deleteKeys());
    }

    @Test
    @DisplayName("registerBookDirect - 파일이 존재하는 경우")
    void registerBookDirect_success_withFiles() {
        // given
        BookRegisterRequest request = createRequest();

        MultipartFile mockFile = mock(MultipartFile.class);
        List<MultipartFile> files = List.of(mockFile);

        when(bookRepository.existsByIsbn(request.getIsbn())).thenReturn(false);
        when(fileService.uploadBookImageFromFiles(files)).thenReturn(List.of("uploaded1.jpg", "uploaded2.jpg"));

        // when
        bookService.registerBookDirect(request, files);

        // then
        verify(bookRepository).save(any(Book.class));
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("registerBookDirect - 파일이 없는 경우 (files == null) 기본 이미지 사용")
    void registerBookDirect_success_noFiles_nullList() {
        // given
        BookRegisterRequest request = createRequest();
        when(bookRepository.existsByIsbn(request.getIsbn())).thenReturn(false);
        when(minIOProperties.getDefaultImage()).thenReturn("default.jpg");
        when(fileService.uploadBookImageFromFiles(null)).thenReturn(List.of());

        // when
        bookService.registerBookDirect(request, null);

        // then
        verify(minIOProperties).getDefaultImage();
        verify(bookRepository).save(any(Book.class));
        verify(fileService).uploadBookImageFromFiles(null);
    }


    @Test
    @DisplayName("getAllBooks(Pageable) - 관리자용 페이징 조회")
    void getAllBooks_admin_success() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookAdminListResponseRecord> mockPage = new PageImpl<>(List.of(
                mock(BookAdminListResponseRecord.class)
        ));

        when(bookRepository.findAllBy(pageable)).thenReturn(mockPage);

        // when
        Page<BookAdminListResponseRecord> result = bookService.getAllBooks(pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(bookRepository).findAllBy(pageable);
    }

    @Test
    @DisplayName("getAllBooks() - 사용자용 전체 조회")
    void getAllBooks_user_success() {
        // given
        List<BookListResponseRecord> mockList = List.of(mock(BookListResponseRecord.class));
        when(bookRepository.findAllBy()).thenReturn(mockList);

        // when
        List<BookListResponseRecord> result = bookService.getAllBooks();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookRepository).findAllBy();
    }

    @Test
    @DisplayName("findAllByIds - ID 리스트로 조회")
    void findAllByIds_success() {
        // given
        List<Long> ids = List.of(1L, 2L, 3L);
        List<BookListResponseRecord> mockResult = List.of(mock(BookListResponseRecord.class));

        when(bookRepository.findVisibleBooksByIds(ids)).thenReturn(mockResult);

        // when
        List<BookListResponseRecord> result = bookService.findAllByIds(ids);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookRepository).findVisibleBooksByIds(ids);
    }

    @Test
    @DisplayName("getBookByIdForAdmin - 존재하지 않는 책 ID")
    void getBookByIdForAdmin_notFound() {
        Long bookId = 999L;

        when(bookRepository.findBookDetailForAdmin(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookByIdForAdmin(bookId));
        verify(bookRepository).findBookDetailForAdmin(bookId);
    }

    @Test
    @DisplayName("getBookByIdForUser - 성공")
    void getBookByIdForUser_success() {
        Long bookId = 2L;
        BookDetailResponse mockResponse = mock(BookDetailResponse.class);

        when(bookRepository.findBookDetailForUser(bookId)).thenReturn(Optional.of(mockResponse));

        BookDetailResponse result = bookService.getBookByIdForUser(bookId);

        assertNotNull(result);
        verify(bookRepository).findBookDetailForUser(bookId);
    }

    @Test
    @DisplayName("getBookByIdForUser - 존재하지 않는 책 ID")
    void getBookByIdForUser_notFound() {
        Long bookId = 888L;

        when(bookRepository.findBookDetailForUser(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookByIdForUser(bookId));
        verify(bookRepository).findBookDetailForUser(bookId);
    }

    @Test
    @DisplayName("updateBook - 이미지 변경 포함하여 책 정보 수정 성공")
    void updateBook_success_withImages() {
        // given
        Long bookId = 1L;
        Book book = spy(createBook());
        BookUpdateRequest request = mock(BookUpdateRequest.class);

        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        List<MultipartFile> files = List.of(file);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(fileService.uploadBookImageFromFiles(files)).thenReturn(List.of("img1.jpg", "img2.jpg"));
        when(minIOProperties.getDefaultImage()).thenReturn("default.jpg");

        book.addImages(List.of(
                new Image(book, "default.jpg", true),
                new Image(book, "old.jpg", false)
        ));

        // when
        bookService.updateBook(bookId, request, files);

        // then
        verify(fileService).uploadBookImageFromFiles(files);
        verify(bookRepository).findById(bookId);

        verify(eventPublisher).publishEvent(eventCaptor.capture());
        BookImageDeleteEvent captured = eventCaptor.getValue();
        assertEquals(List.of("old.jpg"), captured.deleteKeys());

        verify(book).updateTextFields(request);
        verify(book).updateStatusByBookCount();
    }

    @Test
    @DisplayName("deleteBook - 도서 삭제 시 이미지 삭제 이벤트 발행")
    void deleteBook_success(){
        Long bookId = 1L;
        Book book = spy(createBook());
        book.addImages(List.of(
                new Image(book, "img1.jpg", true),
                new Image(book, "img2.jpg", false)
        ));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        bookService.deleteBook(bookId);

        verify(bookRepository).findById(bookId);
        verify(book).markAsRemoved();

        verify(eventPublisher).publishEvent(eventCaptor.capture());
        BookImageDeleteEvent captured = eventCaptor.getValue();
        assertEquals(List.of("img1.jpg", "img2.jpg"), captured.deleteKeys());
    }

    @Test
    @DisplayName("deleteBook - 존재하지 않는 도서 삭제 시 예외 발생")
    void deleteBook_bookNotFound(){
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(bookId));
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("decreaseBookCount - 재고 차감 성공")
    void decreaseBookCount_success() {
        Long bookId = 1L;
        Book book = createBook();
        book.setBookCount(10L);

        ReflectionTestUtils.setField(book, "id", bookId);
        BookCountDecreaseRequest request = new BookCountDecreaseRequest(bookId, 3L);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        BookCountDecreaseResponse response = bookService.decreaseBookCount(request);

        assertEquals(bookId, response.getBookId());
        assertEquals(7L, response.getRemainBookCount());
        assertTrue(response.isOrderable());

    }

    @Test
    @DisplayName("decreaseBookCount - 재고 부족")
    void decreaseBookCount_stockNotEnough(){
        Long bookId = 1L;
        Book book = createBook();
        book.setBookCount(2L);

        ReflectionTestUtils.setField(book, "id", bookId);
        BookCountDecreaseRequest request = new BookCountDecreaseRequest(bookId, 5L);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        assertThrows(BookCountNotEnoughException.class, () -> bookService.decreaseBookCount(request));
    }

    @Test
    @DisplayName("decreaseBookCount - 판매 상태가 아니면 예외 발생")
    void decreaseBookCount_notOrderable() {
        Long bookId = 1L;
        Book book = spy(createBook());
        book.setBookCount(10L);

        book.markAsRemoved();
        ReflectionTestUtils.setField(book, "id", bookId);

        BookCountDecreaseRequest request = new BookCountDecreaseRequest(bookId, 3L);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        assertThrows(BookNotOrderableException.class, () -> bookService.decreaseBookCount(request));
    }



    @Test
    @DisplayName("increaseBookCount - 재고 증가 성공")
    void increaseBookCount_success(){
        Long bookId = 1L;
        Book book = createBook();
        book.setBookCount(5L);

        ReflectionTestUtils.setField(book, "id", bookId);
        BookCountIncreaseRequest request = new BookCountIncreaseRequest(bookId, 3L);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        bookService.increaseBookCount(request);

        assertEquals(8L, book.getBookCount());
    }




    @Test
    @DisplayName("getBookByIsbn - 존재하는 ISBN으로 책 정보 조회 성공")
    void getBookByIsbn_success() {
        // given
        String isbn = "1234567890123";
        Long bookId = 1L;

        BookItemResponse response = new BookItemResponse();
        response.setBookId(bookId);
        response.setIsbn(isbn);

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(response));

        // when
        BookItemResponse result = bookService.getBookByIsbn(isbn);

        // then
        assertEquals(isbn, result.getIsbn());
        assertEquals(bookId, result.getBookId());
        verify(bookRepository).findByIsbn(isbn);
    }


    @Test
    @DisplayName("getBookByIsbn - 존재하지 않는 ISBN으로 예외 발생")
    void getBookByIsbn_notFound(){
        String isbn = "not-exist";
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.getBookByIsbn(isbn));
        verify(bookRepository).findByIsbn(isbn);
    }

    @Test
    @DisplayName("redisIncreaseViewCount - Redis에 조회수 증가")
    void redisIncreaseViewCount_success(){
        Long bookId = 1L;
        String key = "viewCount:book:" + bookId;

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        bookService.redisIncreaseViewCount(bookId);

        verify(redisTemplate.opsForValue()).increment(key);
    }

    @Test
    @DisplayName("validateIsbnUnique - 중복 ISBN 시 예외 발생 (registerBookDirect 내부 검증)")
    void registerBookDirect_duplicationIsbn_exception(){
        String isbn = "duplicate-isbn";
        BookRegisterRequest request = mock(BookRegisterRequest.class);
        when(request.getIsbn()).thenReturn(isbn);
        when(bookRepository.existsByIsbn(isbn)).thenReturn(true);

        assertThrows(DuplicateIsbnException.class, () -> bookService.registerBookDirect(request, null));
        verify(bookRepository).existsByIsbn(isbn);
    }

}
