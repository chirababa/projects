library ieee ;
use ieee.std_logic_1164.all;
entity citire is
	port ( 
	start:in BIT;
	sw:in std_logic_vector(3 downto 0);
	clk :in std_logic;
	sel:in BIT_VECTOR(1 downto 0);
	nr_cont: out std_logic_vector(15 downto 0);
	terminare_card:out BIT
	);
end citire;

architecture CIT of citire is
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
--signal clear1:bit:='0';
	signal nr:std_logic_vector(15 downto 0);
	signal out_si:bit;
	--signal reset:bit:=start;
begin
	I1: DMUXCIT port map(sw,start,sel,nr);
	I2: registru port map(nr,start,start,clk,nr_cont);
	SI: poarta_si port map(sel(1),sel(0),out_si);
	D:  bistabil_D port map(out_si,start,clear,clk,terminare_card);
	--clear<= std_logic(start);
	
end architecture CIT;