(clear)
(reset)
(watch all) ; ①
(deffunction goto-box ()
   (printout t "원숭이가 상자로 간다." crlf) )
(assert (원숭이위치 A)) ; ①
(assert (상자위치 B))  ; ②
(defrule GOTOBOX
   ?boxFact <- (상자위치 ?boxP) ; ③
   ?monkeyFact <- (원숭이위치 ~?boxP) ; ④
;   ?monkeyFact <- (원숭이위치 ?monkeyP&~?boxP )  ; ⑤ ④대신 사용
;   ?monkeyFact <- (원숭이위치 ?monkeyP&:(neq ?monkeyP ?boxP) )  ; ⑥ ④대신 사용
;   ?monkeyFact <- (원숭이위치 ?monkeyP&~?boxP|~B )  ; ⑦ ④대신 사용
;   ?monkeyFact <- (원숭이위치 ?monkeyP) (test (neq ?monkeyP ?boxP)) ; ⑧ ④대신 사용
   =>
   (goto-box)
   (retract ?monkeyFact)
   (assert (원숭이위치 ?boxP) )  ) 
(facts)
(run)
(facts)
