library ieee ;
use ieee.std_logic_1164.all;
entity bistabil_D is 
	port(D:in bit;
	R,S:in bit;
	CLK: in std_logic;
	Q:out bit);
end bistabil_D;

ARCHITECTURE flip_flop_D OF bistabil_D IS
BEGIN
  PROCESS (CLK , R)
BEGIN	 
	IF   R = '0' THEN Q <= '0';
     elsIF (rising_edge(CLK)) THEN
        IF   S = '1' THEN Q <= '1';
        ELSE    Q <= D;
        END IF;
    END IF;
END PROCESS ;
END ARCHITECTURE flip_flop_D;