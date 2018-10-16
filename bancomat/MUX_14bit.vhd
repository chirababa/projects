library ieee ;
use ieee.std_logic_1164.all;
entity mux is
	port (
	mux1:in std_logic_vector(13 downto 0); 
	mux2:in std_logic_vector(13 downto 0);
	mux3:in std_logic_vector(13 downto 0);
	mux4:in std_logic_vector(13 downto 0);
	sel:in std_logic_vector(1 downto 0);
	term:out std_LOGIC_VECTOR(13 downto 0)
	);
end mux;

architecture arh_mux of mux is
begin
process(sel) 
begin
case  sel is
	when "00" => term <=mux1;
	when "01" => term <=mux2;
	when "10" => term <=mux3;
	when others => term <=mux4;
end case;
end process;
end architecture arh_mux;  
