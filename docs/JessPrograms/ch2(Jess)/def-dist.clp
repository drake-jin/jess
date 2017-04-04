(deffunction distance2D (?x1 ?y1 ?x2 ?y2) ; ①
           "2차원에서 두 점의 거리를 구하는 함수 정의"  
           (bind ?dx (- ?x1 ?x2) )
           (bind ?dy (- ?y1 ?y2) )
           (bind ?dist (sqrt (+ (* ?dx ?dx) (* ?dy ?dy) ) ) ) ; sqrt-제곱근
           (return ?dist)  )
(distance2D 1 2 3 4) ; ②
