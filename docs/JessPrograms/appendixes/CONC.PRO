/* concatenation */

domains
        X = symbol
	Y = X*
predicates
	conc(Y,Y,Y).
clauses	
	conc([],L2,L2).
	conc([H|L1],L2,[H|After]):-conc(L1,L2,After).
    