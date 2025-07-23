
INSERT INTO book
(book_id, title, description, author, publisher, published_at, isbn, regular_price, status, sale_price, is_giftable, view_count, book_count, discount_rate, created_at, updated_at)
VALUES
    (1, 'Spring Boot 완벽 가이드', 'Spring Boot를 완벽히 배우는 실전 가이드', '홍길동', '한빛미디어', '2024-01-10', '9781234567890', 30000, 'SELL', 25000, FALSE, 100, 50, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'JPA 입문', 'JPA의 기초부터 활용까지', '김철수', '에이콘출판', '2023-09-15', '9780987654321', 25000, 'SELL', 20000, FALSE, 80, 30, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 'Querydsl 사용법', 'Querydsl로 타입 안전한 쿼리 작성', '박영희', '위키북스', '2022-12-05', '9781111111111', 20000, 'REMOVED', 18000, FALSE, 60, 20,15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tag (tag_id, tag_name) VALUES
    (1, '프로그래밍'),
    (2, '자바'),
    (3, '스프링'),
    (4, '데이터베이스');

INSERT INTO book_tag (book_id, tag_id) VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (2, 1),
    (2, 2),
    (3, 4);

INSERT INTO image (book_id, uuid, is_thumbnail) VALUES
    (1, 'uuid-springboot', TRUE),
    (2, 'uuid-jpa', TRUE),
    (3, 'uuid-querydsl', TRUE);
