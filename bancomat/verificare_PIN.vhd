library ieee ;
use ieee.std_logic_1164.all;

entity verificare_PIN is
	port(
	--clk_placuta:in std_logic;
	modif_PIN:in std_logic_vector(15 downto 0);
	scrie:in BIT;
	cod_PIN:in std_logic_vector(15 downto 0);
	poz_card:in std_logic_vector(2 downto 0);
	MIC,MARE,EGAL:out BIT;
	poz_PIN:out std_logic_vector(2 downto 0));
end  verificare_PIN;

architecture verif_PIN of verificare_PIN is
signal s_cod_PIN:std_logic_vector(15 downto 0);
signal start : bit :='1';
component comparator is
	port(
	startCOMP:in bit;  
	num1 : in std_logic_vector(15 downto 0);  
    num2 : in std_logic_vector(15 downto 0);  
    less : out BIT;   
    equal : out BIT;  
    greater : out BIT );
end component comparator;

 component memorie_ram is
	port (
	we : in BIT;
	addr : in std_logic_vector(2 downto 0);
	di : in std_logic_vector(15 downto 0);
	do : out std_logic_vector(15 downto 0));
end component memorie_ram;
begin
	mem_RAM: memorie_ram port map(scrie,poz_card,modif_PIN,s_cod_PIN);
	compare: comparator port map(start,s_cod_PIN,cod_PIN,MIC,EGAL,MARE);
	--comp_mare=>MARE;
	poz_pin<=poz_card;
end architecture verif_PIN;