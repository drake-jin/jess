(clear)
(reset)
(watch all) ; ①
(deffunction grab-bananas ()
   (printout t "원숭이가 바나나를 잡는다" crlf) )  ; ②
(assert (원숭이손 empty)) ; ③
(defrule GRAB ; ④
           ?handFact <- (원숭이손 empty) ; ⑤
           =>
           (grab-bananas) ; ⑥
           (retract ?handFact ) ; ⑦
           (assert (원숭이손 grab) )  )  ; ⑧
(facts)
(run)
(facts)
