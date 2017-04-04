

import nrc.fuzzy.*;

public class simpleRule2RulesSugeno
{

  public simpleRule2RulesSugeno()
  {
  }

  public static void main(String[] argv) throws FuzzyException
  {
// Step 1  (define the FuzzyVariables for temperature, flow, cold valve change and hot valve change)

	  //	 some values used to describe the fuzzy terms in the temperature FuzzyVariable 
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
	FuzzyValue antecedentFval = new FuzzyValue(temp, "medium or hot"); 
	FuzzyValue conclusionFval = new FuzzyValue(pressure, "low"); 
	FuzzyRule rule2 = new FuzzyRule(); 
	FuzzyValue antecedentFval2 = new FuzzyValue(temp, "medium and hot"); 
	FuzzyValue conclusionFval2 = new FuzzyValue(pressure, "medium"); 
	
//	FuzzyValue inputFval = new FuzzyValue(temp, "very medium"); 
	rule1.addAntecedent(antecedentFval); 
	rule1.addConclusion(conclusionFval); 
	rule1.addInput(inputTempFval); 
	rule2.addAntecedent(antecedentFval2); 
	rule2.addConclusion(conclusionFval2); 
	rule2.addInput(inputTempFval); 
//	rule1.addInput(inputFval); 
//	 execute this simple rule with a single antecedent and 
//	 a single consequent using default rule executor -- 
//	 MamdaniMinMaxMinRuleExecutor 
	FuzzyValueVector fvv = rule1.execute(); 
	FuzzyValueVector fvv2 = rule2.execute(); 
	
    // aggregate the results from the 2 rules
    FuzzyValue zFVal = fvv.fuzzyValueAt(0).fuzzyUnion(fvv2.fuzzyValueAt(0));
      
//	 show the results using the plotting methods for FuzzyValues 
	FuzzyValue fvals[] = new FuzzyValue[2]; 
	fvals[0] = antecedentFval; // 전건부 FuzzySet
	fvals[1] = inputTempFval;  
	FuzzyValue fvalsC[] = new FuzzyValue[1]; 
	fvalsC[0] = conclusionFval; 
	
	System.out.println(FuzzyValue.plotFuzzyValues("*+", 0, 65, fvals)); 
	System.out.println(FuzzyValue.plotFuzzyValues("*", 0, 10, fvalsC)); 
	System.out.println(fvv.fuzzyValueAt(0).plotFuzzyValue("*", 0, 10)); 

	FuzzyValue fvalsR2[] = new FuzzyValue[2]; 
	fvalsR2[0] = antecedentFval2; // 전건부 FuzzySet
	fvalsR2[1] = inputTempFval;  
	FuzzyValue fvalsR2C[] = new FuzzyValue[1]; 
	fvalsR2C[0] = conclusionFval2; 
	
	System.out.println(FuzzyValue.plotFuzzyValues("*+", 0, 50, fvalsR2)); 
	System.out.println(FuzzyValue.plotFuzzyValues("*", 0, 10, fvalsR2C)); 
	System.out.println(fvv2.fuzzyValueAt(0).plotFuzzyValue("*", 0, 10)); 
	
// defuzzify the output to get crisp values

    // calculate the deffuzified value
    double crispz = zFVal.weightedAverageDefuzzify();

    // let's look at the fuzzy set of the output and the crisp value

    FuzzySet.setToStringPrecision(6);
    System.out.println(zFVal);
    System.out.println("");
    System.out.println(zFVal.plotFuzzyValue("+"));
    System.out.println("\nDefuzzified z value is: " + crispz);
  }

}
