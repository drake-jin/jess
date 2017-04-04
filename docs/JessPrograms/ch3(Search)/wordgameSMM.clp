;;;======================================================
;;;
;;;          SEND 
;;;        + MORE
;;;         ------
;;;       = MONEY
;;;
;;;======================================================

(clear)
(reset)
(watch facts)
;(watch all)

(defrule startup
  =>
  (printout t "The problem is" crlf)
  (printout t "    SEND" crlf)
  (printout t " +  MORE" crlf)
  (printout t "   ------" crlf)
  (printout t " = MONEY" crlf)
  (assert 
;	  (number 0)
;	  (number 1)
          (number 2)
          (number 3)
          (number 4)
          (number 5)
          (number 6)
          (number 7)
          (number 8)
;         (number 9)
;         (letter S)
          (letter E)
          (letter N)
          (letter D)
;         (letter M)
;         (letter O)
          (letter R)
          (letter Y)  )  
)

(deftemplate combination (slot letter) (slot number) )

(assert (combination (letter M) (number 1) ) )
(assert (combination (letter O) (number 0) ) )
(assert (combination (letter S) (number 9) ) )

(defrule generate-combinations
  (number ?x)
  (letter ?a)
  =>
  (assert (combination (letter ?a) (number ?x) ) )
)

(defrule find-solution
  (combination (letter D) (number ?d) )
  (combination (letter E) (number ?e&~?d&:(neq e 8) ) )
  (combination (letter Y) (number ?y&~?e&~?d&:
			(= (mod (+ ?d ?e) 10) 
			    ?y)  )  ) ;Y
  (combination (letter N) (number ?n&~y&~?d&~?e&:(neq n 2)))
  (combination (letter R) (number ?r&~n&~?y&~?d&~?e) )
  (combination (letter E) (number ?e&~r&~?n&~?y&~?d&:
			(= (+ ?d ?e 
				   (* 10 ?n) (* 10 ?r)  )				
			   (+ 100 (* 10 ?e) ?y) )  )  ) ;EY

  (combination (letter N) (number ?n&~?r&:
			(= (+ ?d ?e 
			     (* 10 ?n) (* 10 ?r)
                             (* 100 ?e) (* 100 0)  )                             
			   (+ (* 100 ?n) (* 10 ?e) ?y) )  )  ) ;NEY
  =>
  (printout t "A Solution is:" crlf)
  (printout t "  S = " 9 crlf)
  (printout t "  E = " ?e crlf)
  (printout t "  N = " ?n crlf)
  (printout t "  D = " ?d crlf)
  (printout t "  M = " 1 crlf)
  (printout t "  O = " 0 crlf)
  (printout t "  R = " ?r crlf)
  (printout t "  Y = " ?y crlf)
  (printout t crlf)
  (printout t "   "    9 ?e ?n ?d crlf)
  (printout t " + "    1 0 ?r ?e crlf) 
  (printout t "  " "------" crlf)
  (printout t "= " 1 0 ?n ?e ?y crlf crlf)
)  

  (run)