library ieee ;
use ieee.std_logic_1164.all;
entity citire_pin_modificat is
	port( enable_mod_pin:in BIT;
	sw:in std_logic_vector(3 downto 0);
	clk :in std_logic;
	clr_modif_pin:in bit;
	sel:in BIT_VECTOR(1 downto 0);
	PIN_mod: out std_logic_vector(15 downto 0);
	terminare_pin_mod:out BIT);
end citire_pin_modificat;

architecture arh_CIT_mod of citire_pin_modificat is
component DMUXCIT is
	port (dmuxsw:in std_logic_vector(3 downto 0);
	enable:in BIT;
	sel:in BIT_VECTOR(1 downto 0);
	term:out std_LOGIC_VECTOR(15 downto 0)
	);
end component DMUXCIT;
component registru IS PORT(
    int : IN STD_LOGIC_VECTOR(15 downto 0);
    pl  : IN BIT; -- load/enable.
    clr : IN bit; -- async. clear.
    clk : IN STD_LOGIC; -- clock.
    q   : OUT STD_LOGIC_VECTOR(15 downto 0) -- output.
);
end component registru;
component poarta_si is
	port(A,B:in BIT;
	Y:out BIT);
end component poarta_si; 

component bistabil_D is 
	port(D:in BIT;
	R,S:in bit;
	CLK: in std_logic;
	Q:out BIT);
end component bistabil_D;	  
signal clear:bit:='0';
signal pin_modificat:std_logic_vector(15 downto 0);
signal out_si:bit;
begin
	I1: DMUXCIT port map(sw,enable_mod_pin,sel,pin_modificat);
	I2: registru port map(pin_modificat,enable_mod_pin,clr_modif_pin,clk,PIN_mod);
	SI: poarta_si port map(sel(1),sel(0),out_si);
	D:  bistabil_D port map(out_si,enable_mod_pin,clear,clk,terminare_pin_mod);
	
end architecture arh_CIT_mod;