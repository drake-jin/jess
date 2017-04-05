; 인공지능 퀘스트에 있는 예제
; 기본적인 예제다. 

(clear)
(reset)
(watch all)

(deffunction goto-box()
    (printout t "" crlf)
    (printout t "[System]:원숭이가 상자로 간다" crlf)   
)
(deffunction goto-bananas()
    (printout t "" crlf)
    (printout t "[System]:원숭이가 바나나로 간다 " crlf)
)

(deffunction push-box() 
    (printout t "" crlf)
    (printout t "[System]:원숭이가 상자를 바나나로 민다." crlf)
)
(deffunction climb-box()
    (printout t "" crlf)
    (printout t "[System]:원숭이가 상자에 올랐다." crlf)
)
(deffunction grab-banananas()
    (printout t "" crlf)
    (printout t "[System]:원숭이가 바나나를 잡았다." crlf)
)



(assert (원숭이위치 A))
(assert (원숭이손 empty))
(assert (원숭이발 onFloor))
(assert (상자위치 B))
(assert (바나나위치 C))

(defrule GOTOBOX 
    ?boxFact <-(상자위치 ?boxP)
    ?monkeyFact <-(원숭이위치 ~?boxP)
    ?handFact <-(원숭이손 empty)
    ?feetFact <-(원숭이발 onFloor)
    =>
    (goto-box)
    (retract ?monkeyFact)
    (assert (원숭이위치 ?boxP))
)

(defrule GOTOBANANAS
    ;(declare (salience -100))  ; 우선순위 배정
    ?bananasFact <-(바나나위치 ?bananasP)
    ?monkeyFact <-(원숭이위치 ~?bananasP)
    ?handFact <-(원숭이손 empty)
    ?feetFact <-(원숭이발 onFloor)
    =>
    (goto-bananas)
    (retract ?monkeyFact)
    (assert (원숭이위치 ?bananasP))
)

(defrule PUSHBOX 
    ?bananasFact <-(바나나위치 ?bananasP)
    ?monkeyFact <-(원숭이위치 ?monkeyP)
    ?boxFact <-(상자위치 ?boxP &:(eq ?boxP ?monkeyP) &:(neq ?boxP ?bananasP))
    ?handFact <-(원숭이손 empty)
    ?feetFact <-(원숭이발 onFloor)
    =>
    (push-box)
    (retract ?monkeyFact)
    (retract ?boxFact)
    (assert (원숭이위치 ?bananasP))
    (assert (상자위치 ?bananasP))
)

(defrule CLIMBOX
    ?monkeyFact <-(원숭이위치 ?monkeyP)
    ?boxFact <-(상자위치 ?boxP&: (eq ?boxP ?monkeyP))
    ?handFact <-(원숭이손 empty)
    ?feetFact <-(원숭이발 onFloor)
    ?bananasFact <-(바나나위치 ?bananasP&:(eq ?bananasP ?monkeyP))
    =>  
    (climb-box)
    (retract ?feetFact)
    (assert (원숭이발 onBox))

)

(defrule GRAB
    ?monkeyFact <-(원숭이위치 ?monkeyP)
    ?bananasFact <-(바나나위치 ?bananasP&:(eq ?bananasP ?monkeyP))
    ?boxFact <-(상자위치 ?boxP&:(eq ?boxP ?monkeyP))
    ?handFact <-(원숭이손 empty)
    ?feetFact <-(원숭이발 onBox)
    =>
    (grab-banananas)
    (retract ?handFact)
    (assert (원숭이손 grab))
)

(run)
