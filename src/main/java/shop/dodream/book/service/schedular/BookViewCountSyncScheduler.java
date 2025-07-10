package shop.dodream.book.service.schedular;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import shop.dodream.book.repository.BookRepository;
import shop.dodream.book.service.BookDocumentUpdater;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookViewCountSyncScheduler {

    private final RedisTemplate<String, Long> redisTemplate;
    private final BookRepository bookRepository;
    private final BookDocumentUpdater bookDocumentUpdater;

    //5분마다 주기적으로 실행
    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void syncViewCounts(){
        Set<String> keys = redisTemplate.keys("viewCount:book:*");

        if (keys.isEmpty()){
            return;
        }

        for (String key: keys){
            try {
                String bookIdStr = key.replace("viewCount:book:", "");
                Long bookId = Long.valueOf(bookIdStr);
                Long increment = redisTemplate.opsForValue().get(key);

                if (increment != null && increment>0){
                    bookRepository.incrementViewCount(bookId, increment);

                    bookDocumentUpdater.incrementViewCount(bookId, increment);

                    redisTemplate.delete(key);

                    log.info("조회수 동기화 완료 - bookId={}, increment={}", bookId, increment);
                }


            }catch (Exception e){
                log.error("조회수 동기화 실패 - key={}", key, e);
            }
        }

    }

}

