package shop.dodream.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dodream.book.dto.BookResponse;
import shop.dodream.book.dto.CategoryRequest;
import shop.dodream.book.dto.TagRequest;
import shop.dodream.book.dto.TagResponse;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.Tag;
import shop.dodream.book.exception.TagIdNotFoundException;
import shop.dodream.book.exception.TagNameIsNullException;
import shop.dodream.book.repository.BookTagRepository;
import shop.dodream.book.repository.TagRepository;
import shop.dodream.service.TagService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final BookTagRepository bookTagRepository;

    @Override @Transactional
    public TagResponse createTag(TagRequest request){
        if(request.getTagName() == null || request.getTagName().isEmpty()){
            throw new TagNameIsNullException("생성하고자 하는 태그 이름이 비어 있습니다.");
        }
        Tag tag = new Tag();
        tag.setTagName(request.getTagName());
        Tag savedtag = tagRepository.save(tag);
        return new TagResponse(savedtag);
    }

    @Override @Transactional(readOnly = true)
    public List<TagResponse> getTags(){
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(TagResponse::new)
                .collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public List<TagResponse> getTagsByBook(Long bookId){
        List<Tag> tags = bookTagRepository.findTagsByBook(bookId);
        return tags.stream()
                .map(TagResponse::new)
                .collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public List<BookResponse> getBooksByTag(Long tagId){
        List<Book> books = bookTagRepository.findBooksByTag(tagId);
        return books.stream()
                .map(BookResponse::new)
                .collect(Collectors.toList());
    }

    @Override @Transactional
    public TagResponse updateTag(Long tagId, TagRequest request){
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagIdNotFoundException(tagId, " 라는 태그 아이디를 찾을 수 없습니다."));
        tag.setTagName(request.getTagName());

        Tag savedtag = tagRepository.save(tag);
        return new TagResponse(savedtag);
    }

    @Override @Transactional
    public void deleteTag(Long tagId){
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagIdNotFoundException(tagId, " 라는 태그 아이디를 찾을 수 없습니다."));
        tagRepository.delete(tag);
    }
}
