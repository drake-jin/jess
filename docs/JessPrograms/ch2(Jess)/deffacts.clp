(watch facts)
(clear)
(deffacts catalog "원숭이와 바바나 카탈로그"  ; ①
        (원숭이위치 A)
        (원숭이손 비어있음)
        (원숭이발 방바닥)
        (상자위치 B)
        (바나나위치 C)
        (mbp A 비어있음 방바닥 B C)   )
(facts) ; ②
(reset) ; ③
(facts)