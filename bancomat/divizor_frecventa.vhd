library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.numeric_std.ALL; 

entity divizor_frecventa is
	generic(Hz:INTEGER);
	port ( clk: in std_logic;
		clock_out: out std_logic);
end divizor_frecventa;

architecture clock_divider of divizor_frecventa is
signal count: integer:=1;
signal tmp : std_logic := '0';
begin
process(clk)
begin
	
	if(clk'event and clk='1') then
		count <=count+1;
		if (count = Hz) then
			tmp <= NOT tmp;
			count <= 1;
		end if;
	end if;
	clock_out <= tmp;
end process;
end architecture clock_divider;
