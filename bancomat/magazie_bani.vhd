library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL; 

entity magazie is 
	port(
	retragere:in BIT;
	depunere:in BIT;
	verif:in BIT;
	stare_card_pin:in bit;
	suma_cit:in std_logic_vector(13 downto 0);
	clk :in std_logic;
	suma_magazie: out std_logic_vector(13 downto 0);
	suma_mag_en:out bit
	);
end magazie; 

architecture magazie_arh of magazie is 

signal suma_out1: std_logic_vector(13 downto 0); 
signal suma_out2: std_logic_vector(13 downto 0);
signal total: std_logic_vector(13 downto 0);
signal iesire_mux: std_logic_vector(13 downto 0);
signal enable_op: BIT;
signal adresa: std_logic_vector(8 downto 0);
signal e: BIT;
signal l: BIT;
signal g: BIT;
signal adresa_NRT: std_logic_vector(1 downto 0);
signal sau_rd : bit;
signal err1: BIT;
signal err2: BIT;
signal reset_nrt: BIT;	

signal suma_500:std_logic_vector(13 downto 0):="00000111110100";
signal suma_100:std_logic_vector(13 downto 0):="00000001100100";
signal suma_50:std_logic_vector(13 downto 0):="00000000110010";
signal suma_10:std_logic_vector(13 downto 0):="00000000001010";


component numarator_bancnote is
	port(
	verif:in bit;
	CU:in bit;
	clk: in std_logic;
	adrese: out std_logic_vector (1 downto 0));
end component numarator_bancnote;		

component memorie_ram_bancnote is
	port (	
		enable:in bit;
		we_dep : in BIT;
		we_ret: in BIT;
		addr : in std_logic_vector(1 downto 0);
		clk:in std_logic;
		addr_out:out std_logic_vector(8 downto 0));
end component memorie_ram_bancnote;	 

component comparator_bancnote is
port(
		startCOMP:in bit;
		num1 : in std_logic_vector(13 downto 0); 
		num2 : in std_logic_vector(13 downto 0);
		less : out BIT;  
		equal :out BIT;   
		greater :out BIT);	 
end component comparator_bancnote;

component mux is
	port (	 
	mux1:in std_logic_vector(13 downto 0); 
	mux2:in std_logic_vector(13 downto 0);
	mux3:in std_logic_vector(13 downto 0);
	mux4:in std_logic_vector(13 downto 0);
	sel:in std_logic_vector(1 downto 0);
	term:out std_LOGIC_VECTOR(13 downto 0)
	);
end component mux;

component scadere is 
	port (suma1:in std_logic_vector(13 downto 0); 
	suma2:in std_logic_vector(13 downto 0);
	suma_out:out std_logic_vector(13 downto 0);
	enable:out bit
	);
end component scadere;


component memorie_ram_bancnote2 is
	port (	enable:in bit;
	reset:in bit;
	addr : in std_logic_vector(1 downto 0);
	clk: in std_logic;
	addr_out:out std_logic_vector(13 downto 0));
end component memorie_ram_bancnote2;

component poarta_sau is
	port(A,B:in bit;
	Q:out bit);
end component poarta_sau;

component poarta_si_f is
	port(A,B:in BIT;
	Y:out BIT);
end component poarta_si_f;

component inversor is
	port(A:in BIT;
	Y:out BIT);
end component inversor;

begin
	mux_intrari : mux port map(suma_500,suma_100,suma_50,suma_10,adresa_NRT,iesire_mux);
	comparator_special : comparator_bancnote port map(verif,suma_out2,iesire_mux,l,e,g);
	ram: memorie_ram_bancnote port map (enable_op,depunere,retragere,adresa_NRT,clk,adresa);
	sau : poarta_sau port map (verif,reset_nrt,sau_rd);  
	sau_comp : poarta_sau port map (e,g,enable_op);
	mem_2_RAM:  memorie_ram_bancnote2 port map(enable_op,verif,adresa_NRT,clk,suma_out1);
	operatie : scadere port map (suma_cit,suma_out1,suma_out2,reset_nrt);
	numarator : numarator_bancnote port map (sau_rd,l,clk,adresa_NRT); 
	suma_mag_en<=reset_nrt;
	suma_magazie<= suma_out1;
	--err1<='1' when adresa_NRT = 1 else '0' ; 
	--err2<='1' when not iesire_mux = 0 else '0' ; 
	--err<= err2 and err1;
end magazie_arh; 
	 