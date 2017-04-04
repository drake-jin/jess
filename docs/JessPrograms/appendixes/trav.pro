DOMAINS
	treetype = tree(string, treetype, treetype) ; empty()
PREDICATES
	traverse(treetype)
CLAUSES
  	traverse(empty).
traverse(tree(Name,Left,Right)):-
		write(Name,'\n'),
		traverse(Left),
		traverse(Right).
GOAL
	traverse( tree("Cathy", 
		  tree("Michael",
 			tree("Charles", empty, empty),
			tree("Hazel", empty, empty)),
		tree("Melody",
			tree("Jim", empty, empty),
			tree("Eleanor", empty, empty)))).

