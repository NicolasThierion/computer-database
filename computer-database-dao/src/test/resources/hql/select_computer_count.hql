SELECT COUNT(*) AS count
FROM Computer computer
WHERE UPPER(%s) LIKE :value
