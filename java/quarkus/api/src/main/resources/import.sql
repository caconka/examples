INSERT INTO fruit (id, name, color, season, create_date, update_date) VALUES (1, 'Apple', 'Red', 'Fall',CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO fruit (id, name, color, season, create_date, update_date) VALUES (2, 'Orange', 'Orange', 'Winter', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO fruit (id, name, color, season, create_date, update_date) VALUES (3, 'Banana', 'Yellow', 'Spring', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO fruit (id, name, color, season, create_date, update_date) VALUES (4, 'Grape', 'Green', 'Summer', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO fruit (id, name, color, season, create_date, update_date) VALUES (6, 'Mango', 'Orange', 'Summer', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO fruit (id, name, color, season, create_date, update_date) VALUES (5, 'Watermelon', 'Green', 'Summer', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

SELECT setval('fruit_seq', (SELECT MAX(id) FROM fruit));

INSERT INTO book (id, title, author, publication_date, create_date, update_date) VALUES (1, 'The Hobbit', 'J.R.R. Tolkien', TIMESTAMP'1937-09-21', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

SELECT setval('book_seq', (SELECT MAX(id) FROM book));
