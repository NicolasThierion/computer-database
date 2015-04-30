SELECT computer
FROM Computer computer LEFT OUTER JOIN computer.company
WHERE UPPER(%s) LIKE :value
ORDER BY %s
