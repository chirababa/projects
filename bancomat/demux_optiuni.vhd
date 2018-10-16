library ieee ;
use ieee.std_logic_1164.all;
entity DMUX_opt is
	port (enable:in BIT;
	sel_opt:in BIT_VECTOR(1 downto 0);
	interogare_sold:out BIT;
	schimbare_PIN:out BIT;
	retragere_numerar:out BIT;
	depunere_numerar:out BIT);
end DMUX_opt;

architecture arh_DMUX_optiuni of DMUX_opt is

begin
process(sel_opt,enable)
begin
 if enable='1' then 
	if sel_opt="00" then
		interogare_sold<='1';
		schimbare_PIN<='0';
		retragere_numerar<='0';
		depunere_numerar<='0';
	elsif sel_opt="01" then
		interogare_sold<='0';
		schimbare_PIN<='1';
		retragere_numerar<='0';
		depunere_numerar<='0';
	elsif sel_opt="11" then
		interogare_sold<='0';
		schimbare_PIN<='0';
		retragere_numerar<='1';
		depunere_numerar<='0';
	elsif sel_opt="10" then
		interogare_sold<='0';
		schimbare_PIN<='0';
		retragere_numerar<='0';
		depunere_numerar<='1';
	end if;
	else
	interogare_sold<='0';
    schimbare_PIN<='0';
    retragere_numerar<='0';
    depunere_numerar<='0';
end if;

end process;
end architecture arh_DMUX_optiuni;