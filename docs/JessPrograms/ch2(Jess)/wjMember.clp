(clear)
(reset)
(watch all)

(deffunction FULL4LJUG () 
  (printout t "4L 항아리를 가득 채워라." crlf) )
(deffunction FULL3LJUG () 
  (printout t "3L 항아리를 가득 채워라." crlf) )
(deffunction EMPTY4LJUG () 
  (printout t "4L 항아리를 모두 비워라." crlf) )
(deffunction EMPTY3LJUG () 
  (printout t "3L 항아리를 모두 비워라." crlf) )
(deffunction POUR34FULL () 
  (printout t "3L 항아리에서 4L항아리의 아구까지 부어라." crlf) )
(deffunction POUR43FULL () 
  (printout t "4L 항아리에서 3L항아리의 아구까지 부어라." crlf) )
(deffunction POUR34 () 
  (printout t "3L 항아리에서 4L항아리로 모두 부어라." crlf) )
(deffunction POUR43 () 
  (printout t "4L 항아리에서 3L항아리로 모두 부어라." crlf) )
(deffunction goal () 
  (printout t "4L 항아리에 2L가 채워졌습니다." crlf)
  (printout t ?*wj-visited* crlf) )

(assert (4LJUG 0) )
(assert (3LJUG 0) )

(deffunction wj-Str (?4LJug ?3LJug)
  (bind ?wj-Str (str-cat  ?4LJug ?3LJug) )
  ?wj-Str  )

(deffunction add-wj-list (?*wj-visited* ?wj-Str1)
  (bind ?wj-visited2 (create$ ?*wj-visited* ?wj-Str1) ) 
  (return ?wj-visited2)  )

(bind ?*wj-visited* (create$ (wj-Str 0 0) ) )

(defrule FULL4LJUG
  ?FULL4L <- (4LJUG ?x&:(< ?x 4) )
  ?FULL3L <- (3LJUG ?y) 
  (test (not (member$ (wj-Str 4 ?y) ?*wj-visited*) ) )    
  =>
  (FULL4LJUG)
  (retract ?FULL4L) 
  (assert (4LJUG 4) ) 
  (bind ?*wj-visited* (add-wj-list ?*wj-visited* (wj-Str 4 ?y) ) )
)

(defrule FULL3LJUG
  ?FULL4L <- (4LJUG ?x) 
  ?FULL3L <- (3LJUG ?y&:(< ?y 3) )   
  (test (not (member$ (wj-Str ?x 3) ?*wj-visited*) ) )     
  =>
  (FULL3LJUG)
  (retract ?FULL3L) 
  (assert (3LJUG 3) )
  (bind ?*wj-visited* (add-wj-list ?*wj-visited* (wj-Str ?x 3) ) )
) 

(defrule EMPTY4LJUG
  ?EMPTY4L <- (4LJUG ?x&:(> ?x 0) )     
  ?EMPTY3L <- (3LJUG ?y)
  (test (not (member$ (wj-Str 0 ?y) ?*wj-visited*) ) )
  =>
  (EMPTY4LJUG)
  (retract ?EMPTY4L)
  (assert (4LJUG 0) ) 
  (bind ?*wj-visited* (add-wj-list ?*wj-visited* (wj-Str 0 ?y) ) )
)  

(defrule EMPTY3LJUG
  ?EMPTY4L <- (4LJUG ?x) 
  ?EMPTY3L <- (3LJUG ?y&:(> ?y 0) )  
  (test (not (member$ (wj-Str ?x 0) ?*wj-visited*) ) ) 
  =>
  (EMPTY3LJUG)
  (retract ?EMPTY3L)
  (assert (3LJUG 0) )   
  (bind ?*wj-visited* (add-wj-list ?*wj-visited* (wj-Str ?x 0) ) )
)  

(defrule POUR34FULL 
  ?POUR34F1 <- (4LJUG ?x&:(< ?x 4))
  ?POUR34F2 <- (3LJUG ?y&:
    (and (> ?y 0) (>= (+ ?y ?x) 4) )  ) 
  (test (not (member$ (wj-Str 4 (- ?y (- 4 ?x) ) ) ?*wj-visited*) ) )  
  =>
  (POUR34FULL)
  (bind ?y (- ?y (- 4 ?x) ) )
  (retract ?POUR34F1)
  (retract ?POUR34F2)
  (assert (4LJUG 4) )  
  (assert (3LJUG ?y) )   
  (bind ?*wj-visited* (add-wj-list ?*wj-visited* (wj-Str 4 ?y) ) )
) 

(defrule POUR43FULL
  ?POUR43F1 <- (3LJUG ?y&:(< ?y 3) )
  ?POUR43F2 <- (4LJUG ?x&:
    (and (> ?x 0) (>= (+ ?y ?x) 3) )  )  
  (test (not (member$ (wj-Str (- ?x (- 3 ?y) ) 3) ?*wj-visited*) ) ) 
  =>
  (POUR43FULL)
  (bind ?x (- ?x (- 3 ?y) ) )
  (retract ?POUR43F1)
  (retract ?POUR43F2)
  (assert (4LJUG ?x) )  
  (assert (3LJUG 3) )  
  (bind ?*wj-visited* (add-wj-list ?*wj-visited* (wj-Str ?x 3) ) )
) 

(defrule POUR34
  ?POUR341 <- (4LJUG ?x)
  ?POUR342 <- (3LJUG ?y&:
    (and (> ?y 0) (< (+ ?y ?x) 4) )  )  
  (test (not (member$ (wj-Str (+ ?x ?y) 0) ?*wj-visited*) ) ) 
  =>
  (POUR34)
  (bind ?x (+ ?x ?y) )
  (retract ?POUR341)
  (retract ?POUR342)
  (assert (4LJUG ?x) )  
  (assert (3LJUG 0) )  
  (bind ?*wj-visited* (add-wj-list ?*wj-visited* (wj-Str ?x 0) ) )
) 

(defrule POUR43
  ?POUR432 <- (3LJUG ?y) 
  ?POUR431 <- (4LJUG ?x&:
    (and (> ?x 0) (< (+ ?y ?x) 3) )  ) 
  (test (not (member$ (wj-Str 0 (+ ?x ?y) ) ?*wj-visited*) ) ) 
  =>
  (POUR43)
  (bind ?y (+ ?x ?y) ) 
  (retract ?POUR431)
  (retract ?POUR432)
  (assert (4LJUG 0) )  
  (assert (3LJUG ?y) ) 
  (bind ?*wj-visited* (add-wj-list ?*wj-visited* (wj-Str 0 ?y) ) )
) 

(defrule goal
  (declare (salience 100)) 
  (4LJUG ?x&:(= ?x 2) )    
  =>
  (goal) 
  (halt)  ) 

(run)

