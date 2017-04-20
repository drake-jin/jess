;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(printout t "=========Home work ===========" crlf)
(bind ?buy_list (create$ bread milk juice))
(bind ?store_list (create$ eggs milk bread salt soap))
(bind ?cart_list (create$ ))
(bind ?length 0)

(deffunction gotomarket (?buy ?store)
    (foreach ?buy_temp ?buy
            (
            if(member$ ?buy_temp ?store) then
                    (bind ?cart_list (create$ ?cart_list ?buy_temp))
                    (bind ?length (+ 1 ?length))
            )
    )
    (return ?cart_list)
)

(bind ?result (gotomarket ?buy_list ?store_list))

(printout t "list = " ?result crlf)
(printout t "count = " ?length crlf)









