SELECT COUNT(company.id) AS count
FROM Company company
WHERE UPPER(:criteria) LIKE :value
