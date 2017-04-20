# 선교사와 식인종 문제
### Missionaries and Cannibals Problem

# 요구조건 
 - 선교사와 식인종 각 3명이 강 왼편에 있고 이들은 강에 정착되어있는 배를 이용하여 강 오른편으로 이동하려 한다.
 - 이들은 모두 노를 저을 수 있으며 보트는 2인승이다.
 - 이들이 함께 있을 때 선교사의 수가 식인종의 수 보다 적은 경우 선교사는 먹힌다고 한다. 
 - 이들이 모두 강의 오른편으로 안전하게 이동할 수 있는 방법을 구하라.
 
# 상태공간 정의
 - M = 선교사
 - C = 식인종
 - B = 배
 - FactSet = (M, C, B)

# 초기상태와 목표상태

 > 초기상태 : (3, 3, 1)
 > 목표상태 : (0, 0, 2)

# 문제해결을 위한 규칙 구하기
 1. 안전조건
  ``` 
safe_cond(M, C){
  if (M=0 or M>= 0) and (3-M=0 or 3-M >= 3-C) then
  return true
}
  ```
 2. 선교사 1명이 오른쪽으로 이동
  ```
if (B=1, M>=1, safe_cond(M-1, C)=true) then
    (M-1, C, 2)
  ```

 3. 선교사 1명이 왼쪽으로 이동
  ``` 
if (B=2, 3-M>=1, safe_cond(M+1, C)=true) then
    (M+1, C, 1)

  ```
 4. 식인종 1명이 오른쪽으로 
  ```
if (B=1, C>=1, safe_cond(M, C-1)=true) then
    (M, C-1, 2)

  ```

 5. 식인종 1명이 왼쪽으로 

  ```
if (B=2, 3-C>=1, safe_cond(M, C+1)=true) then
    (M, C+1, 1)
  ```
 
 6. 선교사 1인과 식인종 1인이 왼쪽에서 오른쪽으로 
  ```
if (B=1, M>=1, C>=1, M=C) then
    (M-1, C-1, 2)
  ```
 
 7. 선교사 1인 식인종, 1인이 오른쪽에서 왼쪽으로
  ```
if (B=2, 3-M>=1, 3-C>=1, (3-M)=(3-C)) then
    (M-1, C-1, 2)
  ```

 8. 선교사 2명이 오른쪽으로 

  ```
if (B=1, M>=2, safe_cond(M-2, C)=true) then
    (M-2, C, 2)
  ```

 9. 선교사 2명이 왼쪽으로

  ```
if (B=2, 3-M>=2, safe_cond(M+2, C)=true) then
    (M+2, C, !)
  ```

 10. 식인종 2명이 오른쪽으로

```
if (B=1, C>=2, safe_cond(M, C-2)=true) then
    (M, C-2, 2)
```


 11. 식인종 2명이 왼쪽으로

```
if (B=2, 3-C>=2, safe_cond(M, C+2)=true) then
    (M, C+2, 1)
```
 
 12. 목표상태에 도달
```
if (M=0, C=0, B=2 then)
    (halt)
```


# 무한반복 방지를 위해 트리(tree)를 이용한 프로그램 : status template

``` lisp
(deftemplate MAIN::status
    (slot search-depth)
    (slot parent)
    (multislot mcb)
    (slot last-move)
)
```

(3 3 1)인 초기상태 :
```
    (search-depth 1), 
    (parent root), 
    (mcb 3 3 1), 
    (last-move no-move)
```

























