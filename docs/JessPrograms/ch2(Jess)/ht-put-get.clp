 (bind ?ht (new java.util.Hashtable))  ; ①
; <External-Address:java.util.Hashtable>
(call ?ht put “key1” “계란”)  ; ②
(call ?ht put “key2” “빵”)  ; ③ 
 (call ?ht put “key3” “우유”)  ; ④ 
(call ?ht get “key1”) ; ⑤