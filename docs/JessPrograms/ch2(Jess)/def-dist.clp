(deffunction distance2D (?x1 ?y1 ?x2 ?y2) ; ��
           "2�������� �� ���� �Ÿ��� ���ϴ� �Լ� ����"  
           (bind ?dx (- ?x1 ?x2) )
           (bind ?dy (- ?y1 ?y2) )
           (bind ?dist (sqrt (+ (* ?dx ?dx) (* ?dy ?dy) ) ) ) ; sqrt-������
           (return ?dist)  )
(distance2D 1 2 3 4) ; ��
