package shop.dodream.book.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dodream.book.dto.BookWithTagRequest;
import shop.dodream.book.dto.BookWithTagResponse;
import shop.dodream.book.dto.BookWithTagsResponse;
import shop.dodream.book.dto.TagResponse;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookTag;
import shop.dodream.book.entity.BookTagId;
import shop.dodream.book.entity.Tag;
import shop.dodream.book.exception.BookNotFoundException;
import shop.dodream.book.exception.BookTagDuplicateException;
import shop.dodream.book.exception.BookTagNotFoundException;
import shop.dodream.book.exception.TagNotFoundException;
import shop.dodream.book.repository.BookRepository;
import shop.dodream.book.repository.BookTagRepository;
import shop.dodream.book.repository.TagRepository;
import shop.dodream.book.service.BookTagService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookTagServiceImpl implements BookTagService {
    private final BookTagRepository bookTagRepository;
    private final BookRepository bookRepository;
    private final TagRepository tagRepository;

    @Override @Transactional
    public BookWithTagResponse registerTag(Long bookId, Long tagId){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));

        if (bookTagRepository.existsByBookIdAndTagId(bookId, tagId)) {
            throw new BookTagDuplicateException(bookId, tagId);
        }

        BookTag bookTag = new BookTag(book, tag);
        bookTagRepository.save(bookTag);

        return new BookWithTagResponse(book.getId(), new TagResponse(tag));
    }

    @Override @Transactional(readOnly = true)
    public BookWithTagsResponse getTagsByBook(Long bookId){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        List<Tag> tags = tagRepository.findAllByBookId(bookId);

        List<TagResponse> tagResponses = tags.stream()
                .map(TagResponse::new)
                .toList();

        return new BookWithTagsResponse(book.getId(), tagResponses);
    }

    @Override @Transactional
    public BookWithTagResponse updateTagByBook(Long bookId, Long tagId, BookWithTagRequest request){
        Long newTagId = request.getTagId();

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));
        Tag newTag = tagRepository.findById(newTagId)
                .orElseThrow(() -> new TagNotFoundException(newTagId));

        BookTag bookTag = bookTagRepository.findByBookIdAndTagId(bookId, tagId)
                .orElseThrow(() -> new BookTagNotFoundException(bookId, tagId));
        bookTagRepository.delete(bookTag);

        boolean exists = bookTagRepository.existsByBookIdAndTagId(bookId, newTagId);
        if (exists) {
            throw new BookTagDuplicateException(bookId, newTagId);
        }

        bookTagRepository.save(new BookTag(book, newTag));

        return new BookWithTagResponse(book.getId(), new TagResponse(newTag));


    }

    @Override @Transactional
    public void deleteTagByBook(Long bookId, Long tagId){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));

        BookTagId bookTagId = new BookTagId(bookId, tagId);
        BookTag bookTag = bookTagRepository.findById(bookTagId)
                .orElseThrow(()-> new BookTagNotFoundException(bookId, tagId));
        bookTagRepository.delete(bookTag);
    }
}
