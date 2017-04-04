/* missionaries and canibals problem */
domains
    	cnt = integer
    	m_c = cnt*
    	visited = m_c*
   	r = symbol
   	rule = r* 
predicates
	safe_cond(cnt, cnt)
	s(rule, m_c, m_c)
	solve(m_c, visited)
	member(m_c, visited)
	in_key
clauses	
	safe_cond(M,C):-
		M=0 or M>=C.
/* across a missionary to the other side */
	s([r1,m_1to2], [M1,C,1], [M2,C,2]):- 
		M1>=1, ML=M1-1, MR=(3-M1)+1, CR=(3-C), 
		safe_cond(ML,C), safe_cond(MR,CR), M2=M1-1.
	s([r2,m_2to1], [M1,C,2], [M2,C,1]):- 
		3-M1>=1, ML=M1+1, MR=(3-M1)-1, CR=(3-C), 
		safe_cond(ML,C), safe_cond(MR,CR), M2=M1+1.

/* across a canibal to the other side */
	s([r3,c_1to2], [M,C1,1], [M,C2,2]):- 
		C1>=1, CL=C1-1, CR=(3-C1)+1, MR=(3-M), 
		safe_cond(M,CL), safe_cond(MR,CR), C2=C1-1.
	s([r4,c_2to1], [M,C1,2], [M,C2,1]):- 
		3-C1>=1, CL=C1+1, CR=(3-C1)-1, MR=(3-M), 
		safe_cond(M,CL), safe_cond(MR,CR), C2=C1+1.

/* across a missionary & a canibal to the other side */
	s([r5,mc_1to2], [M1,C1,1], [M2,C2,2]):- 
		M1=C1,C1>0, ML=M1-1, MR=(3-M1)+1, CL=C1-1, CR=(3-C1)+1,
		safe_cond(MR,CR), safe_cond(MR,CR), M2=M1-1, C2=C1-1.
	s([r6,mc_2to1], [M1,C1,2], [M2,C2,1]):- 
		(3-M1)=(3-C1),(3-C1)>0, ML=M1+1, MR=(3-M1)-1, CL=C1+1, CR=(3-C1)-1,
		safe_cond(MR,CR), safe_cond(MR,CR), M2=M1+1, C2=C1+1.

/* across 2 missionaries to the other side */
	s([r7,mm_1to2], [M1,C,1], [M2,C,2]):- 
		M1>=2, ML=M1-2, MR=(3-M1)+2, CR=(3-C), 
		safe_cond(ML,C), safe_cond(MR,CR), M2=M1-2.
	s([r8,mm_2to1], [M1,C,2], [M2,C,1]):- 
		3-M1>=2, ML=M1+2, MR=(3-M1)-2, CR=(3-C), 
		safe_cond(ML,C), safe_cond(MR,CR), M2=M1+2.

/* across 2 canibals to the other side */
	s([r9,cc_1to2], [M,C1,1], [M,C2,2]):- 
		C1>=2, CL=C1-2, CR=(3-C1)+2, MR=(3-M), 
		safe_cond(M,CL), safe_cond(MR,CR), C2=C1-2.
	s([r10,cc_2to1], [M,C1,2], [M,C2,1]):- 
		3-C1>=2, CL=C1+2, CR=(3-C1)-2, MR=(3-M), 
		safe_cond(M,CL), safe_cond(MR,CR), C2=C1+2.

	solve([0,0,2],_):-!. 
	solve([M,C,B],V):-
		s(N,[M,C,B], Next),
		not(member(Next,V)),
		solve(Next,[Next|V]),
	            write(" ",Next," ",N),nl. 

	/* subroutine for member */				
	member(X,[X|_]) :- !.
	member(X,[_|T]):- member(X,T).				
				
            /* input from user: Goal: in_key */
	in_key:-write("\n [M,C,B] rule"), nl,
            	solve([3,3,1],[[3,3,1]]).    	        
  
