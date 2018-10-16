library IEEE;
use IEEE.std_logic_1164.all;
use IEEE.std_logic_unsigned.all;
use IEEE.NUMERIC_STD.all;

entity ROM is
  port ( address : in std_logic_vector(2 downto 0);
         data : out std_logic_vector(15 downto 0) );
end entity ROM;

architecture memorie_rom of ROM is
  type mem is array ( 0 to 5) of std_logic_vector(15 downto 0);
  constant my_Rom : mem := (
    0 =>  "0000000000000001",--prag minim(0001)
    1  => "0001001000110100",--1234
    2  => "0100001100100001",--4321
    3  => "0101010000110010",--5432
    4  => "1000010000100001",--8421
	5  => "1111111111111111");--prag de comparatie(FFFF)
begin
    data<=my_Rom(conv_integer(address));
end architecture memorie_rom;