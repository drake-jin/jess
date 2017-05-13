(clear)
(reset)
(watch all)

(assert (원숭이위치 A))
(assert (원숭이손 empty))
(assert (원숭이발 onFloor))
(assert (상자위치 B))
(assert (바나나위치 C))

(defrule GOTOBOX 
    ?boxPFact <-(상자위치 ?boxP)
    ?monkeyPFact <- (원숭이위치 ?monkeyP&~ ?boxP)
    ?bananaPFact <-(바나나위치 ?bananaP &:(eq ?bananaP C))
    =>
    (printout t "GOTOBOX " crlf)
    (retract ?monkeyPFact)
    (assert (원숭이위치 B))
)

(defrule GOTOBANANAS 
    ?monkeyPFact <- (원숭이위치 ?monkeyP &: (neq ?monkeyP C))
    ?bananaPFact <- (바나나위치 ?bananaP & C)
    ?boxPFact <- (상자위치 ?boxP &:(eq ?boxP ?bananaP))
    ?monkeyFeetFact <-(원숭이발 ?monkeyFeet &:(neq ?monkeyFeet onBox))
    ?monkeyHandFact <- (원숭이손 empty)
    =>
    (printout t "GOTOBANANAS " crlf)
    (retract ?monkeyPFact)
    (assert (원숭이위치 C))
)

(defrule PUSHBOX
    ?monkeyPFact <- (원숭이위치 B)
    ?boxPFact <- (상자위치 B)
    ?bananaPFact <- (바나나위치 C)
    =>
    (printout t "PUSHBOX " crlf)
    (retract ?boxPFact)
    (assert (상자위치 C))
)


(defrule CLIMBUP
    ?monkeyPFact <- (원숭이위치 C)
    ?boxPFact <- (상자위치 C)
    ?bananaPFact <- (바나나위치 C)
    ?monkeyFeetFact <- (원숭이발 onFloor)
    ?monkeyHandFact <- (원숭이손 empty)
    =>
    (printout t "CLIMBUP" crlf)
    (retract ?monkeyFeetFact)
    (assert (원숭이발 onBox))
)


(run)
