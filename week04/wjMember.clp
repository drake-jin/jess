;; 4리터와 3리터 항아리 , 두 항아리를 이용하여 
; 4 리터 항아리에 2L 항아리의 물을 받을 수 있도록 하여라

; 이 모델은 무한루프가 나오는 약간 좋지 않은 모델..
; 이 보다 괜찮은 모델은 트리구조를 활용하기 위해.. 



;initialize this source code
(clear)
(reset)
(watch all)


;; print out
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

; local method 
(deffunction wj-Str (?4LJug ?3LJug)
    (bind ?wj-Str (str-cat ?4LJug ?3LJug))
    (return ?wj-Str)
;    ?wj-Str
)
(deffunction add-wj-list(?*wj-visited* ?wj-Str1)
    (bind ?wj-visited2 (create$ ?*wj-visited* ?wj-Str1))
    (return ?wj-visited2)
)

(assert (4LJUG 0))
(assert (3LJUG 0))

; global variable 
(bind ?*wj-visited* (create$ (wj-Str 0 0 )))

; test print 
(printout t "===================== method test start =======================" crlf)
(FULL3LJUG)
(FULL4LJUG)
(EMPTY3LJUG)
(EMPTY4LJUG)
(POUR34FULL)
(POUR43FULL)
(POUR34)
(POUR43)
(goal)
(printout t "===================== method test end =======================" crlf)



;define rules 

; 4리터 항아리를 전부 부어라
(defrule FULL4LJUG 
    ?FULL4L <- (4LJUG ?x &: (< ?x 4))
    ?FULL3L <- (3LJUG ?y)
    (test (not (member$ (wj-Str 4 ?y) ?*wj-visited* )))
    =>
; 조건 :  4LJUG가 만약 4 미만이고, wj-visited (4 n)이 아니라면 밑의 규칙을 돌린다.
;관전 포인트 : member$ 를 활용한 무한방복 방지 프로그램
; ?*wj-visited* 이 변수에다가는 채워진 과거의 기록을 저장한다.
; 그 저장된 기록중에 반복을 하지 않기위해서 이전에 했던 행위들을 저장하여 검토한다.
;검토하면서 이전에 했던 행동이라면 규칙이 돌지 않도록 한다. 
    (FULL4LJUG)
    (retract ?FULL4L)
    (assert (4LJUG 4))
    (bind ?*wj-visited* (add-wj-list ?*wj-visited* (wj-Str 4 ?y) ))
)

; 3리터 항아리를 전부 부어라
(defrule FULL3LJUG 
    ?FULL4L <- (4LJUG ?x)
    ?FULL3L <- (3LJUG ?y &: (< ?y 3))
    (test (not (member$ (wj-Str ?x 3) ?*wj-visited*)))
    =>
; 조건 : 3LJUG가 만약 3미안이고, wj-visited (n 3) 이 존재하지 않다면 밑의 규칙을 돌린다
; FULL4L 에는 fact의 번호가 저장되어있고
; 4LJUG에는 값이 들어가있다.
    (FULL3LJUG)
    (retract ?FULL3L)
    (assert (3LJUG 3))
    (bind ?*wj-visited* (add-wj-list ?*wj-visited* (wj-Str ?x 3)))
)


;4리터 항아리를 전부 비워라
(defrule EMPTY4LJUG
    ?EMPTY4L <- (4LJUG ?x &: (> ?x 0))
    ?EMPTY3L <- (3LJUG ?y)
    (test (not (member$ (wj-Str 0 ?y) ?*wj-visited*)))
    =>
;조건 : 3LJUG에 관계없이 4LJUG에 물이 들어있고, ?*wj-visited* 에 다음과 같은 적이 없었다면
; 
    (EMPTY4LJUG)
    (retract ?EMPTY4L)
    (assert (4LJUG 0))
    (bind ?*wj-visited* (add-wj-list ?*wj-visited* (wj-Str 0 ?y)))
)

;3리터 항아리를 그냥 비워라
(defrule EMPTY3LJUG
    ?EMPTY4L <- (4LJUG ?x)
    ?EMPTY3L <- (3LJUG ?y &: (> ?y 0))
    (test (not (member$ (wj-Str ?x 0) ?*wj-visited*)))
    =>
;조건: 4LJUG 에 관계없이 3LJUG에 물이 들어있고, ?*wj-visited* 에 다음과 같은 적이 없었다면 아래의 행동을 수행한다.
    (EMPTY3LJUG)
    (retract ?EMPTY3L)
    (assert (3LJUG 0))
    (bind ?*wj-visited* (add-wj-list ?*wj-visited* (wj-Str ?x 0)))
)


; 3리터 항아리에서 4리터 항아리 아구까지 전부 부워라
(defrule POUR34FULL
    ?POUR34F1 <- (4LJUG ?x &: (< ?x 4))
    ?POUR34F2 <- (3LJUG ?y &: (and (> ?y 0) (>= (+ ?y ?x) 4)))
    (test (not (member$ (wj-Str 4 (- ?y (- 4 ?x))) ?*wj-visited*)))
    =>
;4리터 항아리가 끝까지 안채워졌고, 3리터항아리의 물과 4리터 항아리의 물이 4가 넘을 때 아래의 행동들을 수행한다.
;목적 : 


    (POUR34FULL)
    (bind ?y (- ?y (- 4 ?x)))
    (retract ?POUR34F1)
    (retract ?POUR34F2)
    (assert (4LJUG 4))
    (assert (3LJUG ?y))
    (bind ?*wj-visited* (add-wj-list ?*wj-visited* (wj-Str 4 ?y)))
)

; 4리터 항아리에서 3리터 항아리로 옮겨 담아라. 
; 이 때 4리터 항아리는 전부를 비우지 않고 3리터가 다 채워진 나머지를 채우게 된다.
(defrule POUR43FULL 
    ?POUR43F1 <- (3LJUG ?y &: (< ?y 3))
    ?POUR43F2 <- (4LJUG ?x &: (and (> ?x 0) (>= (+ ?y ?x) 3)))
    (test (not (member$ (wj-Str (- ?x (- 3 ?y)) 3) ?*wj-visited*)))
    =>
    (POUR43FULL)
    (bind ?x (- ?x (- 3 ?y)))
    (retract ?POUR43F1)
    (retract ?POUR43F2)
    (assert (4LJUG ?x))
    (assert (3LJUG 3))
    (bind ?*wj-visited* (add-wj-list ?*wj-visited* (wj-Str ?x 3)))
)

(defrule POUR34 
    ?POUR341 <- (4LJUG ?x)
    ?POUR342 <- (3LJUG ?y &: (and (> ?y 0) (< (+ ?y ?x) 4)))
    (test (not (member$ (wj-Str (+ ?x ?y) 0) ?*wj-visited*)))
    => 
    (POUR34)
    (bind ?x (+ ?x ?y))
    (retract ?POUR341)
    (retract ?POUR342)
    (assert (4LJUG ?x))
    (assert (3LJUG 0))
    (bind ?*wj-visited* (add-wj-list ?*wj-visited* (wj-Str ?x 0)))
)

(defrule POUR43 
;    ?POUR431 <- (4LJUG ?x &: (and (> ?x 0) (< (+ ?y ?x) 3)))
;    ?POUR432 <- (3LJUG ?y)
;  조건절에 순서도 연관이 있으니 주의할것. 아래193번줄과 194번줄의 순서를 바꾸면 코드가 정상적으로 동작하지 않음.
; 이유는 잘 모르겠...다..
    ?POUR432 <- (3LJUG ?y)
    ?POUR431 <- (4LJUG ?x &: (and (> ?x 0) (< (+ ?y ?x) 3)))
    (test (not (member$ (wj-Str 0 (+ ?x ?y)) ?*wj-visited*)))
    => 
    (POUR43)
    (bind ?y (+ ?x ?y))
    (retract ?POUR431)
    (retract ?POUR432)
    (assert (4LJUG 0))
    (assert (3LJUG ?y))
    (bind ?*wj-visited* (add-wj-list ?*wj-visited* (wj-Str 0 ?y)))
)

(defrule goal 
    (declare (salience 100))
    (4LJUG ?x &: (= ?x 2))
    =>
    (goal)
    (halt)
)

(run)















