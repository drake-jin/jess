import java.awt.*;
import java.applet.*;
import java.util.*;

public class SearchNode extends Object { 
  String label ;     // symbolic name 
  Object stateSpace ;     // defines the state-space
  Object oper;       // operator used to generate this node
  Vector links;      // edges or links to other nodes
  int depth ;        // depth in a tree from start node
  boolean expanded ; // indicates if node has been expanded
  boolean tested ;   // indicates if node was ever tested 
  float cost=0 ;     // cost to get to this node  
  
  static TextArea textArea1 ; // used for trace output only 
  public static final int FRONT = 0 ;
  public static final int BACK = 1 ;
  public static final int INSERT = 2;  
//SN1<-SA4: ("서울", "서울") 등을 매개변수로 다음을 초기화하면서 SearchNode 생성
  SearchNode(String Label, Object StateSpace) {
    label = Label ; stateSpace = StateSpace ; depth = 0 ;
    links = new Vector() ; oper = null ;
    expanded = false ; tested = false ;
  }
//SN2<-SA6: 매개변수의  수에 따라 다음 중 하나가 호출된다.
  public void addLink(SearchNode Node) { // 매개변수가 하나인 경우
                links.addElement(Node);//links 벡터에 추가
  }
  
  public void addLinks(SearchNode n1, SearchNode n2) {//매개변수가 두 개인 경우
                links.addElement(n1) ; 
                links.addElement(n2) ;
  }
  
  public void addLinks(SearchNode n1, SearchNode n2, SearchNode n3) {
                links.addElement(n1) ;// 매개변수가 세 개인 경우
                links.addElement(n2) ; 
                links.addElement(n3) ; 
  }
  
  public void addLinks(SearchNode n1, SearchNode n2, 
                       SearchNode n3, SearchNode n4) {// 매개변수가 네 개인 경우
                links.addElement(n1) ; links.addElement(n2) ; 
                links.addElement(n3) ; links.addElement(n4) ; 
  }
  
  public void addLinks(Vector Nodes) {// 매개변수가 다섯 개 이상인 경우
               for (int i=0 ; i < Nodes.size() ; i++) {
                  links.addElement(Nodes.elementAt(i)) ; 
               }
  }
  public boolean leaf() { return (links.size() == 0) ; }
  public void setDepth(int Depth) { depth = Depth; }
  public void setOperator(Object Oper) { oper = Oper; }
//SN9<-SG8
  public void setExpanded() { expanded = true; } 
  public void setExpanded(boolean TFexpanded) { expanded = TFexpanded; } 
//SN5<-SG8,SG15,SG24,SG31 
  public void setTested(boolean TFtested) { tested = TFtested ; } 

//SN3<-SA11
  static public void setDisplay(TextArea textArea) { textArea1 = textArea; } 
  
  // initialize the node for another search 
//SN4<-SG4
  public void reset() {
      depth = 0 ;
      expanded = false ;
      tested = false ; 
  }
  
//SN6<-SG10: write a trace statement -- indent to indicate depth  
  public void trace() {
       String indent = new String() ; 
       for (int i=0 ; i < depth ; i++) indent += "  " ; 
       textArea1.appendText(indent + "Searching " + depth + ": " + label +
                           " with state-space = " + stateSpace + "\n") ;                                  
  }

//SN7<-SG11,SG17,SG26,SG33: expand the node and add to queue at specified position
  // position 0=front, 1=back, 2=based on node cost
  public void expand(Vector queue, int position) {
//SN8->SN9:
      setExpanded() ; 
      for (int j = 0; j < links.size(); j++) {
         SearchNode nextNode = (SearchNode)links.elementAt(j) ;
         if (!nextNode.tested) {
           nextNode.setTested(true) ; 
           nextNode.setDepth(depth+1) ; 
           switch (position) {
            case FRONT: queue.insertElementAt(nextNode,0); 
                    break ;
            case BACK: queue.addElement(nextNode);   
                    break ;
            case INSERT: 
             boolean inserted = false ;
             float nextCost = nextNode.cost ; 
             for (int k=0 ; k < queue.size() ; k++) { 
               // find where to insert this node
               if (nextCost < ((SearchNode)queue.elementAt(k)).cost) {
                 queue.insertElementAt(nextNode, k); // insert in middle 
                 inserted = true ;
                 break ;     // exit the for loop
               } 
             } 
             // couldn't find place to insert, just add to end
             if (!inserted) queue.addElement(nextNode) ;  
             break;                    
           }  
         } 
     }         
  }
}