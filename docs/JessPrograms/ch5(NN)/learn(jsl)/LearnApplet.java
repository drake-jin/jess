

import java.awt.*;
import java.applet.*;
import java.util.* ;

public class LearnApplet extends Applet {
	void button1_Clicked(Event event) {

	   // first get the selected data set
       String dsName = choice1.getSelectedItem() ;
       dataSet = (DataSet)dataSets.get(dsName) ;

//LA4->LA5:testBackProp(dataSet, textArea2)
	   if (radioButton1.getState() == true) testBackProp(dataSet, textArea2);
	   if (radioButton2.getState() == true) testKMapNet(dataSet,textArea2) ;
       if (radioButton3.getState() == true) testDecisionTree(dataSet, textArea2); // Decision tree
	}
	void button2_Clicked(Event event) {
            // for future use
	}
	void button3_Clicked(Event event) {

		//{{CONNECTION
		// Clear the text for TextArea
		textArea2.setText("");
		//}}
	}

	// load the selected data set
    public void button4_Clicked(Event event) {
        String dsName = choice1.getSelectedItem() ;
        dataSet = (DataSet)dataSets.get(dsName) ;
        if (dataSet.numRecords > 0) {// int numRecords=0 ;으로  DS에서 초기화
          textArea1.appendText("\nDataSet '" + dsName + "' was already loaded!\n") ;
        } else {
//LA3->DS3:loadDataFile()
          dataSet.loadDataFile() ; // load the data set
        }
	}


	public void init() {
		super.init();

	 
		//{{INIT_CONTROLS
		setLayout(null);
		addNotify();
		resize(518,495);
		label1 = new java.awt.Label("Data Set");
		label1.reshape(12,12,168,25);
		add(label1);
		textArea1 = new java.awt.TextArea();
		textArea1.reshape(0,48,516,120);
		add(textArea1);
		textArea2 = new java.awt.TextArea();
		textArea2.reshape(0,204,516,168);
		add(textArea2);
		button1 = new java.awt.Button("Start");
		button1.reshape(36,444,96,36);
		add(button1);
		button2 = new java.awt.Button("Stop");
		button2.reshape(192,444,109,32);
		add(button2);
		button3 = new java.awt.Button("Reset");
		button3.reshape(372,444,97,36);
		add(button3);
 		Group1 = new CheckboxGroup();
		radioButton1 = new java.awt.Checkbox("Back prop", Group1, false);
		radioButton1.reshape(24,412,135,24);
 		add(radioButton1) ; 
		radioButton2 = new java.awt.Checkbox("Kohonen map", Group1, false);
		radioButton2.reshape(180,412,114,22);
 		add(radioButton2) ; 
		radioButton3 = new java.awt.Checkbox("Decision Tree", Group1, false);
		radioButton3.reshape(348,412,132,24);
 		add(radioButton3) ; 
		choice1 = new java.awt.Choice();
		add(choice1);
		choice1.reshape(180,12,182,24);
		label2 = new java.awt.Label("Status ");
		label2.reshape(0,168,416,31);
		add(label2);
		button4 = new java.awt.Button("Load");
		button4.reshape(396,12,108,28);
		add(button4);
		//}}
		radioButton1.setState(true) ; // select back prop
// LA1->DS1:setDisplay(textArea1)		
        DataSet.setDisplay(textArea1) ; 
        dataSets = new Hashtable() ;
        choice1.addItem("XOR") ;   // select XOR as default
//LA2->DS2:new DataSet("XOR","xor") 생성
        dataSets.put("XOR", new DataSet("XOR","xor")) ;
        choice1.addItem("Vehicles");
        dataSets.put("Vehicles", new DataSet("Vechicles", "vehicles")) ;
        choice1.addItem("Restaurant");
        dataSets.put("Restaurant", new DataSet("Restaurant","resttree")) ;
        choice1.addItem("Linear Ramp") ;
        dataSets.put("Linear Ramp", new DataSet("Linear Ramp","ramp2")) ;
        choice1.addItem("Cluster Data") ;
        dataSets.put("Cluster Data", new DataSet("Cluster Data","kmap1")) ;
        choice1.addItem("Animal Data") ;
        dataSets.put("Animal Data", new DataSet("Animals","animal")) ;
        choice1.addItem("XOR Tree Data") ;
        dataSets.put("XOR Tree Data", new DataSet("XOR Tree Data","xortree")) ;

        }

	public boolean handleEvent(Event event) {
		if (event.target == button1 && event.id == Event.ACTION_EVENT) {
			button1_Clicked(event);
			return true;
		}if (event.target == button2 && event.id == Event.ACTION_EVENT) {
			button2_Clicked(event);
			return true;
		}if (event.target == button3 && event.id == Event.ACTION_EVENT) {
			button3_Clicked(event);
			return true;
		}if (event.target == button4 && event.id == Event.ACTION_EVENT) {
			button4_Clicked(event);
			return true;
		}
		return super.handleEvent(event);
	}

	//{{DECLARE_CONTROLS
	java.awt.Label label1;
	java.awt.TextArea textArea1;
	java.awt.TextArea textArea2;
	java.awt.Button button1;
	java.awt.Button button2;
	java.awt.Button button3;
 	java.awt.Checkbox radioButton1;
	CheckboxGroup Group1;
	java.awt.Checkbox radioButton2;
	java.awt.Checkbox radioButton3;
	java.awt.Choice choice1;
	java.awt.Label label2;
	java.awt.Button button4;
	//}}
	Hashtable dataSets ;
	DataSet dataSet = null ;

//LA5<-LA4
public static void testBackProp(DataSet dataSet, TextArea bottomText) {
//LA6->BP1
	    BackProp testNet = new BackProp("Test Back Prop Network");
	    testNet.textArea1 = bottomText ; 
	    testNet.ds = dataSet ; 
        testNet.numRecs = dataSet.numRecords ;
        testNet.fieldsPerRec = dataSet.normFieldsPerRec ; 
        testNet.data = dataSet.normalizedData ;    // get vector of data
//LA7->DS21:getClassFieldSize()
        int numOutputs = dataSet.getClassFieldSize() ; //1
	    int numInputs = testNet.fieldsPerRec - numOutputs; //3-1=2    
//LA8->BP2:createNetwork(2,2,1)	    
	    testNet.createNetwork(numInputs,numInputs,numOutputs) ;
	    int maxNumPasses = 2500 ;  // default -- could be on applet
	    int numSteps = maxNumPasses * testNet.numRecs ; 
	    for (int i=0 ; i < numSteps; i++) {
//LA9->BP5
	      testNet.process() ;   // train 
	    }
	    testNet.textArea1.appendText("\n Passes Completed: " + maxNumPasses + 
                              "  RMS Error = " + testNet.aveRMSError + " \n") ; 	
   
       testNet.mode = 1 ;  // lock the network
	    // do a final pass and display the results   
	    for (int i=0 ; i < testNet.numRecs ; i++) {
	        testNet.process() ;   // test
	        testNet.display_network() ;   
	    } 
  	}

public static void testKMapNet(DataSet dataSet, TextArea bottomText) {
	    KMapNet testNet = new KMapNet("Test Kohonen Map Network");

        testNet.textArea1 = bottomText ; 
        testNet.ds = dataSet ;      
        testNet.numRecs = dataSet.numRecords ;
        testNet.fieldsPerRec = dataSet.fieldsPerRec ;      
        testNet.data = dataSet.normalizedData ; // get vector of data
                
        // create network, all fields are inputs
	    testNet.createNetwork(testNet.fieldsPerRec, 4, 4) ;
        int maxNumPasses = 20 ;  // default -- could be on applet
	    int numCycles = maxNumPasses * testNet.numRecs ; 
	  
        // train the network
	    for (int i=0 ; i < numCycles; i++) {
	      testNet.cluster() ;   // train
        }
        
	    testNet.mode = 1 ;  // lock the network weights
	    for (int i=0 ; i < testNet.numRecs ; i++) {
          testNet.cluster() ;   // test
          testNet.display_network() ;
        }
	}

public static void testDecisionTree(DataSet dataSet, TextArea bottomText)
 {
    DecisionTree tree = new DecisionTree("\n Test Decision Tree ") ;
    tree.textArea1 = bottomText ;
    tree.ds = dataSet ;
    
  tree.textArea1.appendText("Starting DecisionTree ") ;

 tree.fieldsPerRec = dataSet.fieldsPerRec; 
 tree.examples = dataSet.data ; // get vector of data
 tree.variableList = dataSet.variableList ;
 tree.classVar = (Variable)tree.variableList.get("ClassField") ; 
 tree.variableList.remove("ClassField") ;  

 
 Node root = tree.buildDecisionTree(tree.examples, tree.variableList, new Node("default")) ;  // recursively build tree

 // now display the results
 tree.textArea1.appendText("\nDecisionTree -- classVar = " + tree.classVar.name);
 Node.displayTree(root) ;
 tree.textArea1.appendText("\nStopping DecisionTree - success!") ;
}


}
