(clear)
(reset)
(deftemplate mbp "monkey and bananas problem"
        (slot ��������ġ)
        (multislot �����̼չ�) ; ��
        (slot ������ġ)
        (slot �ٳ�����ġ (default C) )  ) ; ��
(assert (mbp  ; ��   
        (��������ġ A)
        (�����̼չ� ������� ��ٴ�)  )  ) ; ��

(modify 1 (��������ġ B)) ; ��
(facts)
(duplicate 1 (��������ġ C))
(facts)