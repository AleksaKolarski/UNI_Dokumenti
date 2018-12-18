INSERT INTO languages (language_name)
VALUES ('srp');
INSERT INTO languages (language_name)
VALUES ('eng');
INSERT INTO languages (language_name)
VALUES ('ger');
INSERT INTO languages (language_name)
VALUES ('fre');

INSERT INTO categories (category_name)
VALUES ('art');
INSERT INTO categories (category_name)
VALUES ('business');
INSERT INTO categories (category_name)
VALUES ('history');
INSERT INTO categories (category_name)
VALUES ('cooking');

INSERT INTO users (user_firstname, user_lastname, user_username, user_password, user_role, category_id)
VALUES ('User1firstname', 'User1lastname', 'user1', 'user1', 'ADMIN', null);
INSERT INTO users (user_firstname, user_lastname, user_username, user_password, user_role, category_id)
VALUES ('User2firstname', 'User2lastname', 'user2', 'user2', 'USER', null);
INSERT INTO users (user_firstname, user_lastname, user_username, user_password, user_role, category_id)
VALUES ('User3firstname', 'User3lastname', 'user3', 'user3', 'USER', 2);
INSERT INTO users (user_firstname, user_lastname, user_username, user_password, user_role, category_id)
VALUES ('User4firstname', 'User4lastname', 'user4', 'user4', 'USER', 3);

INSERT INTO ebooks (ebook_title, ebook_author, ebook_keywords, ebook_publicationYear, ebook_filename, ebook_mime, category_id, language_id, user_id)
VALUES ();