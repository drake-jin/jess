(clear)
(reset)
(watch all)
(defquery monkey-search  ;①  
     " 원숭이 발이 onBox에 있는 원숭이의 위치는?"
      (declare (variables ?발))  ;②
      (원숭이 ?위치 ?손 ?발))  ;③

(deffacts query-test-facts  ;④
   (원숭이 B empty onFloor)  
   (상자   A blue low)
   (원숭이 C grab onBox) ;⑤ 출력
   (상자   B red high)
   (바나나 C big)  
   (원숭이 A empty onBox) ;⑥ 출력
   (바나나 C small)  )

(reset)
(bind ?it (run-query monkey-search onBox)) ;⑦
(while (call ?it hasNext)  ;⑧ (?it hasNext)도 됨.
             (bind ?token (call ?it next) )  ;⑨
             (bind ?fact (call ?token fact 1))  ;⑩ fact() 메소드를 호출 
             (bind ?slot (fact-slot-value ?fact __data) )  ;⑪
             (bind ?datum (nth$ 1 ?slot) )  ;⑫ ?위치 slot
             (printout t ?datum crlf)  )