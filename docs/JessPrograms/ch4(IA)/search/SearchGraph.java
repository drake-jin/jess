import java.awt.*;
import java.applet.*;
import java.util.*;

class SearchGraph extends Hashtable {
  String name ;
//SG1<-SA3:name=test
  SearchGraph(String Name) {
    name = Name ;
  }

  // reset each SearhNode in the graph
  // clear expanded and tested flags, set depth=0
//SG3<-SA20,SG19
  void reset() {
    Enumeration enum1 = this.elements() ;
    while (enum1.hasMoreElements()) {
        SearchNode nextNode = (SearchNode)enum1.nextElement();
//SG4->SN4
        nextNode.reset() ;
    }
  }

//SG2<-SA5:add node to Hashtable, using node label as key
//  void put(SearchNode node) {
//     put(node.label, node) ;
//  }

//SG5<-SA21: ±Ì¿Ã-øÏº± ≈Ωªˆ on graph
  public SearchNode depthFirstSearch(SearchNode initialNode, Object goalState)
    {
//SG6: queue ∫§≈Õ ª˝º∫
        Vector queue = new Vector() ;
//SG7: queue={sel}
        queue.addElement(initialNode) ;
//SG8->SN5
        initialNode.setTested(true) ;  // test each node once
//SG9: queue ∏« æ’¿« ≥ÎµÂ∏¶ ªË¡¶«— » ∏Ò«•ªÛ≈¬¿Œ¡ˆ ∫Ò±≥
        while (queue.size()> 0) {
          SearchNode testNode = (SearchNode)queue.firstElement() ;
          queue.removeElementAt(0) ;
//SG10->SN6: display trace information         
          testNode.trace() ;   
          if (testNode.stateSpace.equals(goalState)) return testNode ; // found it
//SG11->SN7: ªË¡¶µ» ≥ÎµÂ∞° expanded µ«æÓ ¿÷¡ˆ æ ¿∫ ∞ÊøÏ expand«œø© queue æ’ø° √ﬂ∞°
          if (!testNode.expanded) {
            testNode.expand(queue,SearchNode.FRONT);
          }
        }
        return null ;
    }
    
//SG12<-SA22: ≥ ∫Ò-øÏº± ≈Ωªˆ on graphon graph
  public SearchNode breadthFirstSearch(SearchNode initialNode, Object goalState)
  {
//SG13: queue ∫§≈Õ ª˝º∫
      Vector queue = new Vector() ;
//SG14: queue={sel}
      queue.addElement(initialNode) ;
//SG15->SN5
      initialNode.setTested(true) ;  // test each node once
//SG16: queue ∏« æ’¿« ≥ÎµÂ∏¶ ªË¡¶«— »ƒ ∏Ò¿˚ªÛ≈¬¿Œ¡ˆ ∫Ò±≥
      while (queue.size()> 0) {
        SearchNode testNode = (SearchNode)queue.firstElement() ;
        queue.removeElementAt(0) ;
        testNode.trace() ;
        if (testNode.stateSpace.equals(goalState)) return testNode ; // found it
//SG17->SN7: ªË¡¶µ» ≥ÎµÂ∞° expanded µ«æÓ ¿÷¡ˆ æ ¿∫ ∞ÊøÏ expand«œø© queue µ⁄ø° √ﬂ∞°
        if (!testNode.expanded) {
           testNode.expand(queue,SearchNode.BACK) ;
        }
      }
      return null ;
  }

//SG21<-SG20
  // this is a slightly modified depth-first search algorith
  // that stops searching at a pre-defined depth
  public SearchNode depthLimitedSearch(SearchNode initialNode,
                                       Object goalState,
                                       int maxDepth)
  {
//SG22: queue ∫§≈Õ ª˝º∫
      Vector queue = new Vector() ;
//SG23: queue={sel}      
      queue.addElement(initialNode) ;
//SG24->SN5
      initialNode.setTested(true) ;  // only test each node once
//SG25: queue ∏« æ’¿« ≥ÎµÂ∏¶ ªË¡¶«— »ƒ ∏Ò¿˚ªÛ≈¬¿Œ¡ˆ ∫Ò±≥
      while (queue.size()> 0) {
        SearchNode testNode = (SearchNode)queue.firstElement() ;
        queue.removeElementAt(0) ;
        testNode.trace() ;
        if (testNode.stateSpace.equals(goalState)) return testNode ;
//SG26: ≥ÎµÂ¿« ±Ì¿Ã(depth)∞° maxDepth∏¶ √ ∞˙«œ¡ˆ æ ¿∏∏È 
        // limit the depth of search to maxDepth
        if (testNode.depth < maxDepth) {
//SG27->SN7: ªË¡¶µ» ≥ÎµÂ∞° expanded µ«æÓ ¿÷¡ˆ æ ¿∫ ∞ÊøÏ expand«œø© queue æ’ø° √ﬂ∞°
          if (!testNode.expanded) {
              testNode.expand(queue,SearchNode.FRONT) ;
          }
        }
      }
      return null ;
  }

//SG18<-SA23: π›∫π¿˚ ¡°¡ı ±Ì¿Ã-øÏº± ≈Ωªˆ on graph
    // use depth-first search to find goal
    public SearchNode iterDeepSearch(SearchNode startNode, Object goalState) {
        int maxDepth = 10 ;                 // arbitrary limit
        for (int j=0 ; j < maxDepth ; j++) {
//SG19->SG3:
          reset() ;
//SG20->SG21:          
          SearchNode answer = depthLimitedSearch(startNode, goalState, j);
          if (answer != null) return answer;
        }
        return null ; // failed to find solution in maxDepth
    }

//SG28<-SA24: √÷º± ≈Ωªˆ on graph
    // use best-first search algorithm to find the goal
    // default implementation based on SearchNode cost
    public SearchNode bestFirstSearch(SearchNode initialNode, Object goalState)
    {
//SG29: queue ∫§≈Õ ª˝º∫
        Vector queue = new Vector() ;
//SG30:queueø° Ω√¿€ªÛ≈¬ ≥ÎµÂ∏¶ √ﬂ∞°
        queue.addElement(initialNode) ;
//SG31->SN7:only test each node once
        initialNode.setTested(true) ;  
//SG32:queue ∏« æ’¿« ≥ÎµÂ∏¶ ªË¡¶«— »ƒ ∏Ò¿˚ªÛ≈¬¿Œ¡ˆ ∫Ò±≥
        while (queue.size()> 0) {
          SearchNode testNode = (SearchNode)queue.firstElement() ;
          queue.removeElementAt(0) ;
          testNode.trace() ;
          if (testNode.stateSpace.equals(goalState)) return testNode ;
//SG33->SN5:
         // now, heuristically add nodes to queue
         // insert the child nodes according to cost
          if (!testNode.expanded) {
              testNode.expand(queue,SearchNode.INSERT) ;
          }
        }
        return null ;
    }
}
