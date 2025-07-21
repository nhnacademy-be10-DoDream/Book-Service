CREATE TABLE IF NOT EXISTS category (
                                        category_id BIGINT PRIMARY KEY,
                                        category_name VARCHAR(255) NOT NULL,
    depth BIGINT NOT NULL,
    parent_id BIGINT
    );

INSERT INTO category (category_id, category_name, depth, parent_id) VALUES
                                                                        (1, '소설', 1, NULL),
                                                                        (2, '경제', 1, NULL),
                                                                        (3, 'IT', 1, NULL),
                                                                        (4, '문학', 2, 1),
                                                                        (5, '소설_장르', 3, 4);
