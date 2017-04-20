;; 자동으로 출력되는 MAIN::status 를 줭해보자
; version 1 에서는
; # 무한반복 방지를 위해 member$을 이용한 프로그램
; 을 만들었었다. 실제로 version 1에 member$를 이용하지 않았다면 새로운 (x, y) 형식의 사실로 바꾸는 일을 반복하여
; 목표상태인 (2,n)을 찾게 되게 되기 때문입니다. 
 ; 
; 만약 여기에서 이전의 실행 결과를 가지고 있지 않은 상태에서 계속 탐색을 수행한다면 다른 조건과 겹치게 되어 무한 반복 할 수 있게 됩니다.


;    다음 Listing 2.19 
; 1) 상태공간 : 항아리들의 모든 상태를 어떻게 표현하는가?
;   컴퓨터가 알아들을 수 있는 기호체게를 설정하여 상태공간을 만들자.
; 2) 초기상태와 목표상태 : 위에서 정리한 기호체계를 이용하여 
;   항아리들이 처음 시작할 때의 초기상태와 우리가 목표로 하는 목표상태를 명시해주어야 한다.
;   초기상태는 (0, 0) | 목표상태는 (2, n) 이다. 
; 3) 규칙 설정. 


; Tree 구조로 만들어보자.
; version1 에서는 이전 수행 결과를 가지고 있는 모델이었지만 .. 
; 이번 모델은 부모 노드와 자식노드를 연결하는 트리구조를 적용하여 새로 생성된 
; 현재노드가 자신의 상위노드에 이미 존재하면 삭제하는 방법을 이용한다.

; 이를 구현하기 위해 모듈을 사용하게 되는데 
; 모듈 Module defmodule 은 한 규칙이 실행되면 곧 이어 반복방지를 위한 규칙을 실행해야 하기 때문이다.
; 만약 모듈이 없다면 한 상태에 대해 프로그램 내의 모든 규칙 중 하나의 규 칙만 실행되므로 반복방지를 위한 규칙을 실행할 기회가 없어지게 된다.
;   
; "status" Fact Format
; slot | search-depth : 트리의 깊이
; slot | parent : 부모 노드를 저장한다.
; multislot | jugs 의 4L 3L 를 저장할 곳
; slot | last-move 자신이 만들어진 실행 과정
; 
; 초기(루트) 노드 초기화
; slot | search-depth : 1 
; slot | parent : root 
; multislot | (jugs 0 0)
; last-move | no-move 

; jugs 0 0 인 상태에서는 MAIN::FULL4LJUG 또는 MAIN::FULL3LJUG 규칙이 활성화 될것이고 
; 이 중 MAIN::FULL4LJUG 규칙이 fire되어 f-2가 추가 될 것이다. 
; 91p에서 계속 .... 
; --------------------------------------------------------------------------------------------------

; source code initialize
(clear)
(watch all)


(deftemplate MAIN::status 
    (slot search-depth)
    (slot parent)
    (multislot jugs)
    (slot last-move)
)
; printout 
(deffunction FULL4LJUG()
    (printout t "4L 항아리를 가득 채워라" crlf)
)
(deffunction FULL3LJUG()
    (printout t "3L 항아리를 가득 채워라" crlf)
)
(deffunction EMPTY4LJUG()
    (printout t "4L 항아리를 모두 비워라" crlf)
)
(deffunction EMPTY3LJUG()
    (printout t "3L 항아리를 모두 비워라" crlf)
)
(deffunction POUR34FULL()
    (printout t "3L 항아리에서 4L 항아리의 아구까지 부어라" crlf)
)
(deffunction POUR43FULL()
    (printout t "4L 항아리에서 3L 항아리의 아구까지 부어라" crlf)
)
(deffunction POUR34()
    (printout t "3L 항아리에서 4L 항아리로 모두 부어라" crlf)
)
(deffunction POUR43()
    (printout t "4L 항아리에서 3L 항아리로 모두 부어라" crlf)
)
(deffunction goal()
    (printout t "4L 항아리에서 2L 채워졌습니다.")
    (printout t ?*wj-visited* crlf)
)



;; initial state

(deffacts MAIN::initial-positions
    (status 
        (search-depth 1)
        (parent root)
        (jugs 0 0)
        (last-move no-move)
    )
)

;; generate path rules
(defrule MAIN::FULL4LJUG
    ?node <- (status 
        (search-depth ?num )
        (jugs ?x&:(< ?x 4) ?y)
    )
    => 
    (duplicate ?node 
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs 4 ?y)
        (last-move FULL4LJUG)
    )
    (FULL4LJUG)
)

(defrule MAIN::FULL3LJUG
    ?node <- (status 
        (search-depth ?num) 
        (jugs ?x ?y&:(< ?y 3))
    )
    =>
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs ?x 3)
        (last-move FULL3LJUG)
    )   
    (FULL3LJUG) 
)

(defrule MAIN::EMPTY4LJUG
    ?node <- (status
        (search-depth ?num)
        (jugs ?x&:(> ?x 0) ?y)
    )
    => 
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs 0 ?y)
        (last-move EMPTY4LJUG)
    )
    (EMPTY4LJUG)
)

(defrule MAIN::EMPTY3LJUG
    ?node <- (status
        (search-depth ?num)
        (jugs ?x ?y&:(> ?y 0))
    )
    => 
    (duplicate ?node 
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs ?x 0)
        (last-move EMPTY4LJUG)
    )
    (EMPTY3LJUG)
)

(defrule MAIN::POUR34FULL
    ?node <- (status
        (search-depth ?num)
        (jugs ?x&:(< ?x 4) ?y&:(and (> ?y 0) (>= (+ ?y ?x) 4))) 
    )
    =>
    (bind ?y (- ?y (- 4 ?x)))
    (duplicate ?node 
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs 4 ?y)
        (last-move POUR34FULL)
    )
    (POUR34FULL)
)

(defrule MAIN::POUR43FULL
    ?node <- (status 
        (search-depth ?num)
        (jugs ?x&:(and (> ?x 0) (>= (+ ?y ?x) 3)) ?y&:(< ?y 3)) 
    )
    =>
    (bind ?x (- ?x (- 3 ?y)))
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs ?x 3)
        (last-move POUR43FULL)
    )
    (POUR43FULL)
)

(defrule MAIN::POUR34
    ?node <- (status
        (search-depth ?num)
        (jugs ?x ?y&:(and (> ?y 0) (< (+ ?y ?x) 4)))
    )
    =>
    (bind ?x (+ ?x ?y))
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs ?x 0)
        (last-move POUR34)
    )
)

(defrule MAIN::POUR43
    ?node <- (status 
        (search-depth ?num)
        (jugs ?x&:(and (> ?x 0) (< (+ ?y ?x) 3)) ?y)   
    )
    =>
    (bind ?y (+ ?x ?y))
    (duplicate ?node
        (search-depth (+ 1 ?num))
        (parent ?node)
        (jugs 0 ?y)
        (last-move POUR43)
    )
)

;;; define CONSTRAINT VIOLATION RULES 

(defmodule CONSTRAINT)


(defrule CONSTRAINT::circular-path
    (declare (auto-focus TRUE)) ; 모든 모듈을 돌려라.
    (status 
        (search-depth ?sd1)
        (jugs ?x ?y)
    )
    ?node <- (status 
        (search-depth ?sd2&:(< ?sd1 ?sd2))
        (jugs ?x ?y)
    )
    =>
    (retract ?node)
)

;; find and print solution rules


(defmodule SOLUTION)

(deftemplate SOLUTION::moves
    (slot id)
    (multislot moves-list)
)

(defrule SOLUTION::recognize-solution
    (declare (auto-focus TRUE))
    ?node <- (status 
        (parent ?parent)
        (jugs 2 ?y)
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
    ?mv <- (moves (id ?node)(moves-list $?rest))
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
    (printout t crlf "2L 를 만들었습니다." crlf)
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





















