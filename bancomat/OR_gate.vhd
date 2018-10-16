entity poarta_sau is
	port(A,B:in bit;
	Q:out bit);
end poarta_sau;

architecture OR_gate of poarta_sau is 
begin
	Q<=A or B;
end architecture OR_gate;