SELECT computer.id AS 'computer_id', computer.name AS 'computer_name', computer.introduced AS 'computer_intro', computer.discontinued AS 'computer_disc',
company.id AS 'company_id', company.name AS 'company_name'
FROM computer computer LEFT OUTER JOIN company company on (computer.company_id = company.id)
WHERE %s = ?;
