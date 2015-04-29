SELECT user
FROM User user
WHERE UPPER(%s) LIKE :value
ORDER BY %s
