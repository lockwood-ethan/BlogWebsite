INSERT INTO POST(ID, TITLE, BODY, OWNER) VALUES (23, 'Title 1', 'Body 1', 'username1');
INSERT INTO POST(ID, TITLE, BODY, OWNER) VALUES (24, 'Title 2', 'Body 2', 'username1');
INSERT INTO POST(ID, TITLE, BODY, OWNER) VALUES (25, 'Title 3', 'Body 3', 'username1');
INSERT INTO POST(ID, TITLE, BODY, OWNER) VALUES (26, 'New Title', 'New Body', 'newUsername');

INSERT INTO COMMENT(ID, POST_ID, BODY, OWNER) VALUES (32, 24, 'Comment Body 1', 'username1');
INSERT INTO COMMENT(ID, POST_ID, BODY, OWNER) VALUES (33, 24, 'Comment Body 2', 'newUsername');
INSERT INTO COMMENT(ID, POST_ID, BODY, OWNER) VALUES (34, 25, 'Comment Body 3', 'username1');