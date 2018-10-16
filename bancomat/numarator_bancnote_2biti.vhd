library	ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_arith.all;
use ieee.std_logic_unsigned.all;

entity numarator_bancnote is
	port(
	verif:in bit;
	--freeze1:in bit;
	CU:in bit;
	--CLR:in BIT; 
	clk: in std_logic;
	adrese: out std_logic_vector (1 downto 0));
end numarator_bancnote;	 

architecture arh_numarator_bancnote of numarator_bancnote is
signal temp: std_logic_vector (1 downto 0):="00";
begin 									   
	process(clk,verif)
	begin  
	if(verif='0') then	
		temp<="00";
			elsif(rising_edge(clk)) then
				if(CU='1')then	
					temp<=temp+1;  
				end if;	
	end if;
	end process;  
	adrese<=temp;
end arh_numarator_bancnote;