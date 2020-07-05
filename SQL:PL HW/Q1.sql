CREATE OR REPLACE FUNCTION getPidByPrice(argPrice parts.price%type)
RETURN Parts.pid%type

IS
    partPid Parts.pid%type;
    dist number;
    highestYear number;

BEGIN

-- Get distance
SELECT max(distance) into dist
FROM (
      SELECT abs(price - argPrice) distance
      FROM Parts
      ORDER BY distance)
WHERE ROWNUM <= 3;

-- Get the max year from items with the same distance      
SELECT max(year) into highestYear
FROM (
      SELECT rownum r, abs(price - argPrice) distance, pid, year
      FROM Parts)
      WHERE distance = dist;

-- Get the pid with higher year and highest pid
SELECT pid into partPid
FROM (
      SELECT abs(price - argPrice) distance, pid, year
      FROM Parts
      ORDER BY pid desc)
      WHERE distance = dist and year = highestYear and ROWNUM = 1;

RETURN partPid;

  EXCEPTION

    WHEN TOO_MANY_ROWS THEN
	  RETURN -1;

  END;
/