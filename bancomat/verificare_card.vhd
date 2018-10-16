library ieee ;
use ieee.std_logic_1164.all;

entity verificare_card is
	port(
	startvf:in bit;
	enable:in BIT;
	clk_placuta:in std_logic;
	cont_b:in std_logic_vector(15 downto 0);
	MARE,EGAL:out BIT;
	poz_card:out std_logic_vector(2 downto 0));
end  verificare_card;

architecture verif_card of verificare_card is
signal comp_mare:BIT:='0';
signal comp_egal:BIT:='0';
signal terminare:BIT; 
signal adr: std_logic_vector(2 downto 0);
signal cont_bancar:std_logic_vector(15 downto 0);
signal mic:BIT;


component numaratoradr is
	port(
	CLR:in BIT; 
	clk: in std_logic;
	en_comparator:in BIT;
	en_card:in BIT;
	adrese: out std_logic_vector (2 downto 0));
end component numaratoradr;

component ROM is
	port ( address : in std_logic_vector(2 downto 0);
	data : out std_logic_vector(15 downto 0) );
end component ROM;

component comparator is
	port(
	startCOMP:in bit;  
	num1 : in std_logic_vector(15 downto 0);  
         num2 :  in std_logic_vector(15 downto 0);  
         less :      out BIT;   
           equal :   out BIT;  
           greater :  out BIT );
end component comparator;
component poarta_sau  is
	port(A,B:in bit;
	Q:out bit);
end component poarta_sau;


begin
	SAU:poarta_sau port map(comp_egal,comp_mare,terminare);
	numarator:numaratoradr port map(startvf,clk_placuta,terminare,enable,adr);
	mem_ROM: ROM port map(adr,cont_bancar);
	compare:comparator port map(startvf,cont_bancar,cont_b,mic,comp_egal,comp_mare);
	EGAL<=comp_egal;
	MARE<=terminare;
	poz_card<=adr;
	--comp_mare=>MARE	;
end architecture verif_card;