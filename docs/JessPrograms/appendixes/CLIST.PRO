/* Compound List */

DOMAINS
ilist = integer*
rlist = real*
slist = symbol*
clist = il(ilist); sl(slist); rl(rlist); i(integer); s(symbol)
llist = clist*
PREDICATES
	in_key
	length_of(llist,integer)

CLAUSES
  	length_of([], Result):-write("Count=",Result),nl.
  	length_of([_|T], Counter):-
		NewCounter = Counter + 1,
		length_of(T, NewCounter).

	in_key:-
   	 length_of([il([1999,1,2]), sl([sun, moon, fire]), rl([1999.0, 1.0, 2.0]), i(1), s(water)], 0).
   	 /* start with Counter = 0 */


