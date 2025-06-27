package shop.dodream.book.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.dodream.book.service.BookDocumentUpdater;
import shop.dodream.book.util.BookDocumentUpdater;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookDocumentUpdaterImpl implements BookDocumentUpdater {

    @Override
    public void updateFields(Long bookId, Map<String, Object> fieldMap) {
        
    }
}
