library ieee ;
use ieee.std_logic_1164.all;

entity verificare_suma is 
	port(
	terminaresuma:in bit;
	enb_retragere:in BIT;
	enb_depunere:in BIT; 
	sum_cont: in std_logic_vector(13 downto 0);
	suma_introdusa_conversie:in std_logic_vector(13 downto 0);
	suma_introd_sw:in std_logic_vector(15 downto 0);
	verif_ok:out bit);
end verificare_suma;

architecture arh_verificare_suma of verificare_suma is	
signal t: bit; 
signal T_1000 : std_logic_vector (13 downto 0):="00001111101000";
begin 
	process(enb_retragere,enb_depunere,terminaresuma,sum_cont,suma_introdusa_conversie,suma_introd_sw)
	begin 
		if(terminaresuma='1' and suma_introd_sw /= "0000000000000000")then
		  if(suma_introdusa_conversie < T_1000 or suma_introdusa_conversie = T_1000 ) then
                if(enb_retragere='1')then
                    if (suma_introd_sw(3 downto 0)="0000")then 
                            if(suma_introdusa_conversie < sum_cont)  then
                                t<='1';
                            else
                                t<='0';
                            end if; 
                        else 
                            t<='0';
                        end if;		
                elsif(enb_depunere='1')then
                    if(suma_introd_sw(3 downto 0)="0000") then
                        t<='1';
                    else
                        t<='0';
                    end if;
                end if;
			else 
			     t<='0';
			end if;
		else 
			t<='0';
		end if;
	end process;
	verif_ok<=t;
end arh_verificare_suma;