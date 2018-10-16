entity poarta_si is
	port(A,B:in BIT;
	Y:out BIT);
end poarta_si;

architecture AND_gate of poarta_si is
component inversor is
	port(A:in BIT;
	Y:out BIT);
end component;
signal inv_out:BIT;
begin
	I1:inversor port map(B,inv_out);
	Y<=A and inv_out;
end architecture AND_gate;