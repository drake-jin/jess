
import java.util.*;
import java.io.*;

// supports continuous, numeric discrete, and categorical variables
// could be an interface ???
//  or should we use a type to identify what king of var it is?
//  inheritance classes make things complicated later
// should value be Object so it could hold structs ???
public abstract class Variable {
String name ;
String value ;
int column ;

public Variable() {} ;

//V1<-CV2:Variable(String Name)
public Variable(String Name) {name = Name; value = null; }
void   setValue(String val) { value = val ; }
String getValue() { return value; }

// used by categorical only
Vector labels ;
void setLabels(String Labels) {
    labels = new Vector() ;
    StringTokenizer tok = new StringTokenizer(Labels," ") ;
    while (tok.hasMoreTokens()) {
         labels.addElement(new String(tok.nextToken())) ;
    }
}
// return the label with the specified index
String getLabel(int index) {
    return (String)labels.elementAt(index);
}

// return a string containing all labels
String getLabels() {
    String labelList = new String();
    Enumeration enum = labels.elements() ;
    while(enum.hasMoreElements()) {
         labelList += enum.nextElement() + " " ;
    }
    return labelList ;
}

// given a label, return its index
int getIndex(String label) {
    int i = 0, index = 0  ;
    Enumeration enum = labels.elements() ;
    while(enum.hasMoreElements()) {
        if (label.equals(enum.nextElement()))
        { index = i ; break ; }
        i++;
     }
     return i;
}


boolean categorical() { 
    if (labels != null) {
       return true ;
    } else {
       return false ; 
    }
}

// used by the DataSet class 

//V2<-DS9 
public void setColumn(int col) { column = col ; }
//(V3<-DS10) -> CV3:computeStatistics(String inValue)
public abstract void computeStatistics(String inValue) ; 
//(V5<-DS16)->CV4:normalize(String inValue, float[] outArray, int inx)
public abstract int normalize(String inValue, float[] outArray, int inx);
//V4<-DS15 
public int normalizedSize() { return 1 ; }
public String getDecodedValue(float[] act, int index) { return String.valueOf(act[index]); }

}