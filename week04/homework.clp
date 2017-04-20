; Water - Jug Problem

; 8,5,3 리터 세 항아리에 

; 8, 5, 3 리터 세 항아리에는 계량할 수 있는 눈금이 없다.
; 수도꼭지로부터 물을 받을 수 있으며 물을 밖으로 쏟아버리거나
; 세 항아리들이 한 쌍씩 서로서로 부을 수 있다.
; 8ℓ항아리에 정확히 4ℓ의 물을 채울 수 있나?  

; 시작상태 -  (0 0 0)
; 목표상태 -  (4 n n) 


;문제 해결 조건

;1. 항아리를 한번에 두개 이상의 항아리의 물을 부울 수 없다.

; 문제 해결 방법
; 5리터를 채우고 3리터로 쏟는다. 5리터에는 2리터가 남게된다.
; 3리터를 버린다.
; 5리터에 남은 2리터를 8리터로 붇는다.
; 5리터를 채우고 3리터를 끝까지 채운다. 5리터에는 다시 2리터가 남게 된다.
; 3리터를 버린다.
; 5리터에 남은 2리터를 8리터로 붓는다.
; (4 0 0) 이 된다.

(clear)
(watch all)

; printout
;항아리에 채우기
(deffunction FULL8LJUG()
    (printout t "8L 항아리를 가득 채워라" crlf)
)
(deffunction FULL5LJUG()
    (printout t "5L 항아리를 가득 채워라" crlf)
)
(deffunction FULL3LJUG()
    (printout t "3L 항아리를 가득 채워라" crlf)
)

;항아리에 물 버리기
(deffunction EMPTY8LJUG()
    (printout t "8L 항아리의 물을 모두 버려라" crlf)
)
(deffunction EMPTY5LJUG()
    (printout t "5L 항아리의 물을 모두 버려라" crlf)
)
(deffunction EMPTY3LJUG()
    (printout t "3L 항아리의 물을 모두 버려라" crlf)
)

;항아리에 있는 물 옮기기

; 8L 항아리에서 5L 또는 3L 항아리로 . 모두 붓는건 아님 
(deffunction POUR8Lto5L()
    (printout t "8L 항아리에 있는 물을 5L 항아리를 채워서 담아라." crlf)
)
(deffunction POUR8Lto3L()
    (printout t "8L 항아리에 있는 물을 3L 항아리를 채워서 담아라." crlf)
)
; 5L 항아리에서 8L 또는 3L 항아리로 
(deffunction POUR5Lto8L()
    (printout t "5L 항아리에 있는 물을 8L 항아리를 채워서 담아라." crlf)
)
(deffunction POUR5Lto3L()
    (printout t "5L 항아리에 있는 물을 3L 항아리를 채워서 담아라." crlf)
)
; 3L 항아리에서 8L 또는 5L 항아리로
(deffunction POUR3Lto8L()
    (printout t "3L 항아리에 있는 물을 8L 항아리를 채워서 담아라." crlf)
)
(deffunction POUR3Lto5L()
    (printout t "3L 항아리에 있는 물을 5L 항아리를 채워서 담아라." crlf)
)

; 항아리에 있는 물 있는것 전부 붓기


; 8L 항아리에서 5L 또는 3L 항아리로 . 모두 붓는건 아님 
(deffunction ALLPOUR8Lto5L()
    (printout t "8L 항아리에 있는 물을 5L 항아리에 쏟아라." crlf)
)
(deffunction ALLPOUR8Lto3L()
    (printout t "8L 항아리에 있는 물을 3L 항아리에 쏟아라." crlf)
)
; 5L 항아리에서 8L 또는 3L 항아리로 
(deffunction ALLPOUR5Lto8L()
    (printout t "5L 항아리에 있는 물을 8L 항아리에 쏟아라." crlf)
)
(deffunction ALLPOUR5Lto3L()
    (printout t "5L 항아리에 있는 물을 3L 항아리에 쏟아라." crlf)
)
; 3L 항아리에서 8L 또는 5L 항아리로
(deffunction ALLPOUR3Lto8L()
    (printout t "3L 항아리에 있는 물을 8L 항아리에 쏟아라." crlf)
)
(deffunction ALLPOUR3Lto5L()
    (printout t "3L 항아리에 있는 물을 5L 항아리에 쏟아라." crlf)
)


;; goal
(deffunction goal()
    (printout t crlf)
    (printout t "8L 항아리에 4L 의 물이 들어있습니다." crlf)
)

(printout t "========= deffunction test start =============" crlf)

(FULL8LJUG)
(FULL5LJUG)
(FULL3LJUG)

(EMPTY8LJUG)
(EMPTY5LJUG)
(EMPTY3LJUG)

(POUR8Lto5L)
(POUR8Lto3L)

(POUR5Lto8L)
(POUR5Lto3L)

(POUR3Lto8L)
(POUR3Lto5L)

(ALLPOUR8Lto5L)
(ALLPOUR8Lto3L)

(ALLPOUR5Lto8L)
(ALLPOUR5Lto3L)

(ALLPOUR3Lto8L)
(ALLPOUR3Lto5L)

(printout t "========= deffunction test end =============" crlf)
; MAIN module
; initial-state

(deftemplate MAIN::status
    (slot search-depth)
    (slot parent)
    (multislot jugs)
    (slot last-move)
)

(deffacts MAIN::initial-positions
    (status 
        (search-depth 1)
        (parent root)
        (jugs 0 0 0)
        (last-move no-move)
    )
)

(defrule MAIN::FULL8LJUG
    ?node <- (status
        (search-depth ?num)
        (jugs ?x&:(< ?x 8) ?y&:(neq ?y 5) ?z&:(neq ?z 3))
    )
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs 8 ?y ?z)
        (last-move FULL8LJUG)
    )
    (FULL8LJUG)
)

(defrule MAIN::FULL5LJUG
    ?node <- (status
        (search-depth ?num)
        (jugs ?x&:(neq ?x 8) ?y&:(< ?y 5) ?z&:(neq ?z 3))
    )
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs ?x 5 ?z)
        (last-move FULL5LJUG)
    )
    (FULL5LJUG)

)

(defrule MAIN::FULL3LJUG
    ?node <- (status
        (search-depth ?num)
        (jugs ?x&:(neq ?x 8) ?y&:(neq ?y 5) ?z&:(< ?z 3))
    )
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs ?x ?y 3)
        (last-move FULL3LJUG)
    )
    (FULL3LJUG)
)

(defrule MAIN::EMPTY8LJUG
    ?node <- (status
        (search-depth ?num)
        (jugs ?x&:(= ?x 8) ?y&:(> ?y 0) ?z&:(> ?z 0))
    )
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs 0 ?y ?z)
        (last-move EMPTY8LJUG)
    )
    (EMPTY8LJUG)
)

(defrule MAIN::EMPTY5LJUG
    ?node <- (status
        (search-depth ?num)
        (jugs ?x&:(> ?x 0) ?y&:(= ?y 5) ?z&:(> ?z 0))
    )
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs ?x 0 ?z)
        (last-move EMPTY5LJUG)
    )
    (EMPTY5LJUG)
)

(defrule MAIN::EMPTY3LJUG
    ?node <- (status
        (search-depth ?num)
        (jugs ?x&:(> ?x 0) ?y&:(> ?y 0) ?z&:(= ?z 3))
    )
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs ?x ?y 0)
        (last-move EMPTY3LJUG)
    )
    (EMPTY3LJUG)
)
 
(defrule MAIN::POUR8Lto5L
    ?node <- (status
        (search-depth ?num)
        (jugs ?x&:(and (> ?x 0) (>= (+ ?x ?y) 5)) ?y&:(and (> ?x 0) (< ?y 5)) ?z&:(< ?z 3))
    )
    =>
    (bind ?x (- ?x (- 5 ?y)))
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs ?x 5 ?z)
        (last-move POUR8Lto5L)
    )
    (POUR8Lto5L)
)

(defrule MAIN::POUR8Lto3L
    ?node <- (status
        (search-depth ?num)
        (jugs ?x&:(and (> ?x 0) (>= (+ ?x ?z) 3)) ?y&:(< ?y 5) ?z&:(and (> ?z 0) (< ?z 3)))
    )
    =>
    (bind ?x (- ?x (- 3 ?z)))
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs ?x ?y 3)
        (last-move POUR8Lto3L)
    )
    (POUR8Lto3L)
)
 
(defrule MAIN::POUR5Lto8L
    ?node <- (status
        (search-depth ?num)
        (jugs ?x&:(and (> ?x 0)(< ?x 8)) ?y&:(and (> ?y 0) (>= (+ ?y ?x) 8)) ?z&:(< ?z 3))
    )
    =>
    (bind ?y (- ?y (- 8 ?x)))
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs 8 ?y ?z)
        (last-move POUR5Lto8L)
    )
    (POUR5Lto8L)
)

(defrule MAIN::POUR5Lto3L
    ?node <- (status
        (search-depth ?num)
        (jugs ?x&:(< ?x 8) ?y&:(and (> ?y 0) (>= (+ ?y ?z) 3)) ?z&:(and (> ?z 0) (< ?z 3)))
    )
    =>
    (bind ?y (- ?y (- 3 ?z)))
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs ?x ?y 3)
        (last-move POUR5Lto3L)
    )
    (POUR5Lto3L)
)
 
(defrule MAIN::POUR3Lto8L 
    ?node <- (status
        (search-depth ?num)
        (jugs ?x&:(and (> ?x 0)(< ?x 8)) ?y&:(< ?y 5) ?z&:(and (> ?z 0) (>= (+ ?z ?x) 8)))
    )
    =>
    (bind ?z (- ?z (- 8 ?x)))
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs 8 ?y ?z)
        (last-move POUR3Lto8L)
    )
    (POUR3Lto8L)
)


(defrule MAIN::POUR3Lto5L
    ?node <- (status
        (search-depth ?num)
        (jugs ?x&:(< ?x 8) ?y&:(and (> ?y 0)(< ?y 5)) ?z&:(and (> ?z 0) (>= (+ ?z ?y) 5)))
    )
    =>
    (bind ?z (- ?z (- 5 ?y)))
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs ?x 5 ?z)
        (last-move POUR3Lto5L)
    )
    (POUR3Lto5L)
)
;; --------------------------------------------------------------------------------------------------

(defrule MAIN::ALLPOUR8Lto5L
    ?node <- (status
        (search-depth ?num)
        (jugs ?x&:(and (> ?x 0) (< (+ ?y ?x) 5)) ?y ?z)
    )
    =>
    (bind ?y (+ ?x ?y))
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs 0 ?y ?z)
        (last-move ALLPOUR8Lto5L)
    )
    (ALLPOUR8Lto5L)
)
(defrule MAIN::ALLPOUR8Lto3L
    ?node <- (status
        (search-depth ?num)
        (jugs ?x&:(and (> ?x 0) (< (+ ?z ?x) 3)) ?y ?z)
    )
    =>
    (bind ?z (+ ?x ?z))
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs 0 ?y ?z)
        (last-move ALLPOUR8Lto3L)
    )
    (ALLPOUR8Lto3L)
)

(defrule MAIN::ALLPOUR5Lto8L
    ?node <- (status
        (search-depth ?num)
        (jugs ?x ?y&:(and (> ?y 0) (< (+ ?x ?y) 8)) ?z)
    )
    =>
    (bind ?x (+ ?x ?y))
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs ?x 0 ?z)
        (last-move ALLPOUR5Lto8L)
    )
    (ALLPOUR5Lto8L)

)
(defrule MAIN::ALLPOUR5Lto3L
    ?node <- (status
        (search-depth ?num)
        (jugs ?x ?y&:(and (> ?y 0) (< (+ ?z ?y) 3)) ?z)
    )
    =>
    (bind ?z (+ ?z ?y))
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs ?x 0 ?z)
        (last-move ALLPOUR5Lto3L)
    )
    (ALLPOUR5Lto3L)
)

(defrule MAIN::ALLPOUR3Lto8L
    ?node <- (status
        (search-depth ?num)
        (jugs ?x ?y ?z&:(and (> ?z 0) (< (+ ?x ?z) 8)))
    )
    =>
    (bind ?x (+ ?x ?z))
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs ?x ?y 0)
        (last-move ALLPOUR3Lto8L)
    )
    (ALLPOUR3Lto8L)
)
(defrule MAIN::ALLPOUR3Lto5L
    ?node <- (status
        (search-depth ?num)
        (jugs ?x ?y ?z&:(and (> ?z 0) (< (+ ?z ?y) 5)))
    )
    =>
    (bind ?y (+ ?y ?z))
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs ?x ?y 0)
        (last-move ALLPOUR3Lto5L)
    )
    (ALLPOUR3Lto5L)
)





;; -------------------------------------------------------------------------------------------------- 

(defmodule CONSTRAINT)

(defrule CONSTRAINT::circular-path
    (declare (auto-focus TRUE))
    (status 
        (search-depth ?sd1)
        (jugs ?x ?y ?z)
    )
    ?node <- (status 
        (search-depth ?sd2&:(< ?sd1 ?sd2))
        (jugs ?x ?y ?z)
    )
    =>
    (retract ?node)
)

(defmodule SOLUTION)

(deftemplate SOLUTION::moves
    (slot id)
    (multislot moves-list)
)

(defrule SOLUTION::recognize-solution
    (declare (auto-focus TRUE))
    ?node <- (status 
        (parent ?parent)
        (jugs 4 ?y ?z)
        (last-move ?move)
    )
    =>
    (retract ?node)
    (assert (moves (id ?parent)(moves-list ?move)))
)


(defrule SOLUTION::further-solution
    ?node <- (status 
        (parent ?parent)
        (last-move ?move)
    )
    ?mv <- (moves (id ?node) (moves-list rest$))
    =>
    (modify ?mv (id ?parent) (moves-list ?move ?rest) )
)


(defrule SOLUTION::print-solution
    ?mv <- (moves 
        (id root)
        (moves-list no-move $?m)
    )
    =>
    (retract ?mv)
    
    (goal)
    (printout t crlf "4L 를 만들었습니다." crlf)
    
    (bind ?length (length$ ?m))
    (bind ?i 1)
    
    (while (<= ?i ?length)
        (bind ?thing (nth$ ?i ?m))
        (printout t "thing : [" ?thing "]."  crlf)
        (bind ?i (+ 1 ?i))
    )
)
 
(reset)
(run)
