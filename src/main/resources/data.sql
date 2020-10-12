INSERT INTO Author (prefix, first_name, middle_name, last_name, sufix) VALUES
('', 'Henryk', '', 'Sienkiewicz', ''),
('', 'Adam', '', 'Mickiewicz', ''),
('', 'Juliusz', '', 'Słowacki', '')
;

INSERT INTO Book (title, sub_title, year) VALUES
('Potop', '', 1920),
('Dziady', '', 1939),
('Lalka', '', 1917);

INSERT INTO Book_Author (book_id, author_id) VALUES
(1,1),
(2,2),
(2,3)
;