(clear)
(reset)
(watch all) ; ��
(deffunction grab-bananas ()
   (printout t "�����̰� �ٳ����� ��´�" crlf) )  ; ��
(assert (�����̼� empty)) ; ��
(defrule GRAB ; ��
           ?handFact <- (�����̼� empty) ; ��
           =>
           (grab-bananas) ; ��
           (retract ?handFact ) ; ��
           (assert (�����̼� grab) )  )  ; ��
(facts)
(run)
(facts)
