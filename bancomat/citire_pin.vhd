library ieee ;
use ieee.std_logic_1164.all;
entity citire_PIN is
	port ( 
	start_pin:in BIT;
	PIN_modif:in std_logic_vector(15 downto 0);
	write:in BIT;
	sw:in std_logic_vector(3 downto 0);
	clk :in std_logic;
	sel:in BIT_VECTOR(1 downto 0);
	poz_card_p:in std_logic_vector(2 downto 0);
	MIC1,MARE1,EGAL1:out BIT;
	PIN:out std_logic_vector(15 downto 0);
	terminare_PIN:out BIT);
end citire_PIN;

architecture CIT_PIN of citire_PIN is
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
component verificare_PIN is
	port(
	modif_PIN:in std_logic_vector(15 downto 0);
	scrie:in BIT;
	cod_PIN:in std_logic_vector(15 downto 0);
	poz_card:in std_logic_vector(2 downto 0);
	MIC,MARE,EGAL:out BIT;
	poz_PIN:out std_logic_vector(2 downto 0));
end component verificare_PIN;	

component poarta_si_f is
	port(A,B:in BIT;
	Y:out BIT);
end component poarta_si_f; 

signal clear_PIN:bit:='0';
--signal clear1:bit:='0';
signal s_PIN:std_logic_vector(15 downto 0);	
signal s_PIN_out:std_logic_vector(15 downto 0);
signal out_si_PIN:bit;
signal pozitie_pin:std_logic_vector(2 downto 0);
	--signal reset:bit:=start;
begin
	I1: DMUXCIT port map(sw,start_pin,sel,s_PIN);
	I2: registru port map(s_PIN,start_pin,start_pin,clk,s_PIN_out);
	PIN<=s_PIN_out;
	SI: poarta_si port map(sel(1),sel(0),out_si_PIN);
	D:  bistabil_D port map(out_si_PIN,start_pin,clear_PIN,clk,terminare_PIN); 
	VF: verificare_PIN port map(PIN_modif,write,s_PIN_out,poz_card_p,MIC1,MARE1,EGAL1,pozitie_pin);
	
end architecture CIT_PIN;