library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;

entity memorie_ram_bancnote is
	port (	
		enable:in bit;
		we_dep : in BIT;
		we_ret: in BIT;
		addr : in std_logic_vector(1 downto 0);
		clk:in std_logic;
		addr_out:out std_logic_vector(8 downto 0));
end memorie_ram_bancnote;

architecture arh_RAM_bancnote of memorie_ram_bancnote is
	type ram_type is array (0 to 3) of std_logic_vector (8 downto 0);
signal RAM : ram_type:=(
	0=> "000001111",--500E(15 buc)
	1=> "000011001",--100E(25 buc) 	
	2=>	"000110010",--50E(50 buc) 
	3=>	"111110100");--10E(200) 
begin
	process(addr,enable,clk) 
	begin
		if(enable='1') then	 
			if(rising_edge(clk))then
			if we_dep = '1' then
				RAM(conv_integer(addr)) <=RAM(conv_integer(addr))+1;
			elsif we_ret='1' then
				RAM(conv_integer(addr)) <=RAM(conv_integer(addr))-1;	
			end if;
			end if;
		addr_out <= RAM(conv_integer(addr)); 
		end if;
end process;
end architecture arh_RAM_bancnote;