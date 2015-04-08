SELECT r.id AS 'computer_id', r.name AS 'computer_name', r.introduced AS 'computer_intro', r.discontinued AS 'computer_disc',
y.id AS 'company_id', y.name AS 'company_name'
FROM computer r LEFT OUTER JOIN company y on (r.company_id = y.id)
WHERE r.id = ?;
