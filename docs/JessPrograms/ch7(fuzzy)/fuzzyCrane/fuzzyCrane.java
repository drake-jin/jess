

import nrc.fuzzy.*;

public class fuzzyCrane
{

  public fuzzyCrane()
  {
  }

  public static void main(String[] argv) throws FuzzyException
  {
// Step 1 define the FuzzyVariables 
	FuzzyVariable Angle = new FuzzyVariable("Angle", -90, 90, "degree"); 
	Angle.addTerm("neg_big", new ZFuzzySet(-60.0, -10.0)); 
	Angle.addTerm("neg_small", new TriangleFuzzySet(-60.0, -10.0, 0));
	Angle.addTerm("zero", new TriangleFuzzySet(-10.0, 0, 10.0));
	Angle.addTerm("pos_small", new TriangleFuzzySet(0, 10.0, 60.0));
	Angle.addTerm("pos_big", new SFuzzySet(10.0, 60.0)); 
	
	FuzzyVariable Distance = new FuzzyVariable("Distance", -10, 30, "m"); 
	Distance.addTerm("neg_close", new ZFuzzySet(-5.0, 0.0)); 
	Distance.addTerm("zero", new TriangleFuzzySet(-5.0, 0.16, 3.25));
	Distance.addTerm("close", new TriangleFuzzySet(0.0, 3.25, 10.0));
	Distance.addTerm("medium", new TriangleFuzzySet(3.25, 10.0,22.5));
	Distance.addTerm("far", new SFuzzySet(10.0,22.5)); 
	
	FuzzyVariable Power = new FuzzyVariable("Power", -30, 30, "kw"); 
	Power.addTerm("neg_high", new TriangleFuzzySet(-30.0, -24, -7)); 
	Power.addTerm("neg_medium", new TriangleFuzzySet(-24, -7, 0.3));
	Power.addTerm("zero", new TriangleFuzzySet(-7, 0.0, 10.0));
	Power.addTerm("pos_medium", new TriangleFuzzySet(0.0, 10.0,23.6));
	Power.addTerm("pos_high", new TriangleFuzzySet(10.0,23.6,30)); 	

//	 provide the fuzzified inputs for the rules
    double inputAngle = -28;
    // create fuzzy values from the crisp values
    FuzzyValue inputAngleFval =  new FuzzyValue(Angle, new TriangleFuzzySet(inputAngle-0.05, 
    		inputAngle, inputAngle+0.05));
    
    double inputDistance = 5.99;
    // create fuzzy values from the crisp values
    FuzzyValue inputDistanceFval =  new FuzzyValue(Distance, new TriangleFuzzySet(inputDistance-0.05, 
    		inputDistance, inputDistance+0.05));
    
//	 let's build a rule --- 
	FuzzyRule rule5 = new FuzzyRule(); 
	FuzzyValue antecedentFvalR5Angle = new FuzzyValue(Angle, "neg_small"); 
	rule5.addAntecedent(antecedentFvalR5Angle);
	FuzzyValue antecedentFvalR5Dist = new FuzzyValue(Distance, "close"); 
	rule5.addAntecedent(antecedentFvalR5Dist);
	FuzzyValue conclusionFvalR5Power = new FuzzyValue(Power,"pos_medium");
	rule5.addConclusion(conclusionFvalR5Power);
	
	FuzzyRule rule6 = new FuzzyRule(); 
	FuzzyValue antecedentFvalR6Angle = new FuzzyValue(Angle, "neg_small"); 
	rule6.addAntecedent(antecedentFvalR6Angle);
	FuzzyValue antecedentFvalR6Dist = new FuzzyValue(Distance, "medium"); 	
	rule6.addAntecedent(antecedentFvalR6Dist);
	FuzzyValue conclusionFvalR6Power = new FuzzyValue(Power,"pos_high");
	rule6.addConclusion(conclusionFvalR6Power);
	
	FuzzyRule rule7 = new FuzzyRule(); 
	FuzzyValue antecedentFvalR7Angle = new FuzzyValue(Angle, "neg_big"); 
	rule7.addAntecedent(antecedentFvalR7Angle);
	FuzzyValue antecedentFvalR7Dist = new FuzzyValue(Distance, "medium"); 
	rule7.addAntecedent(antecedentFvalR7Dist);
	FuzzyValue conclusionFvalR7Power = new FuzzyValue(Power,"pos_medium");
	rule7.addConclusion(conclusionFvalR7Power);
	
	rule5.addInput(inputAngleFval);
	rule5.addInput(inputDistanceFval);		
	rule6.addInput(inputAngleFval);
	rule6.addInput(inputDistanceFval);	
	rule7.addInput(inputAngleFval);
	rule7.addInput(inputDistanceFval);	
	

//	 execute this simple rule with a single antecedent and 
//	 a single consequent using default rule executor -- 
//	 MamdaniMinMaxMinRuleExecutor 
	FuzzyValueVector fvv5 = rule5.execute(); 
	FuzzyValueVector fvv6 = rule6.execute(); 
	FuzzyValueVector fvv7 = rule6.execute();
	
    // aggregate the results from the 2 rules
    FuzzyValue zFVal = fvv5.fuzzyValueAt(0).fuzzyUnion(fvv6.fuzzyValueAt(0));
    		   zFVal = fvv5.fuzzyValueAt(0).fuzzyUnion(fvv7.fuzzyValueAt(0));      
    		   
//	 R5 전건부인 Angle, Distance와 후건부 Power 추론값 
	FuzzyValue fvalAngle5[] = new FuzzyValue[2]; 
	fvalAngle5[0] = antecedentFvalR5Angle; // R5 전건부 Angle FuzzySet
	fvalAngle5[1] = inputAngleFval;  // input for R5 Angle FuzzySet
	System.out.println(FuzzyValue.plotFuzzyValues("*+", -90, 90, fvalAngle5)); 
	
	FuzzyValue fvalDist5[] = new FuzzyValue[2]; 
	fvalDist5[0] = antecedentFvalR5Dist; // R5 전건부 Distance FuzzySet
	fvalDist5[1] = inputDistanceFval;  // input for R5 Distance FuzzySet
	FuzzyValue fvalPower5[] = new FuzzyValue[1]; 
	fvalPower5[0] = conclusionFvalR5Power; // R5 후건부 Power FuzzySet	
	System.out.println(FuzzyValue.plotFuzzyValues("*+", -30,30,fvalDist5)); 
	
	System.out.println(fvv5.fuzzyValueAt(0).plotFuzzyValue("*", -30,30)); 

//	 R6 전건부인 Angle, Distance와 후건부 Power 추론값 
	FuzzyValue fvalAngle6[] = new FuzzyValue[2]; 
	fvalAngle6[0] = antecedentFvalR6Angle; // R6 전건부 Angle FuzzySet
	fvalAngle6[1] = inputAngleFval;  // input for R5 Angle FuzzySet
	System.out.println(FuzzyValue.plotFuzzyValues("*+", -90, 90, fvalAngle6)); 
	
	FuzzyValue fvalDist6[] = new FuzzyValue[2]; 
	fvalDist6[0] = antecedentFvalR6Dist; // R6 전건부 Distance FuzzySet
	fvalDist6[1] = inputDistanceFval;  // input for R6 Distance FuzzySet
	FuzzyValue fvalPower6[] = new FuzzyValue[1]; 
	fvalPower6[0] = conclusionFvalR6Power; // R6 후건부 Power FuzzySet	
	System.out.println(FuzzyValue.plotFuzzyValues("*+", -30,30,fvalDist6)); 
	
	System.out.println(fvv6.fuzzyValueAt(0).plotFuzzyValue("*", -30,30)); 
	
//	 R7 전건부인 Angle, Distance와 후건부 Power 추론값 
	FuzzyValue fvalAngle7[] = new FuzzyValue[2]; 
	fvalAngle7[0] = antecedentFvalR7Angle; // R7 전건부 Angle FuzzySet
	fvalAngle7[1] = inputAngleFval;  // input for R7 Angle FuzzySet
	System.out.println(FuzzyValue.plotFuzzyValues("*+", -90, 90, fvalAngle7)); 
	
	FuzzyValue fvalDist7[] = new FuzzyValue[2]; 
	fvalDist7[0] = antecedentFvalR7Dist; // R6 전건부 Distance FuzzySet
	fvalDist7[1] = inputDistanceFval;  // input for R6 Distance FuzzySet
	FuzzyValue fvalPower7[] = new FuzzyValue[1]; 
	fvalPower7[0] = conclusionFvalR7Power; // R7 후건부 Power FuzzySet	
	System.out.println(FuzzyValue.plotFuzzyValues("*+", -30,30,fvalDist7)); 
	
	System.out.println(fvv6.fuzzyValueAt(0).plotFuzzyValue("*", -30,30));
	
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
