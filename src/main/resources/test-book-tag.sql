CREATE TABLE IF NOT EXISTS book (
    book_id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    isbn VARCHAR(20),
    regular_price BIGINT,
    sale_price BIGINT,
    status VARCHAR(20)
    );

CREATE TABLE IF NOT EXISTS tag (
   tag_id BIGINT PRIMARY KEY,
   tag_name VARCHAR(100) NOT NULL
    );

CREATE TABLE IF NOT EXISTS book_tag (
    book_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (book_id, tag_id),
    CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES book(book_id),
    CONSTRAINT fk_tag FOREIGN KEY (tag_id) REFERENCES tag(tag_id)
    );

CREATE TABLE IF NOT EXISTS image (
    image_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    book_id BIGINT NOT NULL,
    uuid VARCHAR(255),
    is_thumbnail BOOLEAN,
    CONSTRAINT fk_book_image FOREIGN KEY (book_id) REFERENCES book(book_id)
    );

INSERT INTO book (book_id, title, author, isbn, regular_price, sale_price, status) VALUES
    (1, 'Spring Boot 완벽 가이드', '홍길동', '9781234567890', 30000, 25000, 'SELL'),
    (2, 'JPA 입문', '김철수', '9780987654321', 25000, 20000, 'SELL'),
    (3, 'Querydsl 사용법', '박영희', '9781111111111', 20000, 18000, 'REMOVED');

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
