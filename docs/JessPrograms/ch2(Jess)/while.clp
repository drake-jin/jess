(bind ?grocery-list (create$ ��� �� ����) ) ; ��
(bind ?noOfList 0) 
(foreach ?elementOfList ?grocery-list 
          (printout t ?elementOfList crlf) 
          (bind ?noOfList (+ 1 ?noOfList ) )  )  ; ��
(while (> ?noOfList 0) do 
           (printout t (nth$ ?noOfList ?grocery-list) crlf)
           (bind ?noOfList (- ?noOfList 1) )  )  ; ��