library ieee ;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;
use ieee.std_logic_arith.all;

entity scadere is 
	port (suma1:in std_logic_vector(13 downto 0); 
	suma2:in std_logic_vector(13 downto 0);
	suma_out:out std_logic_vector(13 downto 0);
	enable:out bit
	);
end scadere;

architecture scadere_arh of scadere is 

signal sumaint:integer:=0;
signal suma_reg_int:integer:=0;
signal total:integer:=0;
signal suma:std_logic_vector(13 downto 0);

begin 
	sumaint<=conv_integer(suma1);
	suma_reg_int<=conv_integer(suma2);
	total<=sumaint-suma_reg_int; 
	suma<=conv_std_logic_vector(total,14);
	suma_out<=suma;
	enable<='1' when suma=0 else '0';
end architecture; 