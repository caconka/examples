INSERT INTO fruit (id, name, color, season) VALUES (1, 'Apple', 'Red', 'Fall');
INSERT INTO fruit (id, name, color, season) VALUES (2, 'Orange', 'Orange', 'Winter');
INSERT INTO fruit (id, name, color, season) VALUES (3, 'Banana', 'Yellow', 'Spring');
INSERT INTO fruit (id, name, color, season) VALUES (4, 'Grape', 'Green', 'Summer');
INSERT INTO fruit (id, name, color, season) VALUES (5, 'Watermelon', 'Green', 'Summer');
INSERT INTO fruit (id, name, color, season) VALUES (6, 'Mango', 'Orange', 'Summer');

SELECT setval('fruit_seq', (SELECT MAX(id) FROM fruit));

INSERT INTO book (id, title, author, publication_date) VALUES (1, 'The Hobbit', 'J.R.R. Tolkien', TIMESTAMP'1937-09-21');

SELECT setval('book_seq', (SELECT MAX(id) FROM book));
