SELECT company
FROM Company company
WHERE UPPER(%s) LIKE :value
ORDER BY %s
