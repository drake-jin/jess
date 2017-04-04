(clear)
(reset)
;(watch all)

(deffunction q1 (?sentence-str) 
   (printout t ?sentence-str "은 문법에 맞습니다." crlf) )

(assert (det "The") )
(assert (det "A") )
(assert (det "An") )
(assert (noun "Time") )
(assert (noun "Flies") )
(assert (noun "Arrow") )
(assert (verb "Flies") )
(assert (verb "Like") )
(assert (verb "Time") )
(assert (p "Like") )
(assert (adj "Time") )

(deffunction p-Str (?str1 ?str2)
  (bind ?p-Str (str-cat ?str1 ?str2) )
  ?p-Str  )

(defrule NP 
   ?detFact <- (det ?detW)   
   ?nounFact <- (noun ?nounW)   
   ?adjFact <- (adj ?adjW)   
   =>
   (assert (np ?nounW)  )
   (assert (np (p-Str ?detW ?nounW) ) )
   (assert (np (p-Str ?adjW ?nounW) ) )
)  

(defrule VP 
   ?verbFact <- (verb ?verbW) 
   ?npFact <- (np ?np-Str)  
   ?ppFact <- (pp ?pp-Str) 
   =>
   (assert (vp ?verbW)  ) 
   (assert (vp (p-Str ?verbW ?np-Str) ) )
   (assert (vp (p-Str ?verbW ?pp-Str) ) )
)

(defrule PP 
   ?pFact <- (p ?pW) 
   ?npFact <- (np ?np-Str)  
   =>
   (assert (pp (p-Str ?pW ?np-Str) ) )
)

(defrule SENTENCE 
   ?npFact <- (np ?np-Str) 
   ?vpFact <- (vp ?vp-Str)  
   ?ppFact <- (pp ?pp-Str) 
   =>
   (assert (sentence ?vp-Str) )
   (assert (sentence (p-Str ?np-Str ?vp-Str) ) )
   (assert (sentence (p-Str (p-Str ?np-Str ?pp-Str) ?vp-Str) ) )
)

(defrule Q1
   ?sFact <- (sentence "TimeFliesLikeAnArrow") 
   =>
   (q1 "TimeFliesLikeAnArrow")
   (halt)
)

(run)
