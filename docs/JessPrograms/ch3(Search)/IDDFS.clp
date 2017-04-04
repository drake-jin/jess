(clear)
(deftemplate MAIN::idbfs (slot depth-limit) )  
(deftemplate MAIN::status 
   (slot search-depth)
   (slot parent)
   (multislot jugs) 
   (slot last-move)  )
(deffacts MAIN::initial-positions
  (status (search-depth 1) 
          (parent root)
          (jugs 0 0)
          (last-move no-move) )  )
(deffacts MAIN::IDBFS
  (idbfs (depth-limit 2) )  )
(defrule MAIN::FULL4LJUG
  ?node <- (status (search-depth ?num) 
                   (jugs ?x&:(< ?x 4) ?y)  )
  ?dlFact <- (idbfs (depth-limit ?dl) ) 
  (test (<= ?num ?dl) ) 		 
  =>
  (duplicate ?node (search-depth (+ 1 ?num))
                   (parent ?node)
                   (jugs 4 ?y)
                   (last-move FULL4LJUG)  )  )
(defrule MAIN::FULL3LJUG
  ?node <- (status (search-depth ?num) 
                   (jugs ?x ?y&:(< ?y 3) )  )
  ?dlFact <- (idbfs (depth-limit ?dl) )
  (test (<= ?num ?dl) )
  =>
  (duplicate ?node (search-depth (+ 1 ?num))
                   (parent ?node)
                   (jugs ?x 3)
                   (last-move FULL3LJUG)  )  )   
(defrule MAIN::EMPTY4LJUG
  ?node <- (status (search-depth ?num) 
                   (jugs ?x&:(> ?x 0) ?y )  )
  ?dlFact <- (idbfs (depth-limit ?dl) )
  (test (<= ?num ?dl) )
  =>
  (duplicate ?node (search-depth (+ 1 ?num))
                   (parent ?node)  
                   (jugs 0 ?y)
                   (last-move EMPTY4LJUG)  )  )  
(defrule MAIN::EMPTY3LJUG
  ?node <- (status (search-depth ?num) 
                   (jugs ?x ?y&:(> ?y 0) )  )
  ?dlFact <- (idbfs (depth-limit ?dl) )
  (test (<= ?num ?dl) )
  =>
  (duplicate ?node (search-depth (+ 1 ?num))
                   (parent ?node)
                   (jugs ?x 0)
                   (last-move EMPTY3LJUG)  )  )  
(defrule MAIN::POUR34FULL
  ?node <- (status (search-depth ?num) 
                   (jugs ?x&:(< ?x 4)
                         ?y&:(and (> ?y 0) (>= (+ ?y ?x) 4) )  )  )
  ?dlFact <- (idbfs (depth-limit ?dl) )
  (test (<= ?num ?dl) )
  =>
  (bind ?y (- ?y (- 4 ?x) ) )
  (duplicate ?node (search-depth (+ 1 ?num))
                   (parent ?node)
                   (jugs 4 ?y)
                   (last-move POUR34FULL)  )  )   
(defrule MAIN::POUR43FULL
  ?node <- (status (search-depth ?num) 
                   (jugs ?x&:(and (> ?x 0) (>= (+ ?y ?x) 3) )
                         ?y&:(< ?y 3)  )  )
  ?dlFact <- (idbfs (depth-limit ?dl) )
  (test (<= ?num ?dl) )
  =>
  (bind ?x (- ?x (- 3 ?y) ) )
  (duplicate ?node (search-depth (+ 1 ?num))
                   (parent ?node)
                   (jugs ?x 3)
                   (last-move POUR43FULL)  )  )   
(defrule MAIN::POUR34
  ?node <- (status (search-depth ?num) 
                   (jugs ?x
                         ?y&:(and (> ?y 0) (< (+ ?y ?x) 4) )  )  )
  ?dlFact <- (idbfs (depth-limit ?dl) )
  (test (<= ?num ?dl) )
  =>
  (bind ?x (+ ?x ?y) )
  (duplicate ?node (search-depth (+ 1 ?num))
                   (parent ?node)
                   (jugs ?x 0)
                   (last-move POUR34)  )  )  
(defrule MAIN::POUR43
  ?node <- (status (search-depth ?num) 
                   (jugs ?x&:(and (> ?x 0) (< (+ ?y ?x) 3) ) 
                         ?y  )  )
  ?dlFact <- (idbfs (depth-limit ?dl) )
  (test (<= ?num ?dl) )
  =>
  (bind ?x (+ ?x ?y) )
  (duplicate ?node (search-depth (+ 1 ?num))
                   (parent ?node)
                   (jugs ?x 0)
                   (last-move POUR43)  )  ) 
(defrule MAIN::no-more-rules
  (declare (salience -100))   ; ③
  ?sdFact <- (idbfs (depth-limit ?sd) )
  =>
(modify ?sdFact (depth-limit (+ ?sd 1) ) ) ; ④
(printout t crlf "Search Depth=" ?sd crlf)  )
 
(defmodule CONSTRAINTS)
(defrule CONSTRAINTS::circular-path 
  (declare (auto-focus TRUE))
  (status (search-depth ?sd1)
          (jugs ?x ?y)  )
  ?node <- (status (search-depth ?sd2&:(< ?sd1 ?sd2) )
                   (jugs ?x ?y) )
  =>
(retract ?node) )

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
  (assert (moves (id ?parent) (moves-list ?move) ) )  )
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
     (bind ?i (+ 1 ?i) )  )  
  (halt)  
)
(reset)
(run)