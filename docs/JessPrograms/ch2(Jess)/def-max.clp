(deffunction max (?a ?b) ; ��
            (if (> ?a ?b) then
               (return ?a)         ; ��
             else
               (return ?b)  )  )   ; ��
(printout t "The greater of 3 and 5 is " (max 3 5) "." crlf)