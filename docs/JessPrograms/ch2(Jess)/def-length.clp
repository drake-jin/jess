(bind ?grocery-list (create$ ��� �� ����) ) ; ��
; (��� �� ����)
 (deffunction lengthOfList (?list)    ; ��
          "�Ϲݸ���Ʈ�� ���Ұ����� ���ϴ� �Լ� ����"    
         (bind ?noOfList 0)
         (foreach ?elementOfList ?list 
            (printout t ?elementOfList crlf) 
            (bind ?noOfList (+ 1 ?noOfList ) )  )
         ?noOfList)  
; TRUE
 (lengthOfList ?grocery-list)  ; ��
