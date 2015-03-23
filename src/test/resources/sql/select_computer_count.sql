SELECT COUNT(r.id) AS 'count'
FROM computer r
WHERE r.name LIKE ?;
