(clear)
(reset)
(watch all) ; ��
(deffunction goto-box ()
   (printout t "�����̰� ���ڷ� ����." crlf) )
(assert (��������ġ A)) ; ��
(assert (������ġ B))  ; ��
(defrule GOTOBOX
   ?boxFact <- (������ġ ?boxP) ; ��
   ?monkeyFact <- (��������ġ ~?boxP) ; ��
;   ?monkeyFact <- (��������ġ ?monkeyP&~?boxP )  ; �� ���� ���
;   ?monkeyFact <- (��������ġ ?monkeyP&:(neq ?monkeyP ?boxP) )  ; �� ���� ���
;   ?monkeyFact <- (��������ġ ?monkeyP&~?boxP|~B )  ; �� ���� ���
;   ?monkeyFact <- (��������ġ ?monkeyP) (test (neq ?monkeyP ?boxP)) ; �� ���� ���
   =>
   (goto-box)
   (retract ?monkeyFact)
   (assert (��������ġ ?boxP) )  ) 
(facts)
(run)
(facts)
