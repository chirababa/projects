entity inversor is
	port(A:in BIT;
	Y:out BIT);
end inversor;

architecture inv_gate of inversor is 
begin 
	Y<=not A;
end architecture inv_gate;