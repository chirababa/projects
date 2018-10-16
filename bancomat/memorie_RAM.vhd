library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;

entity memorie_ram is
		port (
		we : in BIT;
		addr : in std_logic_vector(2 downto 0);
		di : in std_logic_vector(15 downto 0);
		do : out std_logic_vector(15 downto 0));
end memorie_ram;

architecture arh_RAM of memorie_ram is
	type ram_type is array (0 to 5) of std_logic_vector (15 downto 0);
signal RAM : ram_type:=(
	0=> "0000000000000001",--prag de verif
	1=> "0001000100010001",--1111 
	2=>	"0100011100100010",--4722 
	3=>	"0101100000110010",--5832 
	4=>	"1000100010001001",--8889
	5=>	"1111111111111111");
begin
	process(addr,we,di)
	begin
		--if rising_edge(clk) then
			if we = '1' then
				RAM(conv_integer(addr)) <= di;
			else
		do <= RAM(conv_integer(addr));
		end if;
end process;
end architecture arh_RAM;