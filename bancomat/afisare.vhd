library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.std_logic_unsigned.ALL;

entity E7seg is
	port(enable_afisare_card:in BIT;
	enable_afisare_pin:in BIT;
	enable_afis_pin_mod:in BIT;
	enable_afis_int_sold:in BIT;
	enable_afis_ret:in BIT;
	enable_afis_dep:in BIT;
	enable_afisare_err:in BIT;
	enable_afisare_err_dep_ret :in BIT;
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
end E7seg;

architecture arh of E7seg is
signal s_an_decod:std_logic_vector(3 downto 0);	 
 type states is (s0, s1, s2, s3);
    signal present_state, next_state: states;
    signal outp: std_logic_vector (0 to 3);

component decodificator_7seg is
port (bcd : in std_logic_vector(3 downto 0);  --BCD input
	segment7 : out std_logic_vector(6 downto 0)  -- 7 bit decoded output.
	);
end component decodificator_7seg;

begin
	
process (present_state)
	begin   
    case present_state is
        when s0 =>
            outp <= "0111";
            next_state <= s1;                
        when s1 =>
            outp <= "1011";
            next_state <= s2;    
        when s2 =>
            outp <= "1101";
            next_state <= s3;
        when s3 =>
            outp <= "1110";
            next_state <= s0;
    end case;
	end process;

process (clk)
begin
    if (rising_edge(clk)) then
        present_state <= next_state;
    end if;
end process;

anod1<=outp(0);
anod2<=outp(1);
anod3<=outp(2);
anod4<=outp(3);

process(outp)
begin
if (enable_afisare_card='1') then
	if enable_afisare_err='1' then
	case outp is 
		when "0111" => s_an_decod<="1011";
		when "1011" => s_an_decod<="1011";
		when "1101" => s_an_decod<="1010";
		when others => s_an_decod<="1110";
	end case;
	else 
		case outp is 
		when "0111" => s_an_decod<=citire_card(3 downto 0);
		when "1011" => s_an_decod<=citire_card(7 downto 4);
		when "1101" => s_an_decod<=citire_card(11 downto 8);
		when others => s_an_decod<=citire_card(15 downto 12);
		end case ;
	end if;
elsif enable_afisare_pin='1' then
	if enable_afisare_errp='1' then
	case outp is 
		when "0111" => s_an_decod<="1011";
		when "1011" => s_an_decod<="1011";
		when "1101" => s_an_decod<="1010";
		when others => s_an_decod<="1110";
	end case;
	else 
	case outp is 
		when "0111" => s_an_decod<=citire_pin(3 downto 0);
		when "1011" => s_an_decod<=citire_pin(7 downto 4);
		when "1101" => s_an_decod<=citire_pin(11 downto 8);
		when others => s_an_decod<=citire_pin(15 downto 12);
	end case;
	end if;
elsif enable_afis_pin_mod='1' then
	   case outp is 
		when "0111" => s_an_decod<=modificare_PIN(3 downto 0);
		when "1011" => s_an_decod<=modificare_PIN(7 downto 4);
		when "1101" => s_an_decod<=modificare_PIN(11 downto 8);
		when others => s_an_decod<=modificare_PIN(15 downto 12);
	end case;
elsif enable_afis_int_sold='1' then
	  case outp is 
		when "0111" => s_an_decod<=interogare_sold_afisare(3 downto 0);
		when "1011" => s_an_decod<=interogare_sold_afisare(7 downto 4);
		when "1101" => s_an_decod<=interogare_sold_afisare(11 downto 8);
		when others => s_an_decod<=interogare_sold_afisare(15 downto 12);
	end case;
elsif enable_afis_ret='1' then 
    if enable_afisare_err_dep_ret='1' then
	case outp is 
		when "0111" => s_an_decod<="1011";
		when "1011" => s_an_decod<="1011";
		when "1101" => s_an_decod<="1010";
		when others => s_an_decod<="1110";
	end case;
	else
	   case outp is 
		when "0111" => s_an_decod<=suma_dep_ret(3 downto 0);
		when "1011" => s_an_decod<=suma_dep_ret(7 downto 4);
		when "1101" => s_an_decod<=suma_dep_ret(11 downto 8);
		when others => s_an_decod<=suma_dep_ret(15 downto 12);
	end case;
	end if;
elsif  enable_afis_dep='1' then 
 if enable_afisare_err_dep_ret='1' then
   case outp is 
       when "0111" => s_an_decod<="1011";
       when "1011" => s_an_decod<="1011";
       when "1101" => s_an_decod<="1010";
       when others => s_an_decod<="1110";
   end case;
   else
	   case outp is 
		when "0111" => s_an_decod<=suma_dep_ret(3 downto 0);
		when "1011" => s_an_decod<=suma_dep_ret(7 downto 4);
		when "1101" => s_an_decod<=suma_dep_ret(11 downto 8);
		when others => s_an_decod<=suma_dep_ret(15 downto 12);
	end case;
	end if;

else
	case outp is 
            when "0111" => s_an_decod<="1110";
            when "1011" => s_an_decod<="1110";
            when "1101" => s_an_decod<="1110";
            when others => s_an_decod<="1110";
        end case; 
end if;
end process;
decod_7seg:decodificator_7seg port map(s_an_decod,a_g);
end architecture arh; 

