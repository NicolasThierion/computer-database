SELECT c.id AS 'id', c.name AS 'name'
FROM company c
WHERE UPPER(c.name) LIKE ?
ORDER BY name
LIMIT ?, ?;
