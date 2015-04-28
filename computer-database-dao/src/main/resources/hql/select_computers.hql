SELECT computer
FROM Computer computer LEFT OUTER JOIN computer.company AS company
WHERE UPPER(%s) LIKE :value
ORDER BY %s
