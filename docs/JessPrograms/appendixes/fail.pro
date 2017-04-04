domains
	S = symbol
predicates
	likes(S,S)
	c_likes1(S,S,S)
	c_likes2(S,S,S)
	no_back
	_fail
clauses
	likes(kim, pinky).     
	likes(lee, kim).		
	likes(kim, lee).		 
	likes(lee, beer).
	likes(kim, "Jackson").
	likes(lee, pinky).
	likes(lee, "Jackson").
	c_likes1(X,Y,Z) :- likes(X,Z), likes(Y,Z).			
	c_likes2(X,Y,Z) :- likes(X,Z), likes(Y,Z), !.			
	no_back:- c_likes1(lee,kim,Z), write("Z=",Z), nl.
	_fail:- c_likes1(lee,kim,Z), write("Z=",Z), nl, fail.
