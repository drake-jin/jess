 (deffunction lengthOfList1 (?list1 ?list2 ?list3)
           "�Ϲݸ���Ʈ�� �����ϰ� ���Ұ����� ���ϴ� �Լ� ����"    
          (bind ?noOfList 0)
          (bind ?grocery-list (create$ ?list1  ?list2 ?list3) ) ; ��
          (foreach ?elementOfList ?grocery-list 
             (printout t ?elementOfList crlf) 
             (bind ?noOfList (+ 1 ?noOfList ) )  )
          ?noOfList)  
 (lengthOfList1 ��� �� ����)  ; ��