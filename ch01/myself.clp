(printout t (+ 2 3  ) crlf)

(printout t "Hello world[ "3"] this is wolrd"  crlf )

;this is integer type variable
(bind ?x 123123123)
(printout t "hello world [ " ?x "]" crlf)

;this is integer type variable
(bind ?x (long 12.2222222222))
(printout t "hello world [ " ?x "]" crlf)

;create list
(bind ?myList (create$ bread eggs milk))

(printout t ?myList crlf)
(printout t (rest$ ?myList) crlf)
(printout t (first$ ?myList) crlf)
;add some item in ?myList
(bind ?myList (create$ ?myList hello world my name is jin))

(printout t ?myList crlf)
(printout t (rest$ ?myList) crlf)
(printout t (first$ ?myList) crlf)

;for each grammer

(foreach ?item ?myList
    (printout t ?item crlf)
)
















