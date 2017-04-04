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
    
//SA0:SearchGraph���� graph ����    
    SearchGraph graph ; 

//SA2<-SA1 �ѱ��� ���õ�� ������ graph�� "test"��� �̸����� �����.
        public static SearchGraph testGraph() {//SearchGraph���� testGraph() �޼ҵ�
//SA3->SG1:"test"��� SearchGraph���� graph ����       
           SearchGraph graph = new SearchGraph("test");
        
        // �׸�4.1���� ������ �� ���ø� graph�� node�� ǥ���Ѵ�.

//SA4->SN1:SearchNode ����
        SearchNode sel = new SearchNode("����","����");//(Label,StateSpace)
//SA5->SG2:Hashtable�� label�� "����" �� "sel" node�� �߰��Ѵ�.
        graph.put("����",sel);
        SearchNode inc = new SearchNode("��õ","��õ");
        graph.put("��õ",inc);
        SearchNode kus = new SearchNode("����","����");
        graph.put("����",kus);
        SearchNode kwj = new SearchNode("����","����");
        graph.put("����",kwj);
        SearchNode dej = new SearchNode("����","����");
        graph.put("����",dej);
        SearchNode woj = new SearchNode("����","����");
        graph.put("����",woj);
        SearchNode chc = new SearchNode("��õ","��õ");
        graph.put("��õ",chc);
        SearchNode deg = new SearchNode("�뱸","�뱸");
        graph.put("�뱸",deg);
        SearchNode mas = new SearchNode("����","����");
        graph.put("����",mas);
        SearchNode pus = new SearchNode("�λ�","�λ�");
        graph.put("�λ�",pus);
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
        Label1 = new Label("���ۻ���",Label.LEFT);
        Label1.setFont(new Font("Dialog",Font.BOLD,12));
        Label2 = new Label("��ǥ����",Label.LEFT);
        Label2.setFont(new Font("Dialog",Font.BOLD,12));
        Choice1 = new Choice();
        Choice1.setFont(new Font("Dialog",Font.BOLD,12));
        Choice2 = new Choice();
        Choice2.setFont(new Font("Dialog",Font.BOLD,12));
        Label3 = new Label("Ž�����",Label.LEFT);
        Label3.setFont(new Font("Dialog",Font.BOLD,12));
        CheckboxGrpInsearchApplet = new CheckboxGroup(); // CheckboxGroup in searchApplet
        RadioButton1 = new Checkbox("����-�켱(Depth-First)",CheckboxGrpInsearchApplet,false);
        RadioButton1.setBackground(Color.lightGray);
        RadioButton1.setFont(new Font("Dialog",Font.BOLD,12));
        RadioButton1.setFont(new Font("Dialog",Font.BOLD,12));
        RadioButton2 = new Checkbox("�ʺ�-�켱(Breadth-First)",CheckboxGrpInsearchApplet,false);
        RadioButton2.setBackground(Color.lightGray);
        RadioButton2.setFont(new Font("Dialog",Font.BOLD,12));
        RadioButton3 = new Checkbox("�ݺ��� ���� ����-�켱(IDDF)",CheckboxGrpInsearchApplet,false);
        RadioButton3.setBackground(Color.lightGray);
        RadioButton3.setFont(new Font("Dialog",Font.BOLD,12));
        RadioButton4 = new Checkbox("�ּ�(Best-First)",CheckboxGrpInsearchApplet,false);
        RadioButton4.setBackground(Color.lightGray);
        RadioButton4.setFont(new Font("Dialog",Font.BOLD,12));
        RadioButton5 = new Checkbox("A*",CheckboxGrpInsearchApplet,true);
        RadioButton5.setBackground(Color.lightGray);
        RadioButton5.setFont(new Font("Dialog",Font.BOLD,12));
        Button1 = new Button("����");
        Button1.setFont(new Font("Dialog",Font.BOLD,12));
        Button2 = new Button("����");
        Button2.setFont(new Font("Dialog",Font.BOLD,12));
        Button3 = new Button("�����");
        Button3.setFont(new Font("Dialog",Font.BOLD,12));
        Label4 = new Label("Ž�� ���",Label.LEFT);
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
        
// SA1->SA2:testGraph()�� ȣ���Ͽ� "test" graph ����
      graph = testGraph();              // build the test graph
//SA8: "test" graph hashtable�� �ʱ� �ּҰ�
     Enumeration enum = graph.keys() ; 
//SA9: ���ۻ��¿� ��ǥ������ ����Ʈ�� ��� ���ø� �߰�
     while(enum.hasMoreElements()) {
        String nodename = (String)enum.nextElement();
        Choice1.addItem(nodename);   // fill start cities
        Choice2.addItem(nodename);   // fill goal cities
     } // endwhile
//SA10: ����-�켱�� ����Ʈ��
        RadioButton1.setState(true) ;      // default to depth-first
//SA11->SN3:TextArea1�� display ȭ������ ���
        SearchNode.setDisplay(TextArea1) ; // used for trace display
//SA12, SA13: "���ۻ���"�� "��ǥ����"�� "����"�� "�λ�"���� ���� �ʱ�ȭ
        Choice1.select("����") ;        
        Choice2.select("�λ�") ;

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
// SA14:��ư �̺�Ʈ�� �߻���Ų��.("����" ��ư)
    public void Button1_Action(Object target)
    {
      // int method = 0;
// SA15: Ŭ������ �ν��Ͻ�ȭ�Ѵ�.
       SearchNode answer = null;
// SA16: Ŭ������ �ν��Ͻ�ȭ�Ѵ�.       
       SearchNode startNode ; 
// SA17: ������� ����� �������̽����� ���Ҽ� �ְ� �Ѵ�.
       String start = Choice1.getSelectedItem();
// SA18: ����� node�� ���Ѵ�.
       startNode = (SearchNode)graph.get(start);
// SA19: �������� ����� �������̽����� ���Ҽ� �ְ� �Ѵ�.
       String goal = Choice2.getSelectedItem();
// SA20->SG3: �ٸ� Ž���� ���ؼ� ��� node���� reset�Ѵ�.
       graph.reset();  
// SA21->SG5:����-�켱 Ž��
       if (RadioButton1.getState() == true) {
        TextArea1.appendText(goal + "�� ���� ����-�켱 Ž�� " + ":\n\n");
        answer = graph.depthFirstSearch(startNode,goal);
        // �޼ҵ带 ȣ���� �� ���� node�� �������� �����Ѵ�.
       } // endif
// SA22->SG12: �ʺ�-�켱 Ž��
       if (RadioButton2.getState() == true){// �ɼ� ���ڿ��� �ش� �ɼ��� ������ �Ǿ������� Ȯ���Ѵ�.
        TextArea1.appendText(goal + "�� ���� �ʺ�-�켱 Ž�� " + ":\n\n");
        answer = graph.breadthFirstSearch(startNode, goal);
       } // endif
// SA23->SG18: �ݺ��� ���� ����-�켱 Ž��
       if (RadioButton3.getState() == true) {// �ɼ� ���ڿ��� �ش� �ɼ��� ������ �Ǿ������� Ȯ���Ѵ�.
        TextArea1.appendText(goal + "�� ���� �ݺ��� ���� ����-�켱(IDDF)Ž�� " + ":\n\n");
        answer = graph.iterDeepSearch(startNode, goal); //
       } // endif
// SA24->SG28: �ּ� Ž��
       if (RadioButton4.getState() == true) {// �ɼ� ���ڿ��� �ش� �ɼ��� ������ �Ǿ������� Ȯ���Ѵ�.
        TextArea1.appendText(goal + "�� ���� �ּ�(Best-First) Ž�� " + ":\n\n");
        Choice2.select("�λ�") ; // goal must be Node("�λ�")
        answer = graph.bestFirstSearch(startNode, "�λ�");
       } // endif
       if (RadioButton5.getState() == true) {// �ɼ� ���ڿ��� �ش� �ɼ��� ������ �Ǿ������� Ȯ���Ѵ�.
        TextArea1.appendText(goal + "�� ���� A* Ž�� " + ":\n\n");
        Choice2.select("�λ�") ; // goal must be Node("�λ�")
        answer = graph.bestFirstSearch(startNode, "�λ�");
       } // endif

       if (answer == null) {// ã�� ���(����)�� ������
         TextArea1.appendText("Could not find answer!\n");
       } else {// ã�� ���(����)�� ������
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
