# JESS
 - 정의 :  Java Expert System Shell, 자바 언어의 특징을 가진 규칙기반 전문가 시스템 (Ruled-based expert system)이다. 
 - 특징 
  > 1. CLIPS 규칙 엔진 사용.
  > 2. 지금것은 7버전, 실습은 6버전
  > 3. Java와 연동가능 .

### Jess vs Prolog
 prolog 는 jess와 대조 되는 언어

분류 | Jess | Prolog
======================
추론방법|전진추론방법 | 후진추론방법
------------------------------------
메모리 효율 | 실행시간 효율 | 메모리 효율
 
### Jess 응용 분야
 1. 평가의 전문가 시스템
 2. 매수 매도직접 하는 로보어드바이저
 3. 네트워크 침입 감시 시스템
 4. 지능형 네트워크 시위칭
 5. ERP, CRM 솔루션 회사의 전문가 시스템

### jess 코드의 구분
 - 기호 symbol   :
 - 원소(식별자) atoms : 대소문자 구분, $ ? = 와 같은 문자열을 첫 문자로 사용될 수 없다.  
 - 수 number
 - 문자열 strings
 - 주석 comments
 - 토큰 tokens
 - 함수
 - 리스트
 - 구조 
 - 변수 일반변수는 이름 앞에 ?name 붙이고 전역변수는 ?\*name\* 이렇게...

### 코드의 Syntax 설명
 1. 리스트 
  ``` 
    ; a b c d 이런건 전부 문자열로 들어가게됨. ?a ?b ?c ?d 이건 변수니까 문자열이 아니라 변수로 사용되게 됨...
   (a b c d)
  ```
 2. 구조  
  ```
    ; 전위 연산자 같이 ... 소괄호 막 넣는 이런 구조...
    (-  (+ a b) (+ c d))
  ```
 3. 함수 
  ```
    ; printout 함수, 전위 연산 호출방식에 익숙해 지자.
    (printout t "꺄아아아" crlf)
  ``` 
  4. 변수 및 전역변수
  ``` 
    ; 일반변수는 이름앞에 ? , 전역변수는 ?\*name\*
    (bind ?name "이름")
    (defglobal ?*g_name* "이름")
  ```
  5. 일반 리스트 
  ```
    ; 리스트를 만들어봅시다. 
 in  :  (bind ?list-hi (create$ 안녕 나는 찐빵 맨)
 out :  (안녕 나는 찐빵 맨)
 in  :  (bind ?list-hi (create$ ?list-hi 그래 너는 찐빵 맨))
 out :  (안녕 나는 찐빵 맨 그래 너는 찐빵 맨)
  ```   
  6. for 문 
  ```
  (forech ?item ?list-hi
    (printout t ?item crlf)
  )
  ```  
  7. while 문
  ```
  (while (true) do 
    (printout t "무한 반복 이 다아" crlf) 
  )
  ```
  8. if 문
  ```
(if (member$ 찐빵 ?list-hi) 
then 
  (printout t "찐빵찐빵!" crlf)

else 
  (printout t "찐빵음떠 ㅠ" crlf)
  
  (if (member$ 그래 ?list-hi)
  then 
    (printout t "구래구랩!" crlf)
  else 
    (printout t "안구래영 ㅠㅠ" crlf)
  )
)
  ```
  9. deffunction 함수정의하기.
    ```
(deffunction hello (?a ?b ?c ?d)
  (printout t ?d ?c ?b ?a crlf)
)
    (hello 안녕 나는 용지니 얍)
    ```
  10. 자바 클래스 사용
    ```
(bind ?ht (new java.util.Hashtable))
(call ?ht put "key1" "계란1")
(call ?ht put "key2" "계란2")
(call ?ht put "key3" "계란3")
(call ?ht put "key4" "계란4")
(call ?ht get "key1")
(call ?ht get "key2")
(call ?ht get "key3")
(call ?ht get "key4")
    ```



### Jess 명칭
 - if 와 then에 대한 명칭
   - 전건부(premise, LHS Left Hand Side) : 조건
   - 후건부(conclusion, RHS Right Hand Side) : 수행 
   - 사실 (fact)
   - 사실들의 집합 (knowledge base) 
 - 추론엔진 (일치, 충돌집합, 실행의 프로세스로 이루어져 있다.)
   - 일치(match) : 전건부와 조건이 맞을때,
   - 충돌집합(Conflict Resolution) : 전건부와 조건이 겹치게되어 어떤것을 수행해야할지 모를 때,
   - 실행(act) : 후건부를 실행할 때.
   - 선택(select) : 충돌집합이 되었을 때 충돌 집합 중 단 하나만 선택(select)해야 한다. 
   - 쏘아지다(fire) : 충돌집합에서 하나의 규칙을 선택(select)되어져 수행할 때 이 행위를 fire 라 한다.
 - 규칙베이스 (defrule들의 집합)
   - 규칙과 사실들의 집합.
 - 작업메모리 (fact와 rule들중에서 어느것이 바뀌었는가. )
   - 현재상태를 표현하기위하며, 규칙과 사실들을 작업메모리에 로드시켜놓고 작업시킨다.


 

### Jess 주요 코드 및 메서드
 - assert - 작업메모리에 fact를 추가한다. 
 - clear - 작업메모리에 있는 모든 사실, 변수, 규칙, 함수를 없앤다.
 - deffact - 작업메모리에 초기 사실을 정의한다.
 - reset - f-0인 MAIN모듈의 initial-fact이외의 작업메모리를 전부초기화 한다.
 - retract - assert로 올려놓은 작업메모리에 존재하는 사실을 제거한다.
 - watch - 작업메모리의 작업내용을 출력한다.
 - deftemplate - slot(변수), multislot(배열) 로 구조체의 구조인 사실을 만든다.
   - modify - deftemplate 로 assert된 fact를 수정할 수 있다.
   - duplicate - deftemplate 로 assert된 fact를 수정할 수 있다. 
 - defrule - 규칙베이스에 추가할 규칙을 만든다.
 - (watch all) - 프로그램 실행시 jess의 주요 이벤트들의 동작을 출력한다.
 - (run) - 이 프로그램을 실행한다. 맨 마지막줄에 적어야 함,.
 - & - 기본적으로 and를 표시하지만 &:함꼐 쓰인는 것은 함수가 나옴을 알려주기 위함이다.
 - pipes | - or를 의미하지만 &는 |보다 우선순위가 높다.

### defquery
 중요한가 잘 모르겠다.
