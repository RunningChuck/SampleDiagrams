package chart;

import java.util.Date;

import base.Settings;
import data.Scale;
import data.DataChunkContainer;

/**
 * Abstract class for charts. Provides all relevant methods.
 * @author clemens
 *
 */
public abstract class Chart {
	protected DataChunkContainer[][] data;
	protected Scale scale;
	protected String chartName;
	
	
	//titles for different subsets of data (e.g. different teams in company)
	protected String[] dataGroupTitles;
	//titles for different variables in data (e.g. questions in questionnaire)
	protected String[] dataVarTitles;

	public Chart(String name, Scale scale) {
		this.chartName = name;
		this.scale = scale;
	}
	
	/**
	 * set titles for different subsets of data (e.g. different teams in company)
	 * @param dataGroupTitles
	 */
	public void setDataGroupTitles(String[] dataGroupTitles) {
		this.dataGroupTitles = dataGroupTitles;
	}
	
	/**
	 * set titles for different variables in data (e.g. questions in questionnaire)
	 * @param dataVarTitles
	 */
	public void setDataVarTitles(String[] dataVarTitles) {
		this.dataVarTitles = dataVarTitles;
	}
	
	public Scale getScale() {
		return scale;
	}
	
	/**
	 * Render chart. Path and file type specified in setting file.
	 */
	public void saveFile() {
		ChartPainter painter = this.getPainter();
		String path = Settings.PATH_CHARTS + chartName;
		try {
			painter.getWriter().writeOutput(path, painter.getAOI(), Settings.OUTPUT_FILE_FORMAT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(Settings.SIMPLEDATEFORMAT.format(new Date()) + ": " + "*** Rendered \'" + path + ".(svg|png|jpeg)\'");
	}

	protected abstract ChartPainter getPainter();

	/**
	 * Sets a container array of data.
	 * @param data two-dimensional data. convention is: [item number][data group]
	 */
	public void setData(DataChunkContainer[][] data) {
		this.data = data;
	}
}