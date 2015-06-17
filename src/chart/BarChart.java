package chart;

import java.awt.Color;

import base.SettingsColor;
import chart.BarContainer.Orientation;
import data.Scale;
import data.DataChunkContainer;

/**
 * Manages BarCharts. BarCharts are implemented as relative (percentage) standing columns.
 * Other charts with bars are AbsoluteBarCharts, MultiBarCharts, HoriBarCharts
 * @author clemens
 *
 */
public class BarChart extends Chart{
	private BarContainer[] container;
	
	public BarChart(String name, Scale scale) {
		super(name, scale);
	}
	
	private void calculate() {
		Color[] colors = SettingsColor.getBarColors();
		//Use BarContainer --> encapsulates visually depending data and 
		//supports charting of bars, which later simplifies things
		container = new BarContainer[data[0].length];
		for (int i = 0; i < data[0].length; i++) {
			container[i] = new BarContainer(Orientation.BOTTOM);
			for (int j = 0; j < scale.size(); j++) {
				Color tempColor = colors[j];
				container[i].add(new BarPart(data[0][i].getCounts(j), tempColor));
			}
		}
	}

	
	public void setData(DataChunkContainer[][] data) {
		super.setData(data);
		calculate();
	}
	
	public BarContainer[] getBars() {
		return container;
	}
	
	public String[] getBarTitles() {
		return dataGroupTitles;
	}

	public String[] getLegendTitles() {
		return dataVarTitles;
	}

	public ChartPainter getPainter() {
		return new BarChartPainter(this, new GraphicsWriter());	
	}
}
