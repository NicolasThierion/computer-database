SELECT company.id AS 'company_id', company.name AS 'company_name'
FROM company company
WHERE UPPER(%s) LIKE ?
ORDER BY %s
LIMIT ?, ?;
