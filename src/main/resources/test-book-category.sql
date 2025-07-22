CREATE TABLE IF NOT EXISTS book (
    book_id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    isbn VARCHAR(20),
    regular_price BIGINT,
    sale_price BIGINT,
    status VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS category (
    category_id BIGINT PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL,
    depth BIGINT NOT NULL,
    parent_id BIGINT
);

CREATE TABLE IF NOT EXISTS book_category (
    book_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (book_id, category_id),
    CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES book(book_id),
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(category_id)
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
    (2, 'JPA 입문', '김철수', '9780987654321', 25000, 20000, 'SELL');

INSERT INTO category (category_id, category_name, depth, parent_id) VALUES
    (1, '소설', 1, NULL),
    (4, '문학', 2, 1),
    (3, 'IT', 1, NULL);

INSERT INTO book_category (book_id, category_id) VALUES
    (1, 1),
    (1, 4),
    (2, 3);


INSERT INTO image (book_id, uuid, is_thumbnail) VALUES
    (1, 'uuid-springboot', TRUE),
    (2, 'uuid-jpa', TRUE);
