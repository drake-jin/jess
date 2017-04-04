(deffunction lengthOfList2 (?list1 ?list2 $?args)  ; ①
          "일반리스트를 생성하고 원소개수를 구하는 함수 정의"    
          (bind ?noOfList 0)
          (bind ?grocery-list (create$ ?list1  ?list2 ?args) ) ; ②
          (foreach ?elementOfList ?grocery-list 
             (printout t ?elementOfList crlf) 
             (bind ?noOfList (+ 1 ?noOfList ) )  )
          ?noOfList)  
(lengthOfList2 계란 빵 우유 소금 비누)  ; ③
(lengthOfList2 계란 빵)  ; ④
;(lengthOfList2 계란)  ; ⑤ 에러 발생