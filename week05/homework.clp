
; Author : drake-jin
; date 2017-04-20
; title :식인종과 선교사 문제
; desc : 선교사를 오른쪽 강으로 식인종에게 먹히지 않고 옮기는 방법 

; 식인종이 4명, 선교사가 4명, 
; 보트의 정원 3 명 
; 강왼쪽 1 
; 강왼쪽 2 
;  
; M3 = 4 - M1 으로 계산할 수 있다. 이는 강 왼쪽의 선교사나 식인종의 수만 알고 있으면 
; 오른쪽의 선교사나 식인종의 수는 쉽게 알 수 있다는 말이 된다.
; 
; 배가 왼쪽에 있으면 오른쪽에는 없게 되므로 왼쪽편의 정보로 전체의 상황을 쉽게 판단할 수 있다.
; 
; (M, C, 2) 일 때 
; M 은 왼쪽편에 있는 선교사의 수
; C 는 왼쪽편의 식인종의 수 
; B 는 보트의 위치(1은 왼쪽, 2는 오른쪽)
; 
; 



(clear)
(watch all)

; fact 를 저장한다. 
(deftemplate MAIN::status
    (slot search-depth)
    (slot parent)
    (multislot mcb)
    (slot last-move)
)

(deffacts MAIN::initial-positions
    (status 
        (search-depth 1)
        (parent root)
        (mcb 4 4 1)
        (last-move no-move)
    )
)

; safe condition
; 1. 안전조건
; if (M=0 or M>=C) and (4-M=0 or 4-M >= 4-c)
(deffunction safe-cond (?m ?c)
    (if (and (or (= ?m 0) (>= ?m ?c)) (or (= (- 4 ?m) 0) (>= (- 4 ?m) (- 4 ?c)))) then 
        (return TRUE)
    )
)

; 
; 2. 선교사 1명이 오른쪽으로 건너가는 경우
; if B=1, M>=1, safe_cond(M-1, C)=true 
; then (M-1,C,2)
(defrule MAIN::move-M_1to2 
    ?node <- (status (search-depth ?num)(mcb ?m ?c 1))
    (test (and (safe-cond (- ?m 1) ?c) (> ?m 0)))
    =>
    (duplicate ?node 
        (search-depth (+ 1 ?num))
        (parent ?node)
        (mcb (- ?m 1) ?c 2)
        (last-move M_1to2)
    )
)

; 3. 선교사 1명이 왼쪽으로 건너가는 경우
; if B=2, 4-M>=1, safe_cond(M+1, C)=true
; then (M+1,C,1)
(defrule MAIN::move-M_2to1
    ?node <- (status (search-depth ?num) (mcb ?m ?c 2))
    (test (and (safe-cond (+ ?m 1) ?c) (> (- 4 ?m) 0)))
    =>  
    (duplicate ?node 
        (search-depth (+ 1 ?num))
        (parent ?node)
        (mcb (+ ?m 1) ?c 1)
        (last-move M_2to1)
    )
)

; 4. 식인종 1명이 오른쪽으로 건너가는 경우
; if B=1, C>=1, safe_cond(M, C-1)=true
; then (M, C-1, 2)
(defrule MAIN::move-C_1to2
    ?node <- (status (search-depth ?num) (mcb ?m ?c 1)) 
    (test (and (safe-cond ?m (- ?c 1))(> ?c 0)))
    =>
    (duplicate ?node 
        (search-depth (+ 1 ?num))
        (parent ?node)
        (mcb ?m (- ?c 1) 2)
        (last-move C_1to2)
    )
)

; 5. 식인종 1명이 왼쪽으로 건너가는 경우
; if B=2, 4-C>=1, safe_cond(M, C+1)=true
; then (M,C+1,1)
(defrule MAIN::move-C_2to1
    ?node <- (status (search-depth ?num) (mcb ?m ?c 2)) 
    (test (and (safe-cond ?m (+ ?c 1))(> (- 4 ?c) 0)))
    =>
    (duplicate ?node 
        (search-depth (+ 1 ?num))
        (parent ?node)
        (mcb ?m (+ ?c 1) 1)
        (last-move C_1to2)
    )
)

; 6. 선교사1명과 식인종1명이 오른쪽으로 건너가는 경우
; if(B=2, M>=1, C>=1, M=C
; then (M-1, C-1, 2)
(defrule MAIN::move-MC_1to2
    ?node <- (status (search-depth ?num) (mcb ?m ?m 1))
    (test (> ?m 0))
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (mcb (- ?m 1) (- ?m 1) 2)
        (last-move MC_1to2)
    )
)

; 7. 선교사1명과 식인종1명이 오른쪽에서 왼쪽으로 건너가는 경우
; if (B=1, 4-M>=1, 4-C>=1, (4-M)=(4-C) )
; then (M+1, C+1, 1)
(defrule MAIN::move-MC2to1
    ?node <- (status (search-depth ?num) (mcb ?m ?m 2))
    (test (>  (- 4 ?m) 0))
    =>
    (duplicate ?node 
        (search-depth (+ 1 ?num))
        (parent ?node) 
        (mcb (+ ?m 1) (+ ?m 1) 1) 
        (last-move MC_2to1) 
    )
)

; 8. 선교사 2명이 오른쪽으로 가는경우
; if(B=1, M>=2, safe_cond(M-2, C)=true)
; then (M-2, C, 2)
(defrule MAIN::move-MM_1to2
    ?node <- (status (search-depth ?num)(mcb ?m ?c 1))
    (test (and (safe-cond (- ?m 2) ?c) (> ?m 1)))
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (mcb (- ?m 2) ?c 2)
        (last-move MM_1to2) 
    )
)

; 9. 선교사 2명이 왼쪽으로 가는 경우
; if (B=2, 4-M>=2, safe_cond(M+2, C)=true
; then (M+2, C, 1)
(defrule MAIN::move-MM_2to1
    ?node <- (status (search-depth ?num)(mcb ?m ?c 2))
    (test (and (safe-cond (+ ?m 2) ?c) (> (- 4 ?m) 1)))
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (mcb (+ ?m 2) ?c 1)
        (last-move MM_2to1) 
    )
)

; 10. 식인종 2명이 오른쪽으로 가는 경우
; if (B=1, C>=2, safe_cond(M,C-2)=true
; then (M, C-2, 2)
(defrule MAIN::move-CC_1to2
    ?node <- (status (search-depth ?num) (mcb ?m ?c 1))
    (test (and (safe-cond ?m (- ?c 2 )) (> ?c 1)))
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (mcb ?m (- ?c 2) 2)
        (last-move CC_1to2)
    )
)

; 11. 식인종 2명이 왼쪽으로 가는 경우
; if (B=2, 4-C>=2, safe_cond(M, C+2)=true
; then (M, C+2, 1)
(defrule MAIN::move-CC_2to1
    ?node <- (status (search-depth ?num) (mcb ?m ?c 2))
    (test (and (safe-cond ?m (+ ?c 2 )) (> (- 4 ?c) 1)))
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (mcb ?m (+ ?c 2) 1)
        (last-move CC_2to1)
    )
)

; 12. 선교사 2명과 식인종 1명이 오른쪽으로 가는경우
; if (B=1, M>=2, C>=1, safe_cond(M-2,C-1)=true
; then (M-2, C-1, 2)
(defrule MAIN::move-MMC_1to2
    ?node <- (status (search-depth ?num) (mcb ?m ?c 1))
    (test (and (safe-cond (- ?m 2) (- ?c 1)) (> ?m 1) (> ?c 0)))
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (mcb (- ?m 2) (- ?c 1) 2)
        (last-move MMC_1to2)
    )
)
; 13. 선교사 2명과 식인종 1명이 왼쪽으로 가는 경우
; if (B=2, 4-M>=2, 4-C>=1, safe_cond(M+2,C+1)=true
; then (M+2, C+1, 1)
(defrule MAIN::move-MMC_2to1
    ?node <- (status (search-depth ?num) (mcb ?m ?c 2))
    (test (and (safe-cond (+ ?m 2) (+ ?c 1)) (> (- 4 ?m) 1) (> (- 4 ?c) 0)))
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (mcb (+ ?m 2) (+ ?c 1) 1)
        (last-move MMC_2to1)
    )
)

; 14. 선교사 1명과 식인종 2명이 오른쪽으로 가는 경우
; if (B=1, M>=1, C>=2, safe_cond(M-1,C-2)=true
; then (M-1, C-2, 2)
(defrule MAIN::move-MCC_1to2
    ?node <- (status (search-depth ?num) (mcb ?m ?c 1))
    (test (and (safe-cond (- ?m 1) (- ?c 2)) (> ?m 0) (> ?c 1)))
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (mcb (- ?m 1) (- ?c 2) 2)
        (last-move MCC_1to2)
    )
)

; 15. 선교사 1명과 식인종 2명이 왼쪽으로 가는 경우
; if (B=2, 4-M>=1, 4-C>=2, safe_cond(M+1,C+2)=true  
; then (M+1, C+2, 1)
(defrule MAIN::move-MCC_2to1
    ?node <- (status (search-depth ?num) (mcb ?m ?c 2))
    (test (and (safe-cond (+ ?m 1) (+ ?c 2)) (> (- 4 ?m) 0) (> (- 4 ?c) 1)))
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (mcb (+ ?m 1) (+ ?c 2) 1)
        (last-move MCC_2to1)
    )
)

; 16. 선교사 3명이 오른쪽으로 가는 경우
; if (B=1, M>=3, safe_cond(M-3,C)=true
; then (M-3, C, 2)
(defrule MAIN::move-MMM_1to2
    ?node <-(status (search-depth ?num) (mcb ?m ?c 1)) 
    (test (and (safe-cond (- ?m 3) ?c ) (> ?m 2)))
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (mcb (- ?m 3) ?c 2)
        (last-move MMM_1to2)
    )
)


; 17. 선교사 3명이 왼쪽으로 가는 경우
; if (B=2, 4-M>=3, safe_cond(M+3, C) true
; then (M+3, C, 1)
(defrule MAIN::move-MMM_2to1
    ?node <-(status (search-depth ?num) (mcb ?m ?c 2)) 
    (test (and (safe-cond (+ ?m 3) ?c ) (> (- 4 ?m) 2)))
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (mcb (+ ?m 3) ?c 1)
        (last-move MMM_2to1)
    )
)

; 18. 식인종 3명이 오른쪽으로 가는 경우 
; if (B=1, C>=3, safe_cond(M,C-3)=true
; then (M, C+3, 2)
(defrule MAIN::move-CCC_1to2
    ?node <-(status (search-depth ?num) (mcb ?m ?c 1)) 
    (test (and (safe-cond ?m (- ?c 3)) (> ?c 2)))
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (mcb ?m (- ?c 3) 2)
        (last-move CCC_1to2)
    )
)

; 19. 식인종 3명이 왼쪽으로 가는 경우
; if (B=2, 4-C>=3, safe_cond(M,C+3)=true
; then (M, C-3, 1)
(defrule MAIN::move-CCC_2to1
    ?node <-(status (search-depth ?num) (mcb ?m ?c 2)) 
    (test (and (safe-cond ?m (+ ?c 3) ) (> (- 4 ?c) 2)))
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (mcb ?m (+ ?c 3) 1)
        (last-move CCC_2to1)
    )
)













(defmodule CONSTRAINTS)

(defrule CONSTRAINTS::circular-path
    (declare (auto-focus TRUE))
    (status (search-depth ?sd1) (mcb ?m ?c ?b))
    ?node <- (status (search-depth ?sd2&:(< ?sd1 ?sd2)) (mcb ?m ?c ?b))
    =>
    (retract ?node)
)

(defmodule SOLUTION)

(deftemplate SOLUTION::moves
    (slot id)
    (multislot moves-list)
)

; 20 종료 
; if (M=0, C=0, B=2)
; then (halt)
(defrule SOLUTION::recognize-solution
    (declare (auto-focus TRUE))
    ?node <- (status (parent ?parent) (mcb 0 0 2) (last-move ?move))
    =>
    (retract ?node)
    (assert (moves (id ?parent) (moves-list ?move)))
)   

(defrule SOLUTION::further-solution
    ?node <-(status (parent ?parent) (last-move ?move))
    ?mv <- (moves (id ?node) (moves-list $?rest)) 
    =>
    (modify ?mv 
        (id ?parent)
        (moves-list ?move ?rest)
    )
)   

(defrule SOLUTION::print-solution
    ?mv <- (moves (id root) (moves-list no-move $?m))
    =>
    (retract ?mv)
    (printout t "무사히 건너 갔어요!!" crlf)
    (bind ?length (length$ ?m))
    (bind ?i 1)
    (bind ?boat 2)
    (while (<= ?i ?length)
        (bind ?thing (nth$ ?i ?m))
        (printout t ?thing " | " ?boat "로 건넌다." crlf )
        (if (eq ?boat 1) then
            (bind ?boat 2)
        else
            (bind ?boat 1)
        )
        (bind ?i (+ 1 ?i))
    )
    (store cannibalState ?m)
)







(reset)
(run)


