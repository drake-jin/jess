(clear)
(reset)
(watch all)

;;;*************
;;;* TEMPLATES *
;;;*************

;;; The status facts hold the state  
;;; information of the search tree.

(deftemplate MAIN::status 
   (slot search-depth)
   (slot parent)
   (multislot mcb) ; missionaries, cannibals, boat
   (slot last-move)  )
   
;;;*****************
;;;* INITIAL STATE *
;;;*****************

(deffacts MAIN::initial-positions
  (status (search-depth 1) 
          (parent root)
          (mcb 3 3 1)
          (last-move no-move)  )  )

;;;******************
;;;* SAFE CONDITION *
;;;******************

(deffunction safe-cond (?m ?c)
  (if (and (or (= ?m 0) (>= ?m ?c) )
           (or (= (- 3 ?m) 0) (>= (- 3 ?m) (- 3 ?c) )  )  )
  then (return TRUE)
  )
)

;;;***********************
;;;* GENERATE PATH RULES *
;;;***********************

(defrule MAIN::move-M_1to2 
  ?node <- (status (search-depth ?num) 
                   (mcb ?m ?c 1)  )
  (test (and (safe-cond (- ?m 1) ?c) (> ?m 0) ) )    
  =>
  (duplicate ?node (search-depth (+ 1 ?num) )
                   (parent ?node)
                   (mcb (- ?m 1) ?c 2)
                   (last-move M_1to2)  )  
)

(defrule MAIN::move-M_2to1 
  ?node <- (status (search-depth ?num) 
                   (mcb ?m ?c 2)  )
  (test (and (safe-cond (+ ?m 1) ?c) (> (- 3 ?m) 0) ) )    
  =>
  (duplicate ?node (search-depth (+ 1 ?num) )
                   (parent ?node)
                   (mcb (+ ?m 1) ?c 1)
                   (last-move M_2to1)  )  
)

(defrule MAIN::move-C_1to2 
  ?node <- (status (search-depth ?num) 
                   (mcb ?m ?c 1)  )
  (test (and (safe-cond ?m (- ?c 1) ) (> ?c 0) ) )
  =>
  (duplicate ?node (search-depth (+ 1 ?num) )
                   (parent ?node)
                   (mcb ?m (- ?c 1) 2)
                   (last-move C_1to2)  )  
)

(defrule MAIN::move-C_2to1 
  ?node <- (status (search-depth ?num) 
                   (mcb ?m ?c 2)  )
  (test (and (safe-cond ?m (+ ?c 1) ) (> (- 3 ?c) 0) ) )
  =>
  (duplicate ?node (search-depth (+ 1 ?num) )
                   (parent ?node)
                   (mcb ?m (+ ?c 1) 1)
                   (last-move C_2to1)  )  
)

(defrule MAIN::move-MC_1to2 
  ?node <- (status (search-depth ?num) 
                   (mcb ?m ?m 1)  )
  (test (> ?m 0) ) 
  =>
  (duplicate ?node (search-depth (+ 1 ?num) )
                   (parent ?node)
                   (mcb (- ?m 1) (- ?m 1) 2)
                   (last-move MC_1to2)  )  
)

(defrule MAIN::move-MC_2to1 
  ?node <- (status (search-depth ?num) 
                   (mcb ?m ?m 2)  )
  (test (> (- 3 ?m) 0) ) 
  =>
  (duplicate ?node (search-depth (+ 1 ?num) )
                   (parent ?node)
                   (mcb (+ ?m 1) (+ ?m 1) 1)
                   (last-move MC_2to1)  )  
)

(defrule MAIN::move-MM_1to2 
  ?node <- (status (search-depth ?num) 
                   (mcb ?m ?c 1)  )
  (test (and (safe-cond (- ?m 2) ?c) (> ?m 1) ) )    
  =>
  (duplicate ?node (search-depth (+ 1 ?num) )
                   (parent ?node)
                   (mcb (- ?m 2) ?c 2)
                   (last-move MM_1to2)  )  
)

(defrule MAIN::move-MM_2to1 
  ?node <- (status (search-depth ?num) 
                   (mcb ?m ?c 2)  )
  (test (and (safe-cond (+ ?m 2) ?c) (> (- 3 ?m) 1) ) )    
  =>
  (duplicate ?node (search-depth (+ 1 ?num) )
                   (parent ?node)
                   (mcb (+ ?m 2) ?c 1)
                   (last-move MM_2to1)  )  
)

(defrule MAIN::move-CC_1to2 
  ?node <- (status (search-depth ?num) 
                   (mcb ?m ?c 1)  )
  (test (and (safe-cond ?m (- ?c 2) ) (> ?c 1) ) )
  =>
  (duplicate ?node (search-depth (+ 1 ?num) )
                   (parent ?node)
                   (mcb ?m (- ?c 2) 2)
                   (last-move CC_1to2)  )  
)

(defrule MAIN::move-CC_2to1 
  ?node <- (status (search-depth ?num) 
                   (mcb ?m ?c 2)  )
  (test (and (safe-cond ?m (+ ?c 2) ) (> (- 3 ?c) 1) ) )
  =>
  (duplicate ?node (search-depth (+ 1 ?num) )
                   (parent ?node)
                   (mcb ?m (+ ?c 2) 1)
                   (last-move CC_2to1)  )  
)

;;;******************************
;;;* CONSTRAINT VIOLATION RULES *
;;;******************************

(defmodule CONSTRAINTS)

(defrule CONSTRAINTS::circular-path 
  (declare (auto-focus TRUE))
  (status (search-depth ?sd1)
          (mcb ?m ?c ?b)  )
  ?node <- (status (search-depth ?sd2&:(< ?sd1 ?sd2) )
                   (mcb ?m ?c ?b)  )
  =>
  (retract ?node) 
)

;;;*********************************
;;;* FIND AND PRINT SOLUTION RULES *
;;;*********************************

(defmodule SOLUTION)
       
(deftemplate SOLUTION::moves 
   (slot id)
   (multislot moves-list))


(defrule SOLUTION::recognize-solution 
  (declare (auto-focus TRUE))
  ?node <- (status (parent ?parent)
                   (mcb 0 0 2)
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
  (printout t crlf "무사히 건너갔어요!!!: " crlf crlf)
  (bind ?length (length$ ?m) )
  (bind ?i 1)
  (bind ?boat 2)
  (while (<= ?i ?length)
    (bind ?thing (nth$ ?i ?m))
    (printout t ?thing "  " ?boat "로 건넌다." crlf)
    (if (eq ?boat 1)
       then (bind ?boat 2)
     else (bind ?boat 1)  )
    (bind ?i (+ 1 ?i) )      
  )  
  (store cannibalState ?m)
)

(reset)
(run)