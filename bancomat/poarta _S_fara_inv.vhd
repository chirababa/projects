entity poarta_si_f is
	port(A,B:in BIT;
	Y:out BIT);
end poarta_si_f;
architecture AND_gate_f of poarta_si_f is
begin
	Y<=A and B;
end architecture AND_gate_f;