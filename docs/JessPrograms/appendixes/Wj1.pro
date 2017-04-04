/* water-jug1 problem */

domains
    amount = integer
    X=symbol
    rule=X*
    jugs = amount*
    visited = jugs*
    
predicates
	s(rule, jugs, jugs)
	solve(jugs, visited)
	member(jugs, visited)
	in_key

clauses	
	s([r1,f4], [X,Y], [4,Y]):- X < 4.
	s([r2,f3], [X,Y], [X,3]):- Y < 3.
	s([r3,e4], [X,Y], [0,Y]):- X > 0.
	s([r4,e3], [X,Y], [X,0]):- Y > 0.
	s([r5,p3To4F], [X,Y], [4,Y1]):- X+Y>=4,Y>0,Y1=Y-(4-X).
	s([r6,p4To3F], [X,Y], [X1,3]):- X+Y>=3,X>0,X1=X-(3-Y).
	s([r7,p3To4], [X,Y], [Z,0]):- X+Y <= 4, Y > 0, Z =X+Y.
	s([r8,p4To3], [X,Y], [0,W]):- X+Y <= 3, X > 0, W =X+Y.
	
	solve([2,_], _). 
	
	solve([A,B], V):-
		s(N,[A,B], Next),
		not(member(Next,V)), 
		solve(Next, [Next|V]),
	        write(Next,"  ",N),nl.  
	
	/* subroutine for member */				
	member(X,[X|_]) :- !.
	member(X,[_|T]):- member(X,T).				
			    			
        in_key:- solve([0,0],[[0,0]]).    	        

goal	in_key.