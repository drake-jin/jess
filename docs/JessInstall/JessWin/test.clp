;; test comment

(defrule light-red
  (light red)
  =>
  (printout t "Stop" crlf))

(defrule light-green
  (light green)
  =>
  (printout t "Go" crlf))


;; test rule
(defrule light-red
  (light red)
  =>
  (printout t "Stop" crlf))

;; another rule
(defrule light-green
  (light green)
  =>
  (printout t "Go" crlf))

;; end of rules
