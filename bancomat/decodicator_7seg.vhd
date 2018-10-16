library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

entity decodificator_7seg is
port (bcd : in std_logic_vector(3 downto 0);  --BCD input
      segment7 : out std_logic_vector(6 downto 0)  -- 7 bit decoded output.
    );
end decodificator_7seg;

architecture ARH_decd_7seg of decodificator_7seg is

begin
process (bcd)
begin
case  bcd is
	when "0000"=> segment7 <="0000001";  -- '0'
	when "0001"=> segment7 <="1001111";  -- '1'
	when "0010"=> segment7 <="0010010";  -- '2'
	when "0011"=> segment7 <="0000110";  -- '3'
	when "0100"=> segment7 <="1001100";  -- '4' 
	when "0101"=> segment7 <="0100100";  -- '5'
	when "0110"=> segment7 <="0100000";  -- '6'
	when "0111"=> segment7 <="0001111";  -- '7'
	when "1000"=> segment7 <="0000000";  -- '8'
	when "1001"=> segment7 <="0000100";  -- '9'
	when "1010"=> segment7 <="0110000";  -- 'E'
	when "1011"=> segment7 <="1111010";  -- 'r'
	when "1110"=> segment7 <="1111111";
	when "1111"=> segment7 <="0111000";	 -- 'F' 
	when others=> segment7 <="1111111";  
end case;
end process;
end ARH_decd_7seg;