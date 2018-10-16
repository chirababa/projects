library ieee ;
use ieee.std_logic_1164.all;
ENTITY registru3 IS PORT(
    int	: IN STD_LOGIC_VECTOR(2 downto 0);
    pl  : IN BIT; -- load/enable.
    clr : IN bit; -- async. clear.
    clk : IN STD_LOGIC; -- clock.
    q   : OUT STD_LOGIC_VECTOR(2 downto 0) -- output.
);
END registru3;

ARCHITECTURE ARHregistru3 OF registru3 IS

BEGIN
    process(clk, clr)
    begin
        if clr = '1' then
            q <= "000";
        elsif rising_edge(clk) then
            if pl = '1' then
                q <= int;
            end if;
        end if;
    end process;
END ARHregistru3;