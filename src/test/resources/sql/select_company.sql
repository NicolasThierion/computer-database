SELECT company.id AS 'company_id', company.name AS 'company_name'
FROM company company
WHERE %s = ?;
