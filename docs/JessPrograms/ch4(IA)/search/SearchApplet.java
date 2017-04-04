// {$R searchApllet.JFM} 

import java.awt.*;
import java.applet.*;
import java.util.*;

// Class SearchApplet
public class SearchApplet extends Applet
{
    final int MenuBarHeight = 0;

    // Component Declaration
    public TextArea TextArea1;
    public Label Label1;
    public Label Label2;
    public Choice Choice1;
    public Choice Choice2;
    public Label Label3;
    public CheckboxGroup CheckboxGrpInsearchApplet;
    public Checkbox RadioButton1;
    public Checkbox RadioButton2;
    public Checkbox RadioButton3;
    public Checkbox RadioButton4;
    public Checkbox RadioButton5;
    public Button Button1;
    public Button Button2;
    public Button Button3;
    public Label Label4;
    // End of Component Declaration
    
//SA0:SearchGraph형의 graph 선언    
    SearchGraph graph ; 

//SA2<-SA1 한국의 도시들로 구성된 graph를 "test"라는 이름으로 만든다.
        public static SearchGraph testGraph() {//SearchGraph형의 testGraph() 메소드
//SA3->SG1:"test"라는 SearchGraph형의 graph 생성       
           SearchGraph graph = new SearchGraph("test");
        
        // 그림4.1에서 보여진 각 도시를 graph의 node로 표시한다.

//SA4->SN1:SearchNode 생성
        SearchNode sel = new SearchNode("서울","서울");//(Label,StateSpace)
//SA5->SG2:Hashtable에 label이 "서울" 인 "sel" node를 추가한다.
        graph.put("서울",sel);
        SearchNode inc = new SearchNode("인천","인천");
        graph.put("인천",inc);
        SearchNode kus = new SearchNode("군산","군산");
        graph.put("군산",kus);
        SearchNode kwj = new SearchNode("광주","광주");
        graph.put("광주",kwj);
        SearchNode dej = new SearchNode("대전","대전");
        graph.put("대전",dej);
        SearchNode woj = new SearchNode("원주","원주");
        graph.put("원주",woj);
        SearchNode chc = new SearchNode("춘천","춘천");
        graph.put("춘천",chc);
        SearchNode deg = new SearchNode("대구","대구");
        graph.put("대구",deg);
        SearchNode mas = new SearchNode("마산","마산");
        graph.put("마산",mas);
        SearchNode pus = new SearchNode("부산","부산");
        graph.put("부산",pus);
//SA6->SN2:
        sel.addLinks(chc, dej, inc);
        inc.addLinks(sel, dej, kus);
        kus.addLinks(inc, dej, kwj);
        kwj.addLinks(kus, dej, deg, mas);
        dej.addLinks(kwj, kus, inc, sel);
        dej.addLinks(woj, deg);
        woj.addLinks(chc, deg, dej);
        chc.addLinks(woj, sel);
        deg.addLinks(kwj, dej, woj);
        deg.addLinks(pus, mas);
        mas.addLinks(kwj, deg, pus);
        pus.addLinks(mas, deg);

//SA7: use as costs for best first search example
        // straight line distances from another Node to Node("A")
        sel.cost = 0;
        inc.cost = 45;
        kus.cost = 160;
        kwj.cost = 250;
        dej.cost = 130;
        woj.cost = 140;
        chc.cost = 90;
        deg.cost = 230;
        mas.cost = 280;
        pus.cost = 320;  // goal

        
        return graph ;

  }



    // init()
    public void init()
    {
        // Frame Initialization
        setForeground(Color.black);
        setBackground(Color.lightGray);
        setFont(new Font("Dialog",Font.BOLD,12));
        setLayout(null);
        // End of Frame Initialization

        // Component Initialization
        TextArea1 = new TextArea("");
        TextArea1.setForeground(Color.black);
        TextArea1.setBackground(Color.white);
        TextArea1.setFont(new Font("Dialog",Font.BOLD,12));
        Label1 = new Label("시작상태",Label.LEFT);
        Label1.setFont(new Font("Dialog",Font.BOLD,12));
        Label2 = new Label("목표상태",Label.LEFT);
        Label2.setFont(new Font("Dialog",Font.BOLD,12));
        Choice1 = new Choice();
        Choice1.setFont(new Font("Dialog",Font.BOLD,12));
        Choice2 = new Choice();
        Choice2.setFont(new Font("Dialog",Font.BOLD,12));
        Label3 = new Label("탐색방법",Label.LEFT);
        Label3.setFont(new Font("Dialog",Font.BOLD,12));
        CheckboxGrpInsearchApplet = new CheckboxGroup(); // CheckboxGroup in searchApplet
        RadioButton1 = new Checkbox("깊이-우선(Depth-First)",CheckboxGrpInsearchApplet,false);
        RadioButton1.setBackground(Color.lightGray);
        RadioButton1.setFont(new Font("Dialog",Font.BOLD,12));
        RadioButton1.setFont(new Font("Dialog",Font.BOLD,12));
        RadioButton2 = new Checkbox("너비-우선(Breadth-First)",CheckboxGrpInsearchApplet,false);
        RadioButton2.setBackground(Color.lightGray);
        RadioButton2.setFont(new Font("Dialog",Font.BOLD,12));
        RadioButton3 = new Checkbox("반복적 점증 깊이-우선(IDDF)",CheckboxGrpInsearchApplet,false);
        RadioButton3.setBackground(Color.lightGray);
        RadioButton3.setFont(new Font("Dialog",Font.BOLD,12));
        RadioButton4 = new Checkbox("최선(Best-First)",CheckboxGrpInsearchApplet,false);
        RadioButton4.setBackground(Color.lightGray);
        RadioButton4.setFont(new Font("Dialog",Font.BOLD,12));
        RadioButton5 = new Checkbox("A*",CheckboxGrpInsearchApplet,true);
        RadioButton5.setBackground(Color.lightGray);
        RadioButton5.setFont(new Font("Dialog",Font.BOLD,12));
        Button1 = new Button("시작");
        Button1.setFont(new Font("Dialog",Font.BOLD,12));
        Button2 = new Button("중지");
        Button2.setFont(new Font("Dialog",Font.BOLD,12));
        Button3 = new Button("지우기");
        Button3.setFont(new Font("Dialog",Font.BOLD,12));
        Label4 = new Label("탐색 결과",Label.LEFT);
        Label4.setFont(new Font("Dialog",Font.BOLD,12));
        // End of Component Initialization

        // Add()s
        add(Label4);
        add(Button3);
        add(Button2);
        add(Button1);
        add(RadioButton5);
        add(RadioButton4);
        add(RadioButton3);
        add(RadioButton2);
        add(RadioButton1);
        add(Label3);
        add(Choice2);
        add(Choice1);
        add(Label2);
        add(Label1);
        add(TextArea1);
        // End of Add()s

        InitialPositionSet();
        
// SA1->SA2:testGraph()을 호출하여 "test" graph 생성
      graph = testGraph();              // build the test graph
//SA8: "test" graph hashtable의 초기 주소값
     Enumeration enum = graph.keys() ; 
//SA9: 시작상태와 목표상태의 리스트에 모든 도시를 추가
     while(enum.hasMoreElements()) {
        String nodename = (String)enum.nextElement();
        Choice1.addItem(nodename);   // fill start cities
        Choice2.addItem(nodename);   // fill goal cities
     } // endwhile
//SA10: 깊이-우선을 디폴트로
        RadioButton1.setState(true) ;      // default to depth-first
//SA11->SN3:TextArea1을 display 화면으로 사용
        SearchNode.setDisplay(TextArea1) ; // used for trace display
//SA12, SA13: "시작상태"와 "목표상태"를 "서울"과 "부산"으로 각각 초기화
        Choice1.select("서울") ;        
        Choice2.select("부산") ;

    } // End of init()

    // start()
    public void start()
    {
    } // End of start()

    // stop()
    public void stop()
    {
    } // End of stop()

    // destroy()
    public void destroy()
    {
    } // End of destroy()

    public void paint(Graphics g)
    {
        // paint()
        // End of paint()
    }

    public void InitialPositionSet()
    {
        // InitialPositionSet()
        resize(486,565);
        TextArea1.reshape(54,29+MenuBarHeight,375,265);
        Label1.reshape(130,310+MenuBarHeight,77,16);
        Label2.reshape(306,310+MenuBarHeight,88,16);
        Choice1.reshape(111,330+MenuBarHeight,100,24);
        Choice2.reshape(293,331+MenuBarHeight,100,24);
        Label3.reshape(61,370+MenuBarHeight,81,16);
        RadioButton1.reshape(60,390+MenuBarHeight,223,17);
        RadioButton2.reshape(59,414+MenuBarHeight,199,17);
        RadioButton3.reshape(59,437+MenuBarHeight,269,17);
        RadioButton4.reshape(60,457+MenuBarHeight,161,17);
        RadioButton5.reshape(60,477+MenuBarHeight,121,17);
        Button1.reshape(91,520+MenuBarHeight,75,25);
        Button2.reshape(222,520+MenuBarHeight,75,25);
        Button3.reshape(351,519+MenuBarHeight,75,25);
        Label4.reshape(56,6+MenuBarHeight,88,16);
        // End of InitialPositionSet()
    }

    public boolean handleEvent(Event evt)
    {
        // handleEvent()
        if (evt.id == Event.WINDOW_DESTROY && evt.target == this) searchApllet_WindowDestroy(evt.target);
        else if (evt.id == Event.ACTION_EVENT && evt.target == Button1) Button1_Action(evt.target);
        else if (evt.id == Event.ACTION_EVENT && evt.target == Button2) Button2_Action(evt.target);
        else if (evt.id == Event.ACTION_EVENT && evt.target == Button3) Button3_Action(evt.target);
        // End of handleEvent()

        return super.handleEvent(evt);
    }

    // Event Handling Routines
    public void searchApllet_WindowDestroy(Object target)
    {
        System.exit(0);
    }
// SA14:버튼 이벤트를 발생시킨다.("시작" 버튼)
    public void Button1_Action(Object target)
    {
      // int method = 0;
// SA15: 클래스를 인스턴스화한다.
       SearchNode answer = null;
// SA16: 클래스를 인스턴스화한다.       
       SearchNode startNode ; 
// SA17: 출발지를 사용자 인터페이스에서 정할수 있게 한다.
       String start = Choice1.getSelectedItem();
// SA18: 출발지 node를 정한다.
       startNode = (SearchNode)graph.get(start);
// SA19: 도착지를 사용자 인터페이스에서 정할수 있게 한다.
       String goal = Choice2.getSelectedItem();
// SA20->SG3: 다른 탐색을 위해서 모든 node들을 reset한다.
       graph.reset();  
// SA21->SG5:깊이-우선 탐색
       if (RadioButton1.getState() == true) {
        TextArea1.appendText(goal + "에 대한 깊이-우선 탐색 " + ":\n\n");
        answer = graph.depthFirstSearch(startNode,goal);
        // 메소드를 호출할 때 시작 node와 도착지를 전달한다.
       } // endif
// SA22->SG12: 너비-우선 탐색
       if (RadioButton2.getState() == true){// 옵션 상자에서 해당 옵션이 선택이 되었는지를 확인한다.
        TextArea1.appendText(goal + "에 대한 너비-우선 탐색 " + ":\n\n");
        answer = graph.breadthFirstSearch(startNode, goal);
       } // endif
// SA23->SG18: 반복적 점증 깊이-우선 탐색
       if (RadioButton3.getState() == true) {// 옵션 상자에서 해당 옵션이 선택이 되었는지를 확인한다.
        TextArea1.appendText(goal + "에 대한 반복적 점증 깊이-우선(IDDF)탐색 " + ":\n\n");
        answer = graph.iterDeepSearch(startNode, goal); //
       } // endif
// SA24->SG28: 최선 탐색
       if (RadioButton4.getState() == true) {// 옵션 상자에서 해당 옵션이 선택이 되었는지를 확인한다.
        TextArea1.appendText(goal + "에 대한 최선(Best-First) 탐색 " + ":\n\n");
        Choice2.select("부산") ; // goal must be Node("부산")
        answer = graph.bestFirstSearch(startNode, "부산");
       } // endif
       if (RadioButton5.getState() == true) {// 옵션 상자에서 해당 옵션이 선택이 되었는지를 확인한다.
        TextArea1.appendText(goal + "에 대한 A* 탐색 " + ":\n\n");
        Choice2.select("부산") ; // goal must be Node("부산")
        answer = graph.bestFirstSearch(startNode, "부산");
       } // endif

       if (answer == null) {// 찾는 노드(도시)가 없으면
         TextArea1.appendText("Could not find answer!\n");
       } else {// 찾는 노드(도시)가 있으면
         TextArea1.appendText("Answer found in node " + answer.label+"\n\n");
       }

    
    }

    public void Button2_Action(Object target)
    {  // stop
       // for later use --- when we have large search problems
    }

    
    public void Button3_Action(Object target)
    {// clear

        //{{CONNECTION
        // Clear the text for TextArea
        TextArea1.setText("");
        //}}
    }

    // End of Event Handling Routines
    

} // End of Class searchApplet
