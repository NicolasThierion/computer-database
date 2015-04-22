SELECT COUNT(company.id) AS 'count'
FROM company company
WHERE UPPER(%s) LIKE ?;
