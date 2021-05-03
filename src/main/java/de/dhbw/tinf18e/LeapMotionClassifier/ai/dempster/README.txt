The files in this folder can be used for calculation using the Dempster-Shafer-Method.

How to use it:
1. Create a DempsterHandler object by initialising it with the size of your realm of alternatives
2. Call the addMeasure-method on the DempsterHandler to create a new measure. Save the reference to the measure
3. Call the addEntry-method on the measure-object to add a single entry. Supply the values of the entry and its propability.
4. The values of the entry should be a List of 1s and 0s, with the 1s representing a hit. 
   For example if your realm of possibilitys is {A, B, C} you can represent the set of {B} with a list from the array {0,1,0}
5. After all measures and their values have been added, you cann accumulate all measures for the calculation.
   This will result in 1 remaining measure, which you can access by calling getFirstMeasure() on the DempsterHandler-Object.
6. You can use this Measure-Object to calculate belif, plausibility and doubt for all entrys in the measure by calling the according methods
7. Careful: there are very few safety-measures that stop you from making mistakes in this code. If you submit non-fitting lists or otheres
   you will run into exceptions. So check your input!

An example:
//test DempsterHandler
DempsterHandler dempsterHandlerTest = new DempsterHandler(6);
Measure m1 = dempsterHandlerTest.addMeasure();
m1.addEntry(Arrays.asList(new Integer[] {1 ,1, 1, 0, 0, 0}) , 0.88f);
System.out.println("First measure: " + m1.toString());
		
Measure m2 = dempsterHandlerTest.addMeasure();
m2.addEntry(Arrays.asList(new Integer[] {1 ,0, 1, 0, 0, 1}) , 0.45f);
m2.addEntry(Arrays.asList(new Integer[] {0 ,1, 0, 1, 0, 0}) , 0.45f);
		
dempsterHandlerTest.accumulateAllMeasures();
System.out.println("Accumulated measures result in: " + dempsterHandlerTest.getFirstMeasure().toString());
				
Measure m3 = dempsterHandlerTest.addMeasure();
m3.addEntry(Arrays.asList(new Integer[] {0 ,1, 0, 1, 1, 0}) , 0.65f);
	
dempsterHandlerTest.accumulateAllMeasures();
System.out.println("Accumulated measures result in: " + dempsterHandlerTest.getFirstMeasure().toString());

double belief = dempsterHandlerTest.getFirstMeasure().calculateBelief(1);
double plausability = dempsterHandlerTest.getFirstMeasure().calculatePlausability(1);
double doubt = dempsterHandlerTest.getFirstMeasure().calculateDoubt(1);

System.out.println("Belief: \t" + belief +"\nPlausability: \t" + plausability + "\n Doubt: \t" + doubt);