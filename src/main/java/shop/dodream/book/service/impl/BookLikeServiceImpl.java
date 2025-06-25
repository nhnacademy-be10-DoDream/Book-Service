package shop.dodream.book.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dodream.book.dto.BookListResponse;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookLike;
import shop.dodream.book.entity.BookStatus;
import shop.dodream.book.exception.BookAlreadyRemovedException;
import shop.dodream.book.exception.BookLikeAlreadyRegisterException;
import shop.dodream.book.exception.BookLikeNotFoundException;
import shop.dodream.book.exception.BookNotFoundException;
import shop.dodream.book.repository.BookLikeRepository;
import shop.dodream.book.repository.BookRepository;
import shop.dodream.book.service.BookLikeService;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BookLikeServiceImpl implements BookLikeService {


    private final BookRepository bookRepository;
    private final BookLikeRepository bookLikeRepository;



    @Override
    @Transactional
    public void registerBookLike(Long bookId, String userId) {

        if (bookLikeRepository.existsByBookIdAndUserId(bookId,userId)) {
            throw new BookLikeAlreadyRegisterException();
        }

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));

        BookLike bookLike = new BookLike();
        bookLike.setUserId(userId);
        bookLike.setBook(book);

        bookLikeRepository.save(bookLike);
//        bookRepository.incrementLikCount(bookId);


    }


    @Override
    @Transactional(readOnly = true)
    public Boolean bookLikeFindMe(Long bookId, String userId) {
        bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));

        Boolean bookLiked = bookLikeRepository.existsByBookIdAndUserId(bookId,userId);

        return bookLiked;

    }

    @Override
    @Transactional
    public void bookLikeDelete(Long bookId, String userId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));

        if (book.getStatus() == BookStatus.REMOVED){
            throw new BookAlreadyRemovedException();
        }

        BookLike bookLike = bookLikeRepository.findByBookIdAndUserId(bookId, userId).orElseThrow(()-> new BookLikeNotFoundException(bookId,userId));


        bookLikeRepository.delete(bookLike);
//        bookRepository.decreaseLikeCount(bookId);


    }


    @Override
    @Transactional(readOnly = true)
    public List<BookListResponse> getLikedBooksByUserId(String userId) {

        return bookLikeRepository.findLikedBooksByUserId(userId);
    }


    @Override
    @Transactional(readOnly = true)
    public Long getBookLikeCount(Long bookId) {
        return bookLikeRepository.countByBookId(bookId);
    }
}
