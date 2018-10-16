library ieee ;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;
use ieee.std_logic_arith.all;

entity memorie_ram_bancnote2 is
	port (	enable:in bit;
	reset:in bit;
	addr : in std_logic_vector(1 downto 0);
	clk : in std_logic ;
	addr_out:out std_logic_vector(13 downto 0));
end memorie_ram_bancnote2;

architecture arh_RAM_bancnote2 of memorie_ram_bancnote2 is 
	signal s_500:integer:=0;
	signal s_100:integer:=0;
	signal s_50:integer:=0;
	signal s_10:integer:=0;
	signal s_totala:integer:=0;
	signal out_demux:integer:=0;
	signal suma_total:integer:=0;
	type ram_type is array (0 to 3) of std_logic_vector (8 downto 0);
signal RAM : ram_type:=(
	0=> "000000000",--500E(15 buc)
	1=> "000000000",--100E(25 buc) 	
	2=>	"000000000",--50E(50 buc) 
	3=>	"000000000");--10E(200) 
begin
	process(addr,clk,reset,enable) 
	begin
		if(reset='0') then 
			RAM(0)<="000000000";
			RAM(1)<="000000000";
			RAM(2)<="000000000";
			RAM(3)<="000000000";
		elsif(enable='1' ) then   
			if rising_edge(clk) then
			RAM(conv_integer(addr)) <=RAM(conv_integer(addr))+1;	
			end if;	
		end if;
end process; 
	s_500<=conv_integer(RAM(0));
	s_100<=conv_integer(RAM(1));
	s_50<=conv_integer(RAM(2));
	s_10<=conv_integer(RAM(3));
	s_totala<=s_500*500+s_100*100+s_50*50+s_10*10;
	addr_out<=conv_std_logic_vector(s_totala,14);
end architecture arh_RAM_bancnote2;