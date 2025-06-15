package shop.dodream.book.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dodream.book.dto.BookLikeResponse;
import shop.dodream.book.dto.BookListResponse;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookLike;
import shop.dodream.book.entity.BookStatus;
import shop.dodream.book.exception.BookAlreadyRemovedException;
import shop.dodream.book.exception.BookIdNotFoundException;
import shop.dodream.book.exception.BookLikeAlreadyRegisterException;
import shop.dodream.book.repository.BookLikeRepository;
import shop.dodream.book.repository.BookRepository;
import shop.dodream.book.service.BookLikeService;

import java.util.List;


@Service
public class BookLikeServiceImpl implements BookLikeService {


    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookLikeRepository bookLikeRepository;


    @Override
    @Transactional
    public void registerBookLike(Long bookId, String userId) {

        if (bookLikeRepository.existsByBookIdAndUserId(bookId,userId)) {
            throw new BookLikeAlreadyRegisterException("이미 좋아요를 눌른 도서입니다.");
        }

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookIdNotFoundException("해당하는 아이디의 책은 존재하지않습니다."));

        BookLike bookLike = new BookLike();
        bookLike.setUserId(userId);
        bookLike.setBook(book);

        bookLikeRepository.save(bookLike);

    }


    @Override
    @Transactional(readOnly = true)
    public BookLikeResponse bookLikeFindMe(Long bookId, String userId) {
        bookRepository.findById(bookId).orElseThrow(() -> new BookIdNotFoundException("해당하는 아이디의 책은 존재하지않습니다."));

        Boolean bookLiked = bookLikeRepository.existsByBookIdAndUserId(bookId,userId);

        return new BookLikeResponse(bookLiked);

    }

    @Override
    @Transactional
    public void bookLikeDelete(Long bookId, String userId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookIdNotFoundException("해당하는 아이디의 책은 존재하지않습니다."));

        if (book.getStatus() == BookStatus.REMOVED){
            throw new BookAlreadyRemovedException("삭제된 도서에 대해서는 좋아요를 취소할수 없습니다.");
        }

        BookLike bookLike = bookLikeRepository.findByBookIdAndUserId(bookId, userId).orElseThrow(()-> new BookIdNotFoundException("좋아요 기록이 없습니다."));


        bookLikeRepository.delete(bookLike);
    }


    @Override
    public List<BookListResponse> getLikedBooksByUserId(String userId) {

        return bookLikeRepository.findLikedBooksByUserId(userId);
    }
}
