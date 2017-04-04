(clear)
(reset)
;(watch all)

(deffunction q1 (?sentence-str) 
   (printout t ?sentence-str "은 문법에 맞습니다." crlf) )

(assert (det The))
(assert (det A))
(assert (det An))
(assert (noun Apple))
(assert (noun Man))
(assert (verb Eats))
(assert (verb Sing))

(deffunction p-Str (?str1 ?str2)
  (bind ?p-Str (str-cat ?str1 ?str2) )
  ?p-Str  )

(defrule NP 
   ?detFact <- (det ?detW)  
   ?nounFact <- (noun ?nounW)  
   =>
   (assert (np (p-Str ?detW ?nounW))  )
)  

(defrule VP 
   ?verbFact <- (verb ?verbW) 
   ?vpFact <- (np ?np-Str)  
   =>
   (assert (vp ?verbW)  ) 
   (assert (vp (p-Str ?verbW ?np-Str))  )
)

(defrule SENTENCE 
   ?npFact <- (np ?np-Str) 
   ?vpFact <- (vp ?vp-Str)  
   =>
   (assert (sentence (p-Str ?np-Str ?vp-Str))  )
)

(defrule Q1
   ?sFact <- (sentence "TheManEatsTheApple") 
   =>
   (q1 TheManEatsTheApple)
   (halt)
)

(run)