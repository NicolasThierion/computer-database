SELECT company.id AS 'company.id', company.name AS 'company.name'
FROM company company
WHERE UPPER(%s) LIKE ?
ORDER BY %s
LIMIT ?, ?;
