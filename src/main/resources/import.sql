/* U ovom fajlu obavezno svaka naredba u jednom redu */
/* Zbog ogranicenja hibernate biblioteke */

INSERT INTO languages (name) VALUES ('srp');
INSERT INTO languages (name) VALUES ('eng');
INSERT INTO languages (name) VALUES ('ger');
INSERT INTO languages (name) VALUES ('fre');

INSERT INTO categories (name) VALUES ('art');
INSERT INTO categories (name) VALUES ('business');
INSERT INTO categories (name) VALUES ('history');
INSERT INTO categories (name) VALUES ('cooking');

INSERT INTO users (firstname, lastname, username, password, role, category_id) VALUES ('User1firstname', 'User1lastname', 'user1', 'user1', 'ADMIN', null);
INSERT INTO users (firstname, lastname, username, password, role, category_id) VALUES ('User2firstname', 'User2lastname', 'user2', 'user2', 'USER', null);
INSERT INTO users (firstname, lastname, username, password, role, category_id) VALUES ('User3firstname', 'User3lastname', 'user3', 'user3', 'USER', 3);
INSERT INTO users (firstname, lastname, username, password, role, category_id) VALUES ('User4firstname', 'User4lastname', 'user4', 'user4', 'USER', 4);

INSERT INTO ebooks (title, author, keywords, publication_year, filename, mime, language_id, category_id, user_id) VALUES ('art0', 'author0', 'keywords0', 1950, 'filename0', 'pdf', 1, 2, 3);
INSERT INTO ebooks (title, author, keywords, publication_year, filename, mime, language_id, category_id, user_id) VALUES ('art1', 'author1', 'keywords1', 1951, 'filename1', 'pdf', 2, 3, 4);
INSERT INTO ebooks (title, author, keywords, publication_year, filename, mime, language_id, category_id, user_id) VALUES ('art2', 'author2', 'keywords2', 1952, 'filename2', 'word', 3, 4, 2);
INSERT INTO ebooks (title, author, keywords, publication_year, filename, mime, language_id, category_id, user_id) VALUES ('art3', 'author3', 'keywords3', 1953, 'filename3', 'word', 4, 1, 3);
INSERT INTO ebooks (title, author, keywords, publication_year, filename, mime, language_id, category_id, user_id) VALUES ('art4', 'author4', 'keywords4', 1954, 'filename4', 'pdf', 1, 2, 4);
INSERT INTO ebooks (title, author, keywords, publication_year, filename, mime, language_id, category_id, user_id) VALUES ('art5', 'author5', 'keywords5', 1955, 'filename5', 'word', 2, 3, 2);
