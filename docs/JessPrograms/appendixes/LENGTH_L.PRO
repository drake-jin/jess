/* number of list items  */

domains
        X = symbol
	Y = X*
	I = integer
predicates
	length_L(Y,I).
	in_key.
clauses
	length_L([],L):-write("length=",L),nl.
	length_L([H|L],N):-
		N1=N+1,
		length_L(L,N1).
	in_key:-length_L([],0).
    