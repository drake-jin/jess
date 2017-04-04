(bind ?grocery-list (create$ 계란 빵 우유) ) ; ①
; (계란 빵 우유)
 (deffunction lengthOfList (?list)    ; ②
          "일반리스트의 원소개수를 구하는 함수 정의"    
         (bind ?noOfList 0)
         (foreach ?elementOfList ?list 
            (printout t ?elementOfList crlf) 
            (bind ?noOfList (+ 1 ?noOfList ) )  )
         ?noOfList)  
; TRUE
 (lengthOfList ?grocery-list)  ; ③
