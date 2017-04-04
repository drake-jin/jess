(clear)
(reset)
(watch all)

(deffunction goto-box () 
   (printout t "�����̰� ���ڷ� ����." crlf) )
(deffunction goto-bananas () 
   (printout t "�����̰� �ٳ����� ����." crlf) )
(deffunction push-box () 
   (printout t "�����̰� ���ڸ� �ٳ����� �δ�." crlf) )
(deffunction climb-box () 
   (printout t "�����̰� ���ڿ� �ö󰣴�." crlf) ) 
(deffunction grab-bananas () 
   (printout t "�����̰� �ٳ����� ��´�." crlf) )

(assert (��������ġ A))
(assert (�����̼� empty))
(assert (�����̹� onFloor))
(assert (������ġ B))
(assert (�ٳ�����ġ C))

(defrule GOTOBOX
   ?boxFact <- (������ġ ?boxP)
   ?monkeyFact <- (��������ġ ~?boxP)
   ?handFact <- (�����̼� empty)
   ?feetFact <- (�����̹� onFloor)
   =>
   (goto-box)
   (retract ?monkeyFact) 
   (assert (��������ġ ?boxP) )  )   

(defrule GOTOBANANAS 
;  (declare (salience -100))
   ?bananasFact <- (�ٳ�����ġ ?bananasP)  
   ?monkeyFact <- (��������ġ ~?bananasP)
   ?handFact <- (�����̼� empty)
   ?feetFact <- (�����̹� onFloor)
   =>
   (goto-bananas)
   (retract ?monkeyFact) 
   (assert (��������ġ ?bananasP) )  )  

(defrule PUSHBOX 
   ?bananasFact <- (�ٳ�����ġ ?bananasP)  
   ?monkeyFact <- (��������ġ ?monkeyP)
   ?boxFact <- (������ġ ?boxP&:(eq ?boxP ?monkeyP)&:(neq ?boxP ?bananasP))
   ?handFact <- (�����̼� empty)
   ?feetFact <- (�����̹� onFloor)
   =>
   (push-box)
   (retract ?monkeyFact) 
   (retract ?boxFact) 
   (assert (��������ġ ?bananasP) )
   (assert (������ġ ?bananasP) )  )  

(defrule CLIMBOX
   ?monkeyFact <- (��������ġ ?monkeyP)
   ?boxFact <- (������ġ ?boxP&:(eq ?boxP ?monkeyP))
   ?handFact <- (�����̼� empty)
   ?feetFact <- (�����̹� onFloor)
   ?bananasFact <- (�ٳ�����ġ ?bananasP&:(eq ?bananasP ?monkeyP) )  
   =>
   (climb-box)
   (retract ?feetFact) 
   (assert (�����̹� onBox) )  )  

(defrule GRAB
   ?monkeyFact <- (��������ġ ?monkeyP)
   ?bananasFact <- (�ٳ�����ġ ?bananasP&:(eq ?bananasP ?monkeyP))  
   ?boxFact <- (������ġ ?boxP&:(eq ?boxP ?monkeyP))
   ?handFact <- (�����̼� empty)
   ?feetFact <- (�����̹� onBox)
   =>
   (grab-bananas)
   (retract ?handFact) 
   (assert (�����̼� grab) )  )  

(run)
