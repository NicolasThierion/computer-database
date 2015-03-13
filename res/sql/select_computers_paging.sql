SELECT r.id AS 'id', r.name AS 'name', r.introduced AS 'introduced', r.discontinued AS 'discontinued', 
y.id AS 'company_id', y.name AS 'compName'
FROM computer r LEFT OUTER JOIN company y on (r.company_id = y.id)
ORDER BY 'name'
LIMIT ?, ?;
   