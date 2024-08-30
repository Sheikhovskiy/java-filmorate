-- Вставка записей в таблицу mpa, если такие записи не существуют
INSERT INTO mpa (mpa_id, name)
SELECT * FROM (SELECT 1 AS mpa_id, 'G' AS name) AS temp
WHERE NOT EXISTS (
    SELECT 1 FROM mpa WHERE mpa_id = 1
) LIMIT 1;

INSERT INTO mpa (mpa_id, name)
SELECT * FROM (SELECT 2 AS mpa_id, 'PG' AS name) AS temp
WHERE NOT EXISTS (
    SELECT 1 FROM mpa WHERE mpa_id = 2
) LIMIT 1;

INSERT INTO mpa (mpa_id, name)
SELECT * FROM (SELECT 3 AS mpa_id, 'PG-13' AS name) AS temp
WHERE NOT EXISTS (
    SELECT 1 FROM mpa WHERE mpa_id = 3
) LIMIT 1;

INSERT INTO mpa (mpa_id, name)
SELECT * FROM (SELECT 4 AS mpa_id, 'R' AS name) AS temp
WHERE NOT EXISTS (
    SELECT 1 FROM mpa WHERE mpa_id = 4
) LIMIT 1;

INSERT INTO mpa (mpa_id, name)
SELECT * FROM (SELECT 5 AS mpa_id, 'NC-17' AS name) AS temp
WHERE NOT EXISTS (
    SELECT 1 FROM mpa WHERE mpa_id = 5
) LIMIT 1;

-- Вставка записей в таблицу genres, если такие записи не существуют
INSERT INTO genres (genre_id, name)
SELECT * FROM (SELECT 1 AS genre_id, 'Комедия' AS name) AS temp
WHERE NOT EXISTS (
    SELECT 1 FROM genres WHERE genre_id = 1
) LIMIT 1;

INSERT INTO genres (genre_id, name)
SELECT * FROM (SELECT 2 AS genre_id, 'Драма' AS name) AS temp
WHERE NOT EXISTS (
    SELECT 1 FROM genres WHERE genre_id = 2
) LIMIT 1;

INSERT INTO genres (genre_id, name)
SELECT * FROM (SELECT 3 AS genre_id, 'Мультфильм' AS name) AS temp
WHERE NOT EXISTS (
    SELECT 1 FROM genres WHERE genre_id = 3
) LIMIT 1;

INSERT INTO genres (genre_id, name)
SELECT * FROM (SELECT 4 AS genre_id, 'Триллер' AS name) AS temp
WHERE NOT EXISTS (
    SELECT 1 FROM genres WHERE genre_id = 4
) LIMIT 1;

INSERT INTO genres (genre_id, name)
SELECT * FROM (SELECT 5 AS genre_id, 'Документальный' AS name) AS temp
WHERE NOT EXISTS (
    SELECT 1 FROM genres WHERE genre_id = 5
) LIMIT 1;

INSERT INTO genres (genre_id, name)
SELECT * FROM (SELECT 6 AS genre_id, 'Боевик' AS name) AS temp
WHERE NOT EXISTS (
    SELECT 1 FROM genres WHERE genre_id = 6
) LIMIT 1;
