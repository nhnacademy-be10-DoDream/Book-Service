package shop.dodream.book.service.schedular;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import shop.dodream.book.repository.BookRepository;

import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.*;

class BookViewCountSyncSchedulerTest {

    @Mock
    private RedisTemplate<String, Long> redisTemplate;

    @Mock
    private ValueOperations<String, Long> valueOperations;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookViewCountSyncScheduler scheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testSyncViewCounts_shouldUpdateViewCount_whenKeyExistsInRedis() {

        String redisKey = "viewCount:book:42";
        Set<String> keys = Collections.singleton(redisKey);
        Long viewCountIncrement = 5L;

        when(redisTemplate.keys("viewCount:book:*")).thenReturn(keys);
        when(valueOperations.get(redisKey)).thenReturn(viewCountIncrement);

        scheduler.syncViewCounts();

        verify(bookRepository, times(1)).incrementViewCount(42L, 5L);
        verify(valueOperations, times(1)).decrement(redisKey, 5L);
    }

    @Test
    void testSyncViewCounts_shouldSkipWhenKeysEmpty() {
        when(redisTemplate.keys("viewCount:book:*")).thenReturn(Collections.emptySet());

        scheduler.syncViewCounts();

        verify(bookRepository, never()).incrementViewCount(any(), any());
    }

    @Test
    void testSyncViewCounts_shouldHandleInvalidKeyGracefully() {
        String invalidKey = "viewCount:book:invalid";
        Set<String> keys = Collections.singleton(invalidKey);

        when(redisTemplate.keys("viewCount:book:*")).thenReturn(keys);
        when(valueOperations.get(invalidKey)).thenReturn(3L);  // will throw NumberFormatException

        scheduler.syncViewCounts();

        verify(bookRepository, never()).incrementViewCount(any(), any());
    }
}
