/* recursion example: writing a list */

DOMAINS
	list = symbol* /* or whatever type you wish to use */

PREDICATES
	write_a_list(list)
	in_key

CLAUSES
  write_a_list([]). /* If the list is empty, do nothing more. */

  write_a_list([H|T]):- /* Match the head to H and the tail to T, then... */
	write(H),nl,
	write_a_list(T).

  in_key:-
        write_a_list([sun, moon, fire, water, tree, iron, soil]).
