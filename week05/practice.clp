
(clear)
(watch all)

(deftemplate MAIN::status
    (slot search-depth)
    (slot parent)
    (multislot mcb)
    (slot last-move)
)

(deffacts MAIN::initial-positions
    (status
        (search-depth 1)
        (parent root)
        (mcb 3 3 1)
        (last-move no-move)
    )
)

; if (m=0 || m>=C) && (3-m=0 || 3-M >= 3-C)
(deffunction safe-condition (?m ?c)
    (if (and (or (= ?m 0) (>= ?m ?c)) (or (= (- 3 ?m) 0) (>= (- 3 ?m) (- 3 ?c)))) then 
        (return TRUE)
    )
)

(reset)
(run)
