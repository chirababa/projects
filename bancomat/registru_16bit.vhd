library ieee ;
use ieee.std_logic_1164.all;
ENTITY registru IS PORT(
    int	: IN STD_LOGIC_VECTOR(15 downto 0);
    pl  : IN BIT; -- load/enable.
    clr : IN bit; -- async. clear.
    clk : IN STD_LOGIC; -- clock.
    q   : OUT STD_LOGIC_VECTOR(15 downto 0) -- output.
);
END registru;

ARCHITECTURE ARHregistru OF registru IS

BEGIN
    process(clk,clr)
    begin
        if clr = '0' then
            q <= "0000000000000000";
        elsif rising_edge(clk) then
            if pl = '1' then
                q <= int;
            end if;
        end if;
    end process;
END ARHregistru;