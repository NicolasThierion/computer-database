SELECT COUNT(*) AS count
FROM User user
WHERE UPPER(%s) LIKE :value
