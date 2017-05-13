# Chapter03 탐색 방법 
 이번장은 인공지능 분야중 하나인 문제 해결에서 기본적으로 다루고 있는 탐색방법에 대해서 살펴본다.
장기나 바죽처럼 현재의 상태에서 어떤 수를 두어야 상대를 이기는지 몇 수 앞을 내다보아야 하는데 이러한 과정이 바로 탐색이다.

- 주어진 문제를 컴퓨터가 처리할 수 있도록 문제를 정의해야한다.
- 현재 상태를 표현할 수 있는 상태공간을 정의한 후 이 문제에 적절한 탐색 방법을 찾아 적용해야한다.

## 3-1 탐색을 위한 준비.

 1. 상태공간 정의 : 주어진 문제의 모든 가능한 상태를 표현할 수 있는 방법을 정의한다.
 2. 초기상태와 목표상태 지정 : 초기상태(처음시작), 목표상태(이기는 조건)
 3. 규칙만들기 : 규칙은 가능한 간단하게 만든다.
 4. 탐색방법 적용 : jess의 기본적인 탐색은 DFS(깊이 우선 탐색)방법을 자동적으로 적용한다.

### 탐색의 다양한 방법
 - brute-force 와 blind 탐색 : 탐색이 체계적이지 않은 방식
 - 휴리스틱(heuristic), informed, directed 탐색 : 탐색이 체계적인 방식

구분|Brute-Force, Uninformed, Blind Search | Heuristic, Informed, Directed 
----|--------------------------------------|------------------------------
종류|깊이우선탐색, 너비우선탐색            | Best-Frist 탐색, Hill-Climbing 탐색   
종류| Iterative-Deeping깊이 우선 탐색      | A\*탐색, Minimax 프로시져,
종류|                 X                     |  Iterative-deeping A\*탐색


### 알고리즘의 평가
 - Optional (최적) : 알고맂므으로 찾은 경로나 상태가 가장 좋은 것일 때 사용된다.
 - Complete : 초기상태에서 목표상태까지의 경로가 존재하고 반드시 그 알고리즘은 목표상태를 찾는것을 보장한다.
 - Complexity(복잡도) :시간(time)복잡도와 공간(space)복잡도가 존재한다.


## 3-2 그래프 (Graph)
그래프는 방향성이 있는 경우  *방향성 그래프(Directed Graph)* 와 방향성이 없는  *비방향성 그래프(Undirected Graph)* 로 나눌 수 있다. 


> 정의 : 그래프(Graph)  G = (V, E) 는 유한개의 공집합이 아닌 노드V (Vertics) 와 이를 이루고 있는 호 E(edges, arcs) 로 이루어져있다.
> v는 머리 (head), w는 꼬리(tail) 이라 한다. 

#### 방향성 그래프
 ![방향성 그래프 ](https://www.google.co.kr/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiYld7dpezTAhVVImMKHSYeDyYQjRwIBw&url=http%3A%2F%2Fstackoverflow.com%2Fquestions%2F40929724%2Fdetecting-a-cycle-in-a-graph-using-three-color-mechanism&psig=AFQjCNFJUFZcnekkKGtWH8h4CmqkFgRxfw&ust=1494745142577136)

#### 비방향성 그래프
 ![비방향성 그래프](https://www.google.co.kr/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiHpY37pezTAhVEwGMKHQo0CZcQjRwIBw&url=http%3A%2F%2Fstackoverflow.com%2Fquestions%2F27944340%2Flongest-path-in-unweighted-undirected-graph&psig=AFQjCNG73nf4hBDnBxZkZWaDBTvLU77o8g&ust=1494745203752543)

## 3-3 깊이 우선 탐색 Depth First Search
 ![Depth First Search](https://www.google.co.kr/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwjs-MyxpuzTAhUE-GMKHcOeCZYQjRwIBw&url=https%3A%2F%2Fwww.cp.eng.chula.ac.th%2F~piak%2Fteaching%2Fioi2007%2Folympic2007%2Fsearchimp.html&psig=AFQjCNFg0x5TRA-OClhvNvR9Kq90QJv9zA&ust=1494745286928911)
 ![Depth First Search](https://www.google.co.kr/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwi-i7q9puzTAhVI2WMKHTcvC2QQjRwIBw&url=http%3A%2F%2Fwww.stoimen.com%2Fblog%2F2012%2F09%2F17%2Fcomputer-algorithms-graph-depth-first-search%2F&psig=AFQjCNFg0x5TRA-OClhvNvR9Kq90QJv9zA&ust=1494745286928911)
 
###깊이 우선 탐색 (Depth First Search)알고리즘 분석
 - 목표노드를 찾기 까지의 시간과 소모되는 시간, 소모되는 메모리의 양을 파악해야한다. (시간복잡도와 공간복잡도의 파악)
 - 자식의 수 = 분기계수(branching factor) b로 표시한다.
 - 트리의 깊이 = 깊이 (Depth) d로 표시한다. 
 
> *특징*
> - 목표노드가 트리의 제일 왼쪽에 존재할 경우 목표노드를 쉽게 찾는다.
> - 제일 오른쪽의 가장 깊은 곳에 존재한다면 전체 노드를 방문해야한다.
> - 이 경우에어서 왼쪽 부분의 트리가 무한히 깊어진다면 목표노드를 찾지 못할 수 있다.

> Time Complexity Theory [무한히 깊어지지 않는 경우]
> 최악의 DFS 모델
> 1 + b + b^2 + b^3 .... + b^d = ((b^(d+1)-1)/(b-1))

> Space Complexity Theory [어떤 경우라 하더라도]
> 메모리의 경우 초기노드인 뿌리노드부터 목표노드까지의 경로만 기억하면 됨으로 
> d * b = db 

결론적으로 깊이-우선 탐색은 메모리에 대해서 매우 효율적이며 시간에 대해서 탐색노드의 수에 비례하게 된다.
목표노드가 상태공간에 여러개 존재 할 때 깊이가 깊은 해를 찾을 수 있으므로 찾은해가 최적의 해라한다.

### 너비 우선 탐색 (Breadth First Search)알고리즘 분석
 - 다음 깊이에 있는 모든 자식을 탐색해야한다.
 - 분석은 DFS와 마찬가지로 목표노드를 찾기까지의 노드 수를 구하면 된다.
 - 시간복잡도와 공간복잡도는 당연하게...
 
> Time Complextity Theory [무한이 깊어지는 경우]
> 최악의 BFS = 최악의 DFS 와 같다.

> Space Complexity Theory 
> 다음 노드를 탐색하기 위해 현재 깊이에 있는 모드를 기억하기 때문에
> DFS보다 비효율적이라고 하지만... 아닌거같다. 그냥 아니다. 


## 휴리스틱 탐색
 - 좋은 경로만 탐색하는 방법
 - 휴리스틱을 이용하여 탐색하는것이 휴리스틱 탐색
 - 휴리스틱으로 찾은 해를 최적의 해라는것을 보장하지 않지만 그래도 좋은 해를 줄 수 있으며, 시간과 메모리의 폭증을 방지할 수 있다.
 - 휴리스틱 함수는 현재의 상태에서 다음 상태를 선택할 때 가장 좋다고 여겨지는 상태를 선택해야 하는데 이를 측정해주는 함수를 말한다.
 - 결과값은 수치로 표현되며 좋은휴리스틱 함수일수록 좋은 해를 줄 수 있다.







