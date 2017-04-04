(bind ?grocery-list (create$ °è¶õ »§ ¿ìÀ¯) ) ; ¨ç
(bind ?noOfList 0) 
(foreach ?elementOfList ?grocery-list 
          (printout t ?elementOfList crlf) 
          (bind ?noOfList (+ 1 ?noOfList ) )  )  ; ¨è
(while (> ?noOfList 0) do 
           (printout t (nth$ ?noOfList ?grocery-list) crlf)
           (bind ?noOfList (- ?noOfList 1) )  )  ; ¨é