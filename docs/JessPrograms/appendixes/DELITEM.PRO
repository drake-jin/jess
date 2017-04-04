/* deleting an item */

domains
        X = symbol
	Y = X*
predicates
	DelItem(X,Y,Y)
clauses
	DelItem(H,[H|T],T):-!.
	DelItem(H,[],[]):-!.
	DelItem(X,[H|L1],[H|L2]):- DelItem(X,L1,L2).	

    