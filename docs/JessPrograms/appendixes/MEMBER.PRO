/* member */
domains
       x = symbol
       y = x*   /* y: list of symbol */       
predicates
       member(x,y).
clauses
       member(M,[M|T]).
       member(M,[H|T]):- member(M,T).
