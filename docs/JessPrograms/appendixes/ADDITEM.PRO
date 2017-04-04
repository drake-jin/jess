/* add an item to a list, if not exists */

domains
        X = symbol
	Y = X*
predicates
	member(X,Y)
	additem(X,Y,Y)
clauses
    additem(X,L1,[X|L1]):- not (member(X,L1)). 
    additem(X,L1,L1):-member(X,L1).
    /* member subroutine */
    member(M,[M|T]).
    member(M,[H|T]):- member(M,T).
