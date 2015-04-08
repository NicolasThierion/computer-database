SELECT COUNT(computer.id) AS 'count'
FROM computer computer
WHERE UPPER(%s) LIKE ?;
