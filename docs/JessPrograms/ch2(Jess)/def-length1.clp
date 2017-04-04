 (deffunction lengthOfList1 (?list1 ?list2 ?list3)
           "일반리스트를 생성하고 원소개수를 구하는 함수 정의"    
          (bind ?noOfList 0)
          (bind ?grocery-list (create$ ?list1  ?list2 ?list3) ) ; ①
          (foreach ?elementOfList ?grocery-list 
             (printout t ?elementOfList crlf) 
             (bind ?noOfList (+ 1 ?noOfList ) )  )
          ?noOfList)  
 (lengthOfList1 계란 빵 우유)  ; ②