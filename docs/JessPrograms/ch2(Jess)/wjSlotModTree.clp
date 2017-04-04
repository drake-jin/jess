(clear)
(watch all)

;;;*************
;;;* TEMPLATES *
;;;*************

;;; The status facts hold the state  
;;; information of the search tree.
 
(deftemplate MAIN::status 
   (slot search-depth)
   (slot parent)
   (multislot jugs) ; 4L-jug,  3L-jug  
   (slot last-move)  )

;;;*****************
;;;* INITIAL STATE *
;;;*****************

(deffacts MAIN::initial-positions
  (status (search-depth 1) 
          (parent root)
          (jugs 0 0)
          (last-move no-move)  )  )


;;;***********************
;;;* GENERATE PATH RULES *
;;;***********************

(defrule MAIN::FULL4LJUG
  ?node <- (status (search-depth ?num) 
                   (jugs ?x&:(< ?x 4) ?y)  )
  =>
  (duplicate ?node (search-depth (+ 1 ?num) )
                   (parent ?node)
                   (jugs 4 ?y)
                   (last-move FULL4LJUG)  )  
;(halt)
)

(defrule MAIN::FULL3LJUG
  ?node <- (status (search-depth ?num) 
                   (jugs ?x ?y&:(< ?y 3) )  )
  =>
  (duplicate ?node (search-depth (+ 1 ?num))
                   (parent ?node)
                   (jugs ?x 3)
                   (last-move FULL3LJUG)  )  
;(halt)
)   

(defrule MAIN::EMPTY4LJUG
  ?node <- (status (search-depth ?num) 
                   (jugs ?x&:(> ?x 0) ?y )  )
  =>
  (duplicate ?node (search-depth (+ 1 ?num))
                   (parent ?node)
                   (jugs 0 ?y)
                   (last-move EMPTY4LJUG)  )  
)  
 

(defrule MAIN::EMPTY3LJUG
  ?node <- (status (search-depth ?num) 
                   (jugs ?x ?y&:(> ?y 0) )  )
  =>
  (duplicate ?node (search-depth (+ 1 ?num))
                   (parent ?node)
                   (jugs ?x 0)
                   (last-move EMPTY3LJUG)  )  
) 
 
(defrule MAIN::POUR34FULL
  ?node <- (status (search-depth ?num) 
                   (jugs ?x&:(< ?x 4)
                         ?y&:(and (> ?y 0) (>= (+ ?y ?x) 4) )  )  )
  =>
  (bind ?y (- ?y (- 4 ?x) ) )
  (duplicate ?node (search-depth (+ 1 ?num))
                   (parent ?node)
                   (jugs 4 ?y)
                   (last-move POUR34FULL)  )  
)  
 
(defrule MAIN::POUR43FULL
  ?node <- (status (search-depth ?num) 
                   (jugs ?x&:(and (> ?x 0) (>= (+ ?y ?x) 3) )
                         ?y&:(< ?y 3)  )  )
  =>
  (bind ?x (- ?x (- 3 ?y) ) )
  (duplicate ?node (search-depth (+ 1 ?num))
                   (parent ?node)
                   (jugs ?x 3)
                   (last-move POUR43FULL)  )  
)  
 
(defrule MAIN::POUR34
  ?node <- (status (search-depth ?num) 
                   (jugs ?x
                         ?y&:(and (> ?y 0) (< (+ ?y ?x) 4) )  )  )
  =>
  (bind ?x (+ ?x ?y) )
  (duplicate ?node (search-depth (+ 1 ?num))
                   (parent ?node)
                   (jugs ?x 0)
                   (last-move POUR34)  )  
)  

(defrule MAIN::POUR43
  ?node <- (status (search-depth ?num) 
                   (jugs ?x&:(and (> ?x 0) (< (+ ?y ?x) 3) ) 
                         ?y  )  )
  =>
  (bind ?y (+ ?x ?y) )
  (duplicate ?node (search-depth (+ 1 ?num))
                   (parent ?node)
                   (jugs 0 ?y)
                   (last-move POUR43)  )  
) 

;;;******************************
;;;* CONSTRAINT VIOLATION RULES *
;;;******************************
 
(defmodule CONSTRAINTS)

(defrule CONSTRAINTS::circular-path 
  (declare (auto-focus TRUE));automatically executed whenever the rule is activated, default is FALSE 
  (status (search-depth ?sd1)
          (jugs ?x ?y)  )
  ?node <- (status (search-depth ?sd2&:(< ?sd1 ?sd2) )
                   (jugs ?x ?y) )
  =>
  (retract ?node) 
)

;;;*********************************
;;;* FIND AND PRINT SOLUTION RULES *
;;;*********************************

(defmodule SOLUTION)
       
(deftemplate SOLUTION::moves 
   (slot id)
   (multislot moves-list)  )

(defrule SOLUTION::recognize-solution 
  (declare (auto-focus TRUE) )
  ?node <- (status (parent ?parent)
                   (jugs 2 ?y)
                   (last-move ?move)  )
  =>
  (retract ?node)
  (assert (moves (id ?parent) (moves-list ?move) ) )  
)

(defrule SOLUTION::further-solution 
  ?node <- (status (parent ?parent)
                   (last-move ?move)  )
  ?mv <- (moves (id ?node) (moves-list $?rest) )
  =>
  (modify ?mv (id ?parent) (moves-list ?move ?rest) )  )

(defrule SOLUTION::print-solution 
  ?mv <- (moves (id root) (moves-list no-move $?m))
  =>
  (retract ?mv)
  (printout t crlf "2L 만들었어요!!! " crlf)
  (bind ?length (length$ ?m))
  (bind ?i 1)
  (while (<= ?i ?length)
     (bind ?thing (nth$ ?i ?m))
     (printout t " " ?thing "." crlf)
     (bind ?i (+ 1 ?i) )  ) )

(reset)
(run)
