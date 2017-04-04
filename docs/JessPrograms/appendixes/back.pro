domains
	S = symbol
predicates
	likes(S,S)
	co_likes(S,S,S)
clauses
	likes(kim, pinky).     
	likes(lee, kim).		
	likes(kim, lee).		 
	likes(lee, beer).
	likes(lee, pinky).
	likes(lee, "H.O.T").
	co_likes(X,Y,Z) :- likes(X,Z), likes(Y,Z).	