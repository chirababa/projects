library IEEE;
use IEEE.STD_LOGIC_1164.ALL;


entity comparator is
port(   startCOMP:in BIT; 
        num1 : in std_logic_vector(15 downto 0); 
        num2 : in std_logic_vector(15 downto 0);  
         less : out BIT;  
         equal :out BIT;   
         greater :out BIT   
);
end comparator;

architecture comparator_arh of comparator is
begin
process(num1,num2,startCOMP)
begin    
if startCOMP='0' then
less <= '0';
equal <= '0';
greater <= '0';
elsif (num1 > num2 ) then  
less <= '0';
equal <= '0';
greater <= '1';
elsif (num1 < num2) then   
less <= '1';
equal <= '0';
greater <= '0';									 
else     
less <= '0';
equal <= '1';
greater <= '0';
end if;
end process;   
end comparator_arh;