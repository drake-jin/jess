(deffunction max (?a ?b) ; ич
            (if (> ?a ?b) then
               (return ?a)         ; иш
             else
               (return ?b)  )  )   ; ищ
(printout t "The greater of 3 and 5 is " (max 3 5) "." crlf)