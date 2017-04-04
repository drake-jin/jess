(clear)
(reset)
(deftemplate mbp "monkey and bananas problem"  ; ①
  (slot 원숭이위치)
  (slot 원숭이손)
  (slot 원숭이발)
  (slot 상자위치)
  (slot 바나나위치)  ) 
(assert (mbp ; ②
  (바나나위치 C)
  (원숭이발 방바닥)
  (상자위치 B)
  (원숭이위치 A)
  (원숭이손 비어있음)  )  )   ; ③
(facts)
