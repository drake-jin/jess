(deffunction lengthOfList2 (?list1 ?list2 $?args)  ; ��
          "�Ϲݸ���Ʈ�� �����ϰ� ���Ұ����� ���ϴ� �Լ� ����"    
          (bind ?noOfList 0)
          (bind ?grocery-list (create$ ?list1  ?list2 ?args) ) ; ��
          (foreach ?elementOfList ?grocery-list 
             (printout t ?elementOfList crlf) 
             (bind ?noOfList (+ 1 ?noOfList ) )  )
          ?noOfList)  
(lengthOfList2 ��� �� ���� �ұ� ��)  ; ��
(lengthOfList2 ��� ��)  ; ��
;(lengthOfList2 ���)  ; �� ���� �߻�