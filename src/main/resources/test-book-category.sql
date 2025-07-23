
INSERT INTO book (book_id, title, description, author, publisher, published_at, isbn, regular_price, status, sale_price, is_giftable, view_count, book_count, discount_rate, created_at, updated_at)
VALUES
   (1, 'Spring Boot 완벽 가이드', 'Spring Boot를 완벽히 배우는 실전 가이드', '홍길동', '한빛미디어', '2024-01-10', '9781234567890', 30000, 'SELL', 25000, FALSE, 100, 50, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
   (2, 'JPA 입문', 'JPA의 기초부터 활용까지', '김철수', '에이콘출판', '2023-09-15', '9780987654321', 25000, 'SELL', 20000, FALSE, 80, 30, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO category (category_id, category_name, depth, parent_id) VALUES
    (1, '소설', 1, NULL),
    (4, '문학', 2, 1),
    (3, 'IT', 1, NULL);

INSERT INTO book_category (book_id, category_id, created_at, updated_at) VALUES
    (1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (1, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


INSERT INTO image (book_id, uuid, is_thumbnail) VALUES
    (1, 'uuid-springboot', TRUE),
    (2, 'uuid-jpa', TRUE);
