SELECT computer.id AS 'computer.id', computer.name AS 'computer.name', computer.introduced AS 'computer.introduced', computer.discontinued AS 'computer.discontinued',
company.id AS 'company.id', company.name AS 'company.name'
FROM computer computer LEFT OUTER JOIN company company on (computer.company_id = company.id)
WHERE UPPER(%s) LIKE ?
ORDER BY %s
LIMIT ?, ?;
