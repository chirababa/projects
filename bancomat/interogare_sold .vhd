library ieee ;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;
use ieee.std_logic_arith.all;

entity interogare_sold is 
	port (Suma_cont:in std_logic_vector(13 downto 0);
	Suma_afisare:out std_logic_vector(15 downto 0));
end interogare_sold;

architecture arh_interogare_sold of interogare_sold is
	signal s_suma_cont_mii:integer:=0;
	signal s_suma_cont_sute:integer:=0;
	signal s_suma_cont_zeci:integer:=0;
	signal s_suma_cont_unitati:integer:=0;
	signal s_suma_cont_in_4:integer:=0;
	signal s_suma_cont_in_3:integer:=0;
	signal s_suma_cont_in_2:integer:=0;
	signal s_suma_cont_in_1:integer:=0;
	signal s_suma_afisare: std_logic_vector(13 downto 0);
begin
	s_suma_cont_in_4<=conv_integer(Suma_cont);
	s_suma_cont_unitati<=(s_suma_cont_in_4) mod (10);
	Suma_afisare(3 downto 0)<=conv_std_logic_vector(s_suma_cont_unitati,4);
	
	s_suma_cont_in_3<=s_suma_cont_in_4 / 10;
    s_suma_cont_zeci<=s_suma_cont_in_3 mod 10;
	Suma_afisare(7 downto 4)<=conv_std_logic_vector(s_suma_cont_zeci,4);
	
	s_suma_cont_in_2<=s_suma_cont_in_3 / 10;
    s_suma_cont_sute<=s_suma_cont_in_2 mod 10;
	Suma_afisare(11 downto 8)<=conv_std_logic_vector(s_suma_cont_sute,4);
	
	s_suma_cont_in_1<=s_suma_cont_in_2 / 10;
    s_suma_cont_mii<=s_suma_cont_in_1 mod 10;
	Suma_afisare(15 downto 12)<=conv_std_logic_vector(s_suma_cont_mii,4);
	
end architecture arh_interogare_sold;
	
	
	
	
	