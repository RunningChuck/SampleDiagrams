package chart;

import data.Scale;
import data.DataChunkContainer;

/**
 * Manages LineCharts
 * @author clemens
 *
 */
public class LineChart extends Chart{
	public LineChart(String name, Scale scale) {
		super(name, scale);
	}
	//([y][z])
	private double[][] valuesAvg;
	

	public String[] getYValueTitles() {
		return dataVarTitles;
	}

	public String[] getZValueTitles() {
		return dataGroupTitles;
	}

	public double[][] getValuesAvg() {
		return valuesAvg;
	}
	
	private void calculate() {
		valuesAvg = new double[data.length][data[0].length];
		for (int item = 0; item < data.length; item++) {
			for (int company = 0; company < data[0].length; company++) {
				valuesAvg[item][company] = data[item][company].getAvg();
			}
		}
	}
	
	public void setData(DataChunkContainer[][] data) {
		super.setData(data);
		calculate();
	}
	
	public ChartPainter getPainter() {
		return new LineChartPainter(this, new GraphicsWriter());	
	}
}