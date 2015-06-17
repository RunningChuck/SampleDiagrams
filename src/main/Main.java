package main;

import java.io.IOException;
import java.util.ArrayList;

import chart.BarChart;
import chart.Chart;
import chart.LineChart;
import data.DataCollector;
import data.DataCollectorException;
import data.Scale;
import data.DataChunk;
import data.DataChunkContainer;
import data.DataChunkException;

/**
 * Demonstration of charting tool (heavily stripped down version)
 * 
 * Tested in Windows 8.1, Eclipse Luna Service Release 2 (4.4.2), JRE 1.8.0
 * 
 * This tool was designed over two large scale employee questionnaires, and aimed at easily
 * 		translating raw data into charts, that could were later inserted into office documents by VBA.
 * 
 * In full scope, many more features are included (Numerous chart types; date comparison,
 *		e.g. follow-up studies; SQLite database; complex data, e.g. different scales within
 * 		chart; management of data labeling; threading; etc.)
 * 
 * Anonymous data in folder 'res' is taken from reference project
 *		--> Employee questionnaire with > 150 variables and > 7500 data rows
 *
 * Referenced libraries are in folder 'lib', and may need to be included into build path.
 * 		batik-1.7/batik.jar
 * 		crimson-1.1.3/crimson.jar
 * 		opencsv-3.0.jar
 * 
 * @author clemens binder
 *
 */
public class Main {
	
	public static void main(String[] args) {
		
		//Titles normally read from setting files --> label management not included!
		String[] akzeDataTitles = {"Akzeptanz Item 1", "Akzeptanz Item 2", "Akzeptanz Item 3", "Akzeptanz global"};
		String[] nutzDataTitles = {"Nutzen Item 1", "Nutzen Item 2", "Nutzen Item 3", "Nutzen global"};
		String[] alterDataTitles = {"zwischen 18 und 25", "zwischen 26 und 35", "zwischen 36 und 45",
				"zwischen 46 und 55", "zwischen 56 und 65"};	
		String[] groupTitles = {"Vertrieb", "Controlling", "Client Support"};
		
		// ################## READ AND SORT DATA FROM FILE #####################
		
		
		//DataCollector manages csv files, that include a header with variable names, and subsequent data rows.
		DataCollector csvDataCollector = null;
		try {
			//load data (normally read to SQLite)
			csvDataCollector = new DataCollector("res/data.csv");
			
		} catch (DataCollectorException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		//create simple scale definition (a.k.a what means "employee ticked a 5"?)
		Scale sevenLikert = new Scale("7-point Likert scale", 1, 7);

		ArrayList<String> tempColumn;
		
		//create data containers (->chunks)
		//DataChunks support translating raw data into parsed and counted singular values
		DataChunk[][] akzeValues = new DataChunk[3][3];
		DataChunk[][] nutzValues = new DataChunk[3][3];
		DataChunk[] alterValues = new DataChunk[3];
		
		//create chunk containers (+1 item for composite measure)
		//DataChunkContainers encapsulate DataChunks, and support further calculation on data (average,
		//standard deviation, composite measures)
		DataChunkContainer[][] akzeContainer = new DataChunkContainer[4][3];
		DataChunkContainer[][] nutzContainer = new DataChunkContainer[4][3];
		DataChunkContainer[][] alterContainer = new DataChunkContainer[1][3];
		

		//read relevant data
		for (int group = 0; group < 3; group++) {
			try {
				for (int item = 0; item < 3; item++) {
					//retrieve all akze_ variables
					tempColumn = csvDataCollector.getColumn("akze_"+(item+1), "group", (group+1));
					akzeValues[item][group] = new DataChunk("akze_"+(item+1)+"/"+(group+1), sevenLikert, tempColumn);
					
					//retrieve all nutz_ variables
					tempColumn = csvDataCollector.getColumn("nutz_"+(item+1), "group", (group+1));
					nutzValues[item][group] = new DataChunk("akze_"+(item+1)+"/"+(group+1), sevenLikert, tempColumn);
										
					//contain them in containers
					akzeContainer[item][group] = akzeValues[item][group].toContainer();
					nutzContainer[item][group] = nutzValues[item][group].toContainer();
					
				}
				
				//retrieve alter
				tempColumn = csvDataCollector.getColumn("alter", "group", (group+1));
				alterValues[group] = new DataChunk("alter/"+(group+1), sevenLikert, tempColumn);

				alterContainer[0][group] = alterValues[group].toContainer();
				
			} catch (DataCollectorException e) {
				e.printStackTrace();
			}
		}
		
		//create additional composite chunk containers --> composite variable (item 1 to 3 averaged)
		for (int group = 0; group < 3; group++) {
			//add first variable
			akzeContainer[3][group] = akzeValues[0][group].toContainer();
			nutzContainer[3][group] = nutzValues[0][group].toContainer();
			
			//add 2nd and 3rd
			for (int item = 1; item < 3; item++) {
				try {
					akzeContainer[3][group].add(akzeValues[item][group].toContainer());
					nutzContainer[3][group].add(akzeValues[item][group].toContainer());
				} catch (DataChunkException e) {
					//is raised when heterogeneous scales are used in one composite variable
					//cannot cause trouble here, as in this case only one scale has been defined
					//	--> chunks can be added
					e.printStackTrace();
				}
			}
		}
		
		// ################## PASS DATA TO CHARTING CLASSES #####################
		
		Chart chartAkze = new LineChart("LineChart Akzeptanz", sevenLikert);
		
		chartAkze.setData(akzeContainer);
		chartAkze.setDataVarTitles(akzeDataTitles);
		chartAkze.setDataGroupTitles(groupTitles);
		
		//charting --> path set in Settings
		chartAkze.saveFile();
		
		
		Chart chartNutz = new LineChart("LineChart Nutzen", sevenLikert);
		
		chartNutz.setData(nutzContainer);
		chartNutz.setDataVarTitles(nutzDataTitles);
		chartNutz.setDataGroupTitles(groupTitles);
		
		//charting
		chartNutz.saveFile();
		
		Chart chartAlter = new BarChart("BarChart Alter", sevenLikert);
		
		chartAlter.setData(alterContainer);
		chartAlter.setDataVarTitles(alterDataTitles);
		chartAlter.setDataGroupTitles(groupTitles);
		
		//charting
		chartAlter.saveFile();
		
		// ################# CHARTS CREATED ###############
	}
}
