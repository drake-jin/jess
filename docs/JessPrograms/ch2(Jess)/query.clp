(clear)
(reset)
(watch all)
(defquery monkey-search  ;��  
     " ������ ���� onBox�� �ִ� �������� ��ġ��?"
      (declare (variables ?��))  ;��
      (������ ?��ġ ?�� ?��))  ;��

(deffacts query-test-facts  ;��
   (������ B empty onFloor)  
   (����   A blue low)
   (������ C grab onBox) ;�� ���
   (����   B red high)
   (�ٳ��� C big)  
   (������ A empty onBox) ;�� ���
   (�ٳ��� C small)  )

(reset)
(bind ?it (run-query monkey-search onBox)) ;��
(while (call ?it hasNext)  ;�� (?it hasNext)�� ��.
             (bind ?token (call ?it next) )  ;��
             (bind ?fact (call ?token fact 1))  ;�� fact() �޼ҵ带 ȣ�� 
             (bind ?slot (fact-slot-value ?fact __data) )  ;��
             (bind ?datum (nth$ 1 ?slot) )  ;�� ?��ġ slot
             (printout t ?datum crlf)  )