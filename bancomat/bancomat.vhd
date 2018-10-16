library ieee ;
use ieee.std_logic_1164.all;
entity bancomat is 
	port(
	start_b:in BIT;
	start_b_PIN:in BIT;
	sw_b:in std_logic_vector(3 downto 0);
	clk_b :in std_logic;
	clk_placa:in std_logic;
	sel_b:in BIT_VECTOR(1 downto 0);
	sel_opt:in BIT_VECTOR(1 downto 0);
	chitanta : in bit ;
	anod1b : out  STD_LOGIC;
    anod2b : out  STD_LOGIC;
    anod3b : out  STD_LOGIC;
    anod4b : out  STD_LOGIC;
    catodb: out STD_LOGIC_VECTOR(6 downto 0);
    card:out bit;
    pin:out bit;
    sw_pin:out bit;
	sold:out bit;
	ret:out bit;
	dep:out bit;
	chitanta_out:out bit
	);
end bancomat;
architecture ARH_bancomat of bancomat is

signal cnt_cod:std_logic_vector(15 downto 0);
signal S_pin:std_logic_vector(15 downto 0);
signal pin_introdus:std_logic_vector(15 downto 0);
signal finish_card:BIT;
signal BIG:BIT;
signal pozitie_card:std_logic_vector(2 downto 0);
signal pozitie_card1:std_logic_vector(2 downto 0);
signal clock_div:std_logic;
signal clear:BIT;
signal corect:BIT;
signal iesire_bistabil_memorare:BIT;
signal out_gate_si_f:BIT;
signal less:BIT;
signal greater:BIT;
signal afisare_enable_err:BIT;
signal pin_corect:BIT;
signal card_corect:BIT;
signal iesire_poarta_si_afis:BIT;  
signal finish_pin:BIT;
signal afis_err:BIT;
signal pl_pin:bit;
signal pl_pin_negat:bit;
signal mem_pin:bit;
signal iesire_poarta_si_afis_p:bit;
signal afis_err_pin:bit;
signal int_sold:bit;
signal switch_pin:bit;
signal retragere_bani:bit;
signal depunere_bani:bit;
signal iesire_poarta_si_sch_pin:bit;
signal PIN_modificat:std_logic_vector(15 downto 0);
signal write_enable_PIN:BIT;
signal iesire_RAM_PIN:std_logic_vector(15 downto 0);
signal reset_reg:bit:='0';
signal start_b_PIN_inv:bit;
signal memorare_cont:std_logic_vector(13 downto 0);
signal write_enable_conturi:bit;
signal out_memorare_cont:std_logic_vector(13 downto 0);
signal s_afisare_int_sold:std_logic_vector(15 downto 0);
signal enb_afis_sold:BIT;
signal suma_clr:Bit;
signal sumaConv : std_logic_vector(13 downto 0);
signal semn_verificare: bit;
signal errsuma:bit;
signal sum_neconv_afisare:std_logic_vector(15 downto 0);
signal clk2_suma: bit;
signal sig_terminare_sum:bit;
signal enb_afis_err_ex:bit;
signal not_semn_verificare:bit;
signal sig_prt_si_chit:bit;
signal out_interogare_sold_afis:std_logic_vector(13 downto 0);

component citire is	
	port ( start:in BIT;
	sw:in std_logic_vector(3 downto 0);
	clk :in std_logic;
	sel:in BIT_VECTOR(1 downto 0);
	nr_cont: out std_logic_vector(15 downto 0);
	terminare_card:out BIT
	);
end component citire;

component verificare_card is
	port(
	startvf:in bit;
	enable:in BIT;
	clk_placuta:in std_logic;
	cont_b:in std_logic_vector(15 downto 0);
	MARE,EGAL:out BIT;
	poz_card:out std_logic_vector(2 downto 0));
end component verificare_card;

component registru3 IS PORT(
    int	: IN STD_LOGIC_VECTOR(2 downto 0);
    pl  : IN BIT; -- load/enable.
    clr : IN bit; -- async. clear.
    clk : IN STD_LOGIC; -- clock.
    q   : OUT STD_LOGIC_VECTOR(2 downto 0) -- output.
);
END  component registru3;	 

component registru1 IS PORT(
    int	: IN BIT; 
	clr : in BIT;
    pl  : IN BIT; -- load/enable.
    clk : IN STD_LOGIC; -- clock.
    q   : OUT BIT -- output.
);
END component registru1;

component citire_PIN is			 
	port ( start_pin:in BIT;
	PIN_modif:in std_logic_vector(15 downto 0);
	write:in BIT;
	sw:in std_logic_vector(3 downto 0);
	clk :in std_logic;
	sel:in BIT_VECTOR(1 downto 0);
	poz_card_p:in std_logic_vector(2 downto 0);
	MIC1,MARE1,EGAL1:out BIT;
	PIN:out std_logic_vector(15 downto 0);
	terminare_PIN:out BIT);
end component citire_PIN;

component citire_pin_modificat is
	port( enable_mod_pin:in BIT;
	sw:in std_logic_vector(3 downto 0);
	clk :in std_logic;
	clr_modif_pin:in bit;
	sel:in BIT_VECTOR(1 downto 0);
	PIN_mod: out std_logic_vector(15 downto 0);
	terminare_pin_mod:out BIT);
end component citire_pin_modificat;

component DMUX_opt is
	port (enable:in BIT;
	sel_opt:in BIT_VECTOR(1 downto 0);
	interogare_sold:out BIT;
	schimbare_PIN:out BIT;
	retragere_numerar:out BIT;
	depunere_numerar:out BIT);
end component DMUX_opt;

component memorie_ram is
		port (
		we : in BIT;
		addr : in std_logic_vector(2 downto 0);
		di : in std_logic_vector(15 downto 0);
		do : out std_logic_vector(15 downto 0));
end component memorie_ram;

component divizor_frecventa is
	generic(Hz:INTEGER);
	port ( clk: in std_logic;
		clock_out: out std_logic);
end component divizor_frecventa;

component memorie_ram_conturi is
		port (
		ret: in bit;
		dep : in bit; 
		addr : in std_logic_vector(2 downto 0);
		suma : in std_logic_vector(13 downto 0); 
		clk2 : in bit;
		do_interogare:out std_logic_vector(13 downto 0); 
		do : out std_logic_vector(13 downto 0));
end component memorie_ram_conturi;

component interogare_sold is 
	port (Suma_cont:in std_logic_vector(13 downto 0);
	Suma_afisare:out std_logic_vector(15 downto 0));
end component interogare_sold;

component poarta_si_f is
	port(A,B:in BIT;
	Y:out BIT);
end component poarta_si_f;

component poarta_xor is
	port(A,B:in BIT;
	Y:out BIT);
end component poarta_xor;

component poarta_si is
	port(A,B:in BIT;
	Y:out BIT);
end component poarta_si;

component poarta_sau is
	port(A,B:in bit;
	Q:out bit);
end component poarta_sau;

component E7seg is
	port(enable_afisare_card:in BIT;
	enable_afisare_pin:in BIT;
	enable_afis_pin_mod:in BIT;
	enable_afis_int_sold:in BIT;
	enable_afis_ret:in BIT;
	enable_afis_dep:in BIT;
	enable_afisare_err:in BIT;
	enable_afisare_err_dep_ret : in BIT;
	enable_afisare_errp:in BIT;
	citire_card:in std_logic_vector(15 downto 0);
	citire_pin:in std_logic_vector(15 downto 0);
	modificare_PIN:in std_logic_vector(15 downto 0);
	interogare_sold_afisare: in std_logic_vector(15 downto 0);
	suma_dep_ret:in std_logic_vector(15 downto 0); 
	clk:in std_logic;
	a_g:out std_logic_vector(6 downto 0);
	anod1 : out  STD_LOGIC;
    anod2 : out  STD_LOGIC;
    anod3 : out  STD_LOGIC;
    anod4 : out  STD_LOGIC
	);
end component E7seg;

component inversor is
	port(A:in BIT;
	Y:out BIT);
end component inversor;

component comp_cit_verif is 
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
end component comp_cit_verif;

 component magazie is 
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
end component magazie;

begin
	citiire_card:citire port map(start_b,sw_b,clk_b,sel_b,cnt_cod,finish_card);	 
	divizor:divizor_frecventa generic map(199999) port map(clk_placa,clock_div);
	check_card:verificare_card port map(start_b,finish_card,clock_div,cnt_cod,BIG,corect,pozitie_card);
	poz_card_1:registru3 port map(pozitie_card,start_b,clear,clock_div,pozitie_card1);
	bistal_retinere:registru1 port map(corect,reset_reg,start_b_PIN,clock_div,iesire_bistabil_memorare);
	gate_and:poarta_si_f port map(start_b_pin,iesire_bistabil_memorare,out_gate_si_f);
	cit_pin: citire_PIN port map(out_gate_si_f,PIN_modificat,write_enable_PIN,sw_b,clk_b,sel_b,pozitie_card1,less,greater,pin_corect,S_pin,finish_pin);
	poarta_sau_pin: poarta_sau port map(start_b,start_b_pin,pl_pin);
	inversorasd: inversor port map(pl_pin,pl_pin_negat);
	bistal_retinerepin:registru1 port map(pin_corect,start_b,pl_pin_negat,clock_div,mem_pin);
	poarta_si1_afis: poarta_si port map(finish_card,corect,iesire_poarta_si_afis);
	enb_err:poarta_si port map(iesire_poarta_si_afis,iesire_bistabil_memorare,afis_err);
	poarta_si1_afispin: poarta_si port map(finish_pin,pin_corect,iesire_poarta_si_afis_p);
	enb_errpin:poarta_si port map(iesire_poarta_si_afis_p,mem_pin,afis_err_pin);
	afisare:E7seg port map(start_b,start_b_PIN,iesire_poarta_si_sch_pin,enb_afis_sold,retragere_bani,depunere_bani,afis_err,enb_afis_err_ex,afis_err_pin,cnt_cod,S_pin,PIN_modificat,s_afisare_int_sold,sum_neconv_afisare,clock_div,catodb,anod1b,anod2b,anod3b,anod4b);
	card<=iesire_bistabil_memorare;
	
	pin<=pin_corect;
	
	demux_optiuni:DMUX_opt port map(mem_pin,sel_opt,int_sold,switch_pin,retragere_bani,depunere_bani);
	inversorSt: inversor port map(start_b,suma_clr);
	retragere_depunere : comp_cit_verif port map(retragere_bani,depunere_bani,sw_b,clk_b,sel_b,suma_clr,out_memorare_cont,sumaConv,sum_neconv_afisare,sig_terminare_sum,semn_verificare);
	inversor_err: inversor port map(semn_verificare,not_semn_verificare);
	enb_afisare_err_extr:poarta_si_f port map( not_semn_verificare,sig_terminare_sum,enb_afis_err_ex);
	magazie_bani : magazie port map(retragere_bani,depunere_bani,semn_verificare,start_b_pin,sumaConv,clock_div,memorare_cont,clk2_suma);
	prt_si_schimbare_pin : poarta_si_f port map(pl_pin_negat,switch_pin,iesire_poarta_si_sch_pin);
	inv_clear_pin_modif: inversor port map(start_b_PIN,start_b_PIN_inv);
	cit_pin_mod:citire_pin_modificat port map(iesire_poarta_si_sch_pin,sw_b,clk_b,start_b_PIN_inv,sel_b,PIN_modificat,write_enable_PIN);
	mem_ram_conturi:memorie_ram_conturi port map(retragere_bani,depunere_bani,pozitie_card1,memorare_cont,clk2_suma,out_interogare_sold_afis,out_memorare_cont);
	operatie_int_sold:interogare_sold port map(out_interogare_sold_afis,s_afisare_int_sold);
	enb_afis_int_sold:poarta_si_f port map(pl_pin_negat,int_sold,enb_afis_sold);
	prt_si_1_chit: poarta_si_f port map(sig_terminare_sum,retragere_bani,sig_prt_si_chit);
	chitanta1:poarta_si_f port map(sig_prt_si_chit,chitanta,chitanta_out);
	sw_pin<=switch_pin;
	sold<=int_sold;
    ret<=retragere_bani;
    dep<=depunere_bani;
end architecture ARH_bancomat;
	