library ieee ;
use ieee.std_logic_1164.all;
entity citire_suma_ret_dep is
	port( enable_ret_dep:in BIT;
	sw:in std_logic_vector(3 downto 0);
	clk :in std_logic;
	clr_suma:in bit;
	sel:in BIT_VECTOR(1 downto 0);
	suma_dep_ret_out: out std_logic_vector(15 downto 0);
	terminare_suma:out bit);
end citire_suma_ret_dep;

architecture arh_citire_suma_ret_dep of citire_suma_ret_dep is
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
signal suma_ret_dep:std_logic_vector(15 downto 0);
signal out_si:bit;
begin
	I1: DMUXCIT port map(sw,enable_ret_dep,sel,suma_ret_dep);
	I2: registru port map(suma_ret_dep,enable_ret_dep,clr_suma,clk,suma_dep_ret_out);
	SI: poarta_si port map(sel(1),sel(0),out_si);
	D:  bistabil_D port map(out_si,enable_ret_dep,clear,clk,terminare_suma);
	
end architecture arh_citire_suma_ret_dep;