library ieee ;
use ieee.std_logic_1164.all;
entity DMUXCIT is
	port (dmuxsw:in std_logic_vector(3 downto 0);
	enable:in BIT;
	sel:in BIT_VECTOR(1 downto 0);
	term:out std_LOGIC_VECTOR(15 downto 0)
	);
end DMUXCIT;

architecture DMUX of DMUXCIT is
--signal sem:bit_vector(1 downto 0);
begin
process(sel,enable)
begin
if enable='1' then
--sem<=sel;
	case sel is
		when "00"  => term(15 downto 12)<=dmuxsw;
		when "01" => term(11 downto 8)<=dmuxsw;
		when "11" => term(7 downto 4)<=dmuxsw;
		when others => term(3 downto 0)<=dmuxsw;
	end case;
--end if;
else term<="0000000000000000";
end if;
end process;
end architecture DMUX;