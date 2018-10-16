library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;

entity memorie_ram_conturi is
		port (
		ret: in bit;
		dep : in bit; 
		addr : in std_logic_vector(2 downto 0);
		suma : in std_logic_vector(13 downto 0); 
		clk2 : in bit;
		do_interogare:out std_logic_vector(13 downto 0); 
		do : out std_logic_vector(13 downto 0));
end memorie_ram_conturi;

architecture arh_RAM_sume of memorie_ram_conturi is
	type ram_type is array (0 to 5) of std_logic_vector (13 downto 0);
signal RAM : ram_type:=(
	0=> "00001001011000",--600E
	1=> "00001111101000",--1000E 
	2=>	"00001100100000",--800E 
	3=>	"01001110001000",--5000E 
	4=>	"00011111010000",--2000E
	5=>	"00000101011110");--350E
begin
	process(addr,ret,dep,clk2) 
	begin 
		if (clk2'event and clk2='1') then 
			if ret = '1' then
				RAM(conv_integer(addr)) <= RAM(conv_integer(addr))-suma;
			elsif dep = '1' then
				RAM(conv_integer(addr)) <= RAM(conv_integer(addr))+suma;
			end if;	
		end if;	  
end process;	
	do <= RAM(conv_integer(addr)) when clk2='0';
	do_interogare<=RAM(conv_integer(addr));
end architecture arh_RAM_sume;