SELECT COUNT(company.id) AS 'count'
FROM company company
WHERE name LIKE ?;
