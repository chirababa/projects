library ieee ;
use ieee.std_logic_1164.all;
ENTITY registru1 IS PORT(
    int	: IN BIT;	
	clr : in BIT;
    pl  : IN BIT; -- load/enable.
    clk : IN STD_LOGIC; -- clock.
    q   : OUT BIT -- output.
);
END registru1;

ARCHITECTURE ARHregistru1 OF registru1 IS

BEGIN
    process(clk,pl,clr)
    begin
		if(clr='1')then 
			q<='0';
        elsif rising_edge(clk) then
            if pl = '0' then
                q <= int;
            end if;
        end if;
    end process;
END ARHregistru1;