SELECT COUNT(*) AS count
FROM Company company
WHERE UPPER(%s) LIKE :value
