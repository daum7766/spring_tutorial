CREATE TABLE user (
  uid char(8) NOT NULL,
  email varchar(128) DEFAULT NULL,
  password varchar(128) DEFAULT NULL,
  create_date datetime DEFAULT current_timestamp(),
  PRIMARY KEY (uid),
  UNIQUE KEY `user_idx_unique_email` (email)
) DEFAULT CHARSET=utf8;

create table article(
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100),
    content VARCHAR(1000),
    create_date datetime DEFAULT current_timestamp()
    ) DEFAULT CHARSET=utf8;


create table article(
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100),
    content VARCHAR(1000),
    create_date datetime DEFAULT current_timestamp(),
    user char(8),
    FOREIGN KEY(user) REFERENCES user(uid) ON UPDATE CASCADE
    ) DEFAULT CHARSET=utf8;