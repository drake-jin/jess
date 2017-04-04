
domains
    dist = integer
    X = symbol
    visited = X*
predicates
	path(X, X, dist)
	solve(X,X,dist,visited)
	member(X,visited)
	in_key
clauses	
path(seoul, inchun, 50).	path(seoul, daejun, 140). path(seoul, choonchun, 100).
path(inchun, seoul, 50).	path(inchun, daejun, 150). path(inchun, koonsan, 180).
path(daejun, seoul, 140). path(daejun, inchun, 150). path(daejun, koonsan, 80).
path(daejun, kwangjoo, 180). path(daejun, taekoo, 110). path(daejun, wonjoo, 70).
path(taekoo, daejun, 110). path(taekoo, masan, 60). path(taekoo, pusan, 130).
path(taekoo, wonjoo, 140). path(taekoo, kwnagjoo, 200).
path(pusan, taekoo, 130). path(pusan, masan, 50).
path(masan, kwangjoo, 170). path(masan, pusan, 50). path(masan, taekoo, 60). 
path(wonjoo, daejun, 70). path(wonjoo, taekoo, 140). path(wonjoo, choonchun, 60).  
path(choonchun,wonjoo,60).  path(choonchun, seoul, 100). 
path(kwangjoo, daejun, 180). path(kwangjoo, masan, 170). 
path(kwnagjoo, taekoo, 200). path(kwangjoo,  koonsan, 90). 
path(koonsan, kwangjoo, 90). path(koonsan, daejun, 80). 
path(koonsan, inchun, 180).

	solve(City,Last,Dist,_):- 
		path(City,Last,Dist). 
	
	solve(Start,Last,Dist,V):-
		path(Start,Next,Dist1),
		not(member(Next,V)),
		solve(Next, Last, Dist2, [Next|V]), 
		Dist = Dist1 + Dist2,
		write("  ",Next,"  "),nl.

  /* subroutine for member */
  member(X,[X|_]) :- !.
  member(X,[_|T]):- member(X,T).

  in_key:-write("Destination: pusan"),nl,
          solve(seoul,pusan,Dist,[seoul,pusan]),
          write("Start: seoul"),nl,
          write("Distance: ", Dist),nl.
