library ieee ;
use ieee.std_logic_1164.all;

entity comp_cit_verif is 
	port(enable_retr:in bit;
	enable_dep:in bit;
	sws:in std_logic_vector(3 downto 0);
	clks :in std_logic;
	sels:in BIT_VECTOR(1 downto 0);
	clr_sum:in BIT;
	sum_cont1: in std_logic_vector(13 downto 0);
	suma_convertita: out std_logic_vector(13 downto 0);
	suma_neconvertita:out std_logic_vector(15 downto 0);
	semnal_terminare_suma:out bit;
	semnal_verificare:out bit);
end comp_cit_verif;

architecture arh_comp_cit_verif of comp_cit_verif is 
signal suma_t_necov:std_logic_vector(15 downto 0);
signal finish_suma:bit;
signal sum_convertita:std_logic_vector(13 downto 0);
signal enable_retragere_depunere:bit;
signal suma_final_cont_ret:std_logic_vector(13 downto 0);  
signal suma_final_cont_dep:std_logic_vector(13 downto 0);
signal sig_verif:bit;

component citire_suma_ret_dep is
	port( enable_ret_dep:in BIT;
	sw:in std_logic_vector(3 downto 0);
	clk :in std_logic;
	clr_suma:in bit;
	sel:in BIT_VECTOR(1 downto 0);
	suma_dep_ret_out: out std_logic_vector(15 downto 0);
	terminare_suma:out bit);
end component citire_suma_ret_dep;

component conversie_suma_ret_dep is 
	port (enb_conversie:in bit;
	suma_cont_ret_dep:in std_logic_vector(15 downto 0);
	suma_out:out std_logic_vector(13 downto 0));
end component conversie_suma_ret_dep;

component verificare_suma is 
	port(
	terminaresuma:in bit;
	enb_retragere:in BIT;
	enb_depunere:in BIT; 
	sum_cont: in std_logic_vector(13 downto 0);
	suma_introdusa_conversie:in std_logic_vector(13 downto 0);
	suma_introd_sw:in std_logic_vector(15 downto 0);
	verif_ok:out bit);
end component verificare_suma;

component poarta_sau is
	port(A,B:in bit;
	Q:out bit);
end component poarta_sau;  

begin
prt_sau:poarta_sau port map(enable_retr,enable_dep,enable_retragere_depunere);
cit_sum:citire_suma_ret_dep port map(enable_retragere_depunere,sws,clks,clr_sum,sels,suma_t_necov,finish_suma);
conv_sum:conversie_suma_ret_dep port map(finish_suma,suma_t_necov,sum_convertita);
verif_sum:verificare_suma port map(finish_suma,enable_retr,enable_dep,sum_cont1,sum_convertita,suma_t_necov,sig_verif);
semnal_verificare<=sig_verif;
suma_convertita<=sum_convertita;
suma_neconvertita<=suma_t_necov;
semnal_terminare_suma<=finish_suma;
end arh_comp_cit_verif;