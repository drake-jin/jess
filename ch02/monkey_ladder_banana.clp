; # 전제 조건

; 1. 짧은 사다리 2개가 있다.
; 2. 원숭이는 이 사다리를 하나로 합쳐서 바나나를 잡을 수 있다.
; 3. 바나나는 천장에 매달려 있다
; 4. 사다리 A, B는 각각 다른 위치에 있다.


; # 수행순서 
;  1. 사다리 A를 가지고 온다
;  2. 사다리 A를 가지고 사다리 B로 간다.
;  3. 사다리 2개를 합쳐서 하나의 사다리로 만든다 
;  4. 사다리를 합친걸 가지고 바나나로 간다
;  5. 바나나자리에서 사다리를 타고 올라간다.
;  6. 사다리에 올라가서 바나나를 잡는다.

; init this program
(clear)
(reset)
(watch all)

; define each method of print messages
(deffunction grab-banananas()
    (printout t "" crlf)
    (printout t "[System]:원숭이가 바나나를 잡았다 " crlf)
)
(deffunction getladder_a()
    (printout t "" crlf)
    (printout t "[System]:원숭이가 사다리A를 획득했다" crlf)
)
(deffunction getladder_b()
    (printout t "" crlf)
    (printout t "[System]:원숭이가 사다리B를 획득했다 " crlf)
)
(deffunction assembly() 
    (printout t "" crlf)
    (printout t "[System]:원숭이가 사다리를 조립한다," crlf)
)
(deffunction goto-bananas()
    (printout t "" crlf)
    (printout t "[System]:원숭이가 바나나로 간다 " crlf)
)
(deffunction climb() 
    (printout t "" crlf)
    (printout t "[System]:원숭이가 사다리에 오른다" crlf)
)
(deffunction grab() 
    (printout t "" crlf)
    (printout t "[System]:원숭이가 바나나를 잡았다" crlf)
)
(deffunction yammy() 
    (printout t "" crlf)
    (printout t "" crlf)
    (printout t "[System]:===============아아아아 원숭띠 이이==============" crlf)
    (printout t "[System]:===============맛있게 잘 먹는다느응==============" crlf)
    (printout t "[System]:=============== 냠냠냠냠냠냠냠냠냠 ==============" crlf)
)
; load facts on work memory area
(assert (원숭이위치 A))
(assert (원숭이손 empty))
(assert (원숭이발 onFloor))
(assert (사다리조립 false))
(assert (바나나위치 B))
(assert (사다리A위치 D))
(assert (사다리B위치 C))

; define rules 

; getting ladder A
(defrule GETLADDER_A
    ?handFact <-(원숭이손 empty)
    ?ladderFact <-(사다리조립 false)
    ?monkeyFact <-(원숭이위치 ?monkeyP)
    ?ladderAFact <-(사다리A위치 ?ladderAP); &:(eq ?ladderAP ?monkeyP))
    =>
    (getladder_a)
    (retract ?monkeyFact)
    (retract ?ladderAFact)
    (retract ?handFact)
    (assert (원숭이손 사다리A))
    (assert (원숭이위치 ?ladderAP))
    (assert (사다리A위치 원숭이위치))
)

; getting ladder B
(defrule GETLADDER_B
    ?handFact <-(원숭이손 사다리A)
    ?ladderFact <-(사다리조립 false)
    ?monkeyFact <-(원숭이위치 ?monkeyP)
    ?ladderBFact <-(사다리B위치 ?ladderBP)
    ?ladderAFact <-(사다리A위치 원숭이위치)
    =>
    (getladder_b)
    (retract ?monkeyFact)
    (retract ?handFact)
    (retract ?ladderBFact)
    (assert (원숭이위치 ?ladderBP))
    (assert (사다리B위치 원숭이위치))
    (assert (원숭이손 사다리두개))
)

; assemble of two ladder
(defrule ASSEMBLYLADDER 
    ?ladderFact <-(사다리조립 false)
    ?handFact <-(원숭이손 사다리두개)
    ?ladderAFact <-(사다리A위치 원숭이위치)
    ?ladderBFact <-(사다리B위치 원숭이위치)
    =>
    (assembly)
    (retract ?ladderFact)
    (retract ?handFact)
    (assert (사다리조립 true))
    (assert (원숭이손 조립된사다리))
)

; go to the bananas
(defrule GOTOBANANAS
    ?bananasFact <-(바나나위치 ?bananasP)
    ?monkeyFact <-(원숭이위치 ~?bananasP)
    ?handFact <-(원숭이손 조립된사다리)
    ?feetFact <-(원숭이발 onFloor)
    ?ladderFact <-(사다리조립 true)
    => 
    (goto-bananas)
    (retract ?monkeyFact)
    (retract ?handFact)
    (assert (원숭이위치 ?bananasP))
    (assert (원숭이손 empty))
)

; climb up ladder
(defrule CLIMBLADDER
    ?monkeyFact <-(원숭이위치 ?monkeyP)
    ?ladderFact <-(사다리조립 true)
    ?handFact <-(원숭이손 empty)
    ?feetFact <-(원숭이발 onFloor)
    ?bananasFact <-(바나나위치 ?bananasP&:(eq ?bananasP ?monkeyP))
    =>  
    (climb)
    (retract ?feetFact)
    (assert (원숭이발 onLadder))
)

; grap the bananas
(defrule GRAB
    ?monkeyFact <-(원숭이위치 ?monkeyP)
    ?bananasFact <-(바나나위치 ?bananasP&:(eq ?bananasP ?monkeyP))
    ?ladderFact <-(사다리조립 true)
    ?handFact <-(원숭이손 empty)
    ?feetFact <-(원숭이발 onLadder)
    =>
    (grab-banananas)
    (retract ?handFact)
    (assert (원숭이손 grab))
    (yammy)
    (halt)
)

; run this rules and facts 
(run)
