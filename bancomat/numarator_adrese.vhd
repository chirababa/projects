library	ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_arith.all;
use ieee.std_logic_unsigned.all;

entity numaratoradr is
	port(
	CLR:in BIT; 
	clk: in std_logic;
	en_comparator:in BIT;
	en_card:in BIT;
	adrese: out std_logic_vector (2 downto 0));
end entity;	 

architecture arh of numaratoradr is
signal temp: std_logic_vector (2 downto 0):="000";
 begin 									   
	process(clk,CLR)
	begin
	if CLR='0' then
        temp<="000";
     elsif(en_card='1' and en_comparator='0') then
		if (rising_edge(clk)) then 		
			temp<=temp+1; 
	end if;
	end if;
	end process;  
	adrese<=temp;
end architecture;