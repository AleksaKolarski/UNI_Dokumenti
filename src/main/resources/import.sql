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

INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_USER');

INSERT INTO users (firstname, lastname, username, password, category_id) VALUES ('User1firstname', 'User1lastname', 'user1', '$2a$10$XMET5VEZuVzk4wJmSvPcr.G8vKTlrH.2DFXeKZp7QY0BmMJ8wlOkC', null);
INSERT INTO users (firstname, lastname, username, password, category_id) VALUES ('User2firstname', 'User2lastname', 'user2', '$2a$10$gPfPnKhxZoYHi0yVCSm1NO4AxLnVzLD75.MhblfeWrwzNO4aluwhe', null);
INSERT INTO users (firstname, lastname, username, password, category_id) VALUES ('User3firstname', 'User3lastname', 'user3', '$2a$10$ppgk/fKWr770z6eENcyfw.BAu30EP3OPzsfxVPU4uBomU8zw7OqOm', 3);
INSERT INTO users (firstname, lastname, username, password, category_id) VALUES ('User4firstname', 'User4lastname', 'user4', '$2a$10$w/CHtqkkLMKyKxgnisaMQuVtwvFwQ..2graRZ2CMSea6gjs5Fkriu', 4);

INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO users_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO users_roles (user_id, role_id) VALUES (2, 2);
INSERT INTO users_roles (user_id, role_id) VALUES (3, 2);
INSERT INTO users_roles (user_id, role_id) VALUES (4, 2);

INSERT INTO ebooks (title, author, keywords, publication_year, document_name, filename, mime, language_id, category_id, user_id) VALUES ('art0', 'author0', 'keywords0', 1950, 'documentName0', 'filename0', 'pdf', 1, 2, 3);
INSERT INTO ebooks (title, author, keywords, publication_year, document_name, filename, mime, language_id, category_id, user_id) VALUES ('brt1', 'author1', 'keywords1', 1951, 'documentName1', 'filename1', 'pdf', 2, 3, 4);
INSERT INTO ebooks (title, author, keywords, publication_year, document_name, filename, mime, language_id, category_id, user_id) VALUES ('crt2', 'author2', 'keywords2', 1952, 'documentName2', 'filename2', 'word', 3, 4, 2);
INSERT INTO ebooks (title, author, keywords, publication_year, document_name, filename, mime, language_id, category_id, user_id) VALUES ('drt3', 'author3', 'keywords3', 1953, 'documentName3', 'filename3', 'word', 4, 1, 3);
INSERT INTO ebooks (title, author, keywords, publication_year, document_name, filename, mime, language_id, category_id, user_id) VALUES ('ert4', 'author4', 'keywords4', 1954, 'documentName4', 'filename4', 'pdf', 1, 2, 4);
INSERT INTO ebooks (title, author, keywords, publication_year, document_name, filename, mime, language_id, category_id, user_id) VALUES ('frt5', 'author5', 'keywords5', 1955, 'documentName5', 'filename5', 'word', 2, 3, 2);

INSERT INTO ebooks (title, author, keywords, publication_year, document_name, filename, mime, language_id, category_id, user_id) VALUES ('Koridor 10 do kraja 2010', 'Miki - copy sa B92', 'koridor, autoput', 2009, 'Koridor 10 do kraja 2010.pdf', '1547584240649-Koridor 10 do kraja 2010.pdf', null, 2, 1, 1);
INSERT INTO ebooks (title, author, keywords, publication_year, document_name, filename, mime, language_id, category_id, user_id) VALUES ('Otvorene dve deonicekoridora 10', 'Miki123', 'autoput, koridor', 2009, 'Otvorene dve deonicekoridora 10.pdf', '1547584246438-Otvorene dve deonicekoridora 10.pdf', null, 2, 1, 1);
INSERT INTO ebooks (title, author, keywords, publication_year, document_name, filename, mime, language_id, category_id, user_id) VALUES ('Pandemija gripa?', 'Miki321', 'sumnja, pandemija, gripa', 2009, 'Pandemija gripa.pdf', '1547584252570-Pandemija gripa.pdf', null, 2, 1, 1);
