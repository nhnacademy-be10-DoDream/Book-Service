package shop.dodream.book.service.schedular;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dodream.book.repository.BookRepository;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookViewCountSyncScheduler {

    private final RedisTemplate<String, Long> redisTemplate;
    private final BookRepository bookRepository;

    @Scheduled(fixedDelay = 60 * 60 * 1000)
    @Transactional
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

                    redisTemplate.opsForValue().decrement(key, increment);

                    log.info("조회수 동기화 완료 - bookId={}, increment={}", bookId, increment);
                }


            }catch (Exception e){
                log.error("조회수 동기화 실패 - key={}", key, e);
            }
        }

    }

}

