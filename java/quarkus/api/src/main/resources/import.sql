INSERT INTO fruit (id, name, color, season, created_at, updated_at) VALUES (1, 'Apple', 'Red', 'Fall',CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO fruit (id, name, color, season, created_at, updated_at) VALUES (2, 'Orange', 'Orange', 'Winter', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO fruit (id, name, color, season, created_at, updated_at) VALUES (3, 'Banana', 'Yellow', 'Spring', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO fruit (id, name, color, season, created_at, updated_at) VALUES (4, 'Grape', 'Green', 'Summer', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO fruit (id, name, color, season, created_at, updated_at) VALUES (6, 'Mango', 'Orange', 'Summer', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO fruit (id, name, color, season, created_at, updated_at) VALUES (5, 'Watermelon', 'Green', 'Summer', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

SELECT setval('fruit_seq', (SELECT MAX(id) FROM fruit));

INSERT INTO book (id, title, author, publicated_at, created_at, updated_at) VALUES (1, 'The Hobbit', 'J.R.R. Tolkien', TIMESTAMP'1937-09-21', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO genre (id, name, created_at, updated_at) VALUES (1, 'Fantasy', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO book_genre (book_id, genre_id) VALUES (1, 1);
