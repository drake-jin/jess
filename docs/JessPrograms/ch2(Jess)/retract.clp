(watch facts)
(reset)
(assert (원숭이위치 A)) ;  
(assert (원숭이손 비어있음) ) ;  
(assert (원숭이발 방바닥) ) ;  
(assert (상자위치 B) ) ;  
(assert (바나나위치 C)) ; 

(retract 1) ; ①
(assert (원숭이위치 B)) ; ③ 
(facts)