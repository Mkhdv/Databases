CREATE OR REPLACE PROCEDURE insertOrder(arg_pid Parts.pid%type, arg_sid Suppliers.sid%type, arg_quantity Orders.quantity%type)

IS
 partPrice number;
 avgOrderValue number;
 avgQuant number;
 currentOrderValue number;

 newPartPrice number;
 newPartName varchar(30);
 newPartYear number;
 newPartPid number;

BEGIN  
  -- Get price for a single part in the order
  SELECT price INTO partPrice 
  FROM Parts 
  WHERE pid = arg_pid;
  -- Get average quantity of parts in previous orders
  SELECT avg(quantity) into avgQuant
  FROM Orders
  WHERE pid = arg_pid;
  -- Compute 75% of average order value
  avgOrderValue := partPrice * avgQuant * 0.75;
  -- Compute the value of the current order 
  currentOrderValue := partPrice * arg_quantity;

  -- Compare current order value vs 75% of average order value
  IF currentOrderValue <= avgOrderValue THEN

  -- If within limits, insert order
  INSERT INTO Orders VALUES (arg_pid, arg_sid, arg_quantity);

  -- If outside limits
  ELSE 
  -- Compute new price for the part
  newPartPrice := (avgOrderValue/currentOrderValue)*partPrice;

  -- Get name of the part
  SELECT pname INTO newPartName
  FROM Parts 
  WHERE pid = arg_pid;
  -- Get year of the part
  SELECT year INTO newPartYear 
  FROM Parts 
  WHERE pid = arg_pid;

  -- COmpute new PID by adding 1 to the maximum present PID
  SELECT (max(pid) + 1) INTO newPartPid
  FROM Parts;
  -- Make insertion to the Parts table with new paramteters
  INSERT INTO Parts VALUES (newPartPid, newPartName, newPartYear, newPartPrice);
  -- Make insertion to the Orders table of the new part
  INSERT INTO Orders VALUES (newPartPid, arg_sid, arg_quantity);
  END IF;

END;
/

