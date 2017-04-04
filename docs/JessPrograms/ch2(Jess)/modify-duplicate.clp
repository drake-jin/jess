(clear)
(reset)
(deftemplate mbp "monkey and bananas problem"
        (slot 원숭이위치)
        (multislot 원숭이손발) ; ①
        (slot 상자위치)
        (slot 바나나위치 (default C) )  ) ; ②
(assert (mbp  ; ③   
        (원숭이위치 A)
        (원숭이손발 비어있음 방바닥)  )  ) ; ④

(modify 1 (원숭이위치 B)) ; ①
(facts)
(duplicate 1 (원숭이위치 C))
(facts)