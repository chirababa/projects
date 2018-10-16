library ieee ;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;
use ieee.std_logic_arith.all;

entity conversie_suma_ret_dep is 
	port (enb_conversie:in bit;
	suma_cont_ret_dep:in std_logic_vector(15 downto 0);
	suma_out:out std_logic_vector(13 downto 0));
end conversie_suma_ret_dep;

architecture arh_conversie_suma_ret_dep of conversie_suma_ret_dep is
	signal s_suma_cont_mii:integer:=0;
	signal s_suma_cont_sute:integer:=0;
	signal s_suma_cont_zeci:integer:=0;
	signal s_suma_cont_unitati:integer:=0;
	signal s_suma_dep_ret_totala:integer:=0;
begin
	s_suma_cont_unitati<=conv_integer(suma_cont_ret_dep(3 downto 0));
	s_suma_cont_zeci<=conv_integer(suma_cont_ret_dep(7 downto 4));
	s_suma_cont_sute<=conv_integer(suma_cont_ret_dep(11 downto 8));
	s_suma_cont_mii<=conv_integer(suma_cont_ret_dep(15 downto 12));
	s_suma_dep_ret_totala<=s_suma_cont_mii*1000+s_suma_cont_sute*100+s_suma_cont_zeci*10+s_suma_cont_unitati when enb_conversie='1';
	suma_out<=conv_std_logic_vector(s_suma_dep_ret_totala,14) when enb_conversie='1' else "00000000000000";
end architecture arh_conversie_suma_ret_dep;
	
	
	
	
	