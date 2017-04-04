/* recursion example: counting list items */
DOMAINS	
	list = symbol* /* 리스트 선언 */

PREDICATES
	in_key
	length_of(list,integer)

CLAUSES
  	length_of([], Result):-write("Count=",Result),nl.
  	length_of([_|T], Counter):-
		NewCounter = Counter + 1,
		length_of(T, NewCounter).

	in_key:-
   	length_of([sun, moon, fire, water, tree, iron, soil], 0).
   	 /* start with Counter = 0 */


