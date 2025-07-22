package shop.dodream.book.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dodream.book.dto.TagResponse;
import shop.dodream.book.entity.Tag;
import shop.dodream.book.exception.TagNotFoundException;
import shop.dodream.book.repository.TagRepository;
import shop.dodream.book.service.TagService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override @Transactional
    public TagResponse createTag(String newTagName){
        Tag tag = new Tag();
        tag.setTagName(newTagName);
        Tag savedtag = tagRepository.save(tag);
        return new TagResponse(savedtag);
    }

    @Override @Transactional(readOnly = true)
    public TagResponse getTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));
        return new TagResponse(tag);
    }

    @Override @Transactional(readOnly = true)
    public List<TagResponse> getTags(){
        return tagRepository.findAll()
                .stream()
                .map(TagResponse::new)
                .toList();
    }


    @Override @Transactional
    public TagResponse updateTag(Long tagId, String newTagName){
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));
        tag.setTagName(newTagName);

        Tag savedtag = tagRepository.save(tag);
        return new TagResponse(savedtag);
    }

    @Override @Transactional
    public void deleteTag(Long tagId){
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));
        tagRepository.delete(tag);
    }
}
