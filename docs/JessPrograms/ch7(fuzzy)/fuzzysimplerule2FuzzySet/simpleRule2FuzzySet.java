

import nrc.fuzzy.*;

public class simpleRule2FuzzySet
{

  public simpleRule2FuzzySet()
  {
  }

  public static void main(String[] argv) throws FuzzyException
  {
// Step 1  (define the FuzzyVariables)

	  double xHot[] = {25, 35}; 
	  double yHot[] = {0, 1}; 
	  double xCold[] = {5, 15}; 
	  double yCold[] = {1, 0}; 

//	 define our temperature FuzzyVariable with terms hot, 
//	 cold, very hot and medium 
	FuzzyVariable temp = new FuzzyVariable("temperature", 0, 65, "C"); 
	temp.addTerm("hot", xHot, yHot, 2); 
	temp.addTerm("cold", xCold, yCold, 2); 
	temp.addTerm("veryHot", "very hot"); 
	temp.addTerm("medium", "(not hot and (not cold))"); 
//	 define our pressure FuzzyVariable with terms low, medium and high 
	FuzzyVariable pressure = new FuzzyVariable("pressure", 0, 10, "kilo-pascals"); 
	pressure.addTerm("low", new ZFuzzySet(2.0, 5.0)); 
	pressure.addTerm("medium", new PIFuzzySet(5.0, 2.5)); 
	pressure.addTerm("high", new SFuzzySet(5.0, 8.0)); 
	
//	 provide the fuzzified inputs for the rule
    double inputTemp = 32.5;
    // create fuzzy values from the crisp values
    FuzzyValue inputTempFval =  new FuzzyValue(temp, new TriangleFuzzySet(inputTemp-0.05, 
    		inputTemp, inputTemp+0.05));
	
//	 let's build a rule --- 
	FuzzyRule rule1 = new FuzzyRule(); 
	FuzzyValue antecedentFval = new FuzzyValue(temp, "medium or veryhot"); 
	FuzzyValue conclusionFval = new FuzzyValue(pressure, "low or medium"); 
//	FuzzyValue inputFval = new FuzzyValue(temp, "very medium"); 
	rule1.addAntecedent(antecedentFval); 
	rule1.addConclusion(conclusionFval); 
	rule1.addInput(inputTempFval); 
//	rule1.addInput(inputFval); 
//	 execute this simple rule with a single antecedent and 
//	 a single consequent using default rule executor -- 
//	 MamdaniMinMaxMinRuleExecutor 
	FuzzyValueVector fvv = rule1.execute(); 
//	 show the results using the plotting methods for FuzzyValues 
	FuzzyValue fvals[] = new FuzzyValue[2]; 
	fvals[0] = antecedentFval; // Àü°ÇºÎ FuzzySet
	fvals[1] = inputTempFval;  
	FuzzyValue fvals2[] = new FuzzyValue[1]; 
	fvals2[0] = conclusionFval; 
	System.out.println(FuzzyValue.plotFuzzyValues("*+", 0, 65, fvals)); 
	System.out.println(FuzzyValue.plotFuzzyValues("*", 0, 10, fvals2)); 
//	System.out.println(fvals2[0].plotFuzzyValue("*", 0, 10)); 
	System.out.println(fvv.fuzzyValueAt(0).plotFuzzyValue("*", 0, 10)); 
//	 execute again with a different rule executor -- 
//	 LarsenProductMaxMinRuleExecutor 
//	fvv = rule1.execute(new LarsenProductMaxMinRuleExecutor()); 
//	 and show results 
//	System.out.println(fvv.fuzzyValueAt(0).plotFuzzyValue("*", 0, 10));

  }

}
