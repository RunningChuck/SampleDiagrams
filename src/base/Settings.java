package base;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

import chart.GraphicsWriter;

public class Settings {

	public static String TABLE = "ewi14_cc";
//	public final static String TABLE = "ewi14_panels";
//	public final static String TABLE = "ewi14_buebefr";
//	public final static String TABLE = "ewi14_stakeholder";
	public static String TABLE_COMPARE = "ewi13_01";
	public final static String TABLE_WEIGHTED = "ewi14_gewichtet";
	
	/** SET TRUE FOR STAKEHOLDER AND SMALL SAMPLES! **/
	public static final boolean OMIT_ST_DEVIATION = false;
//	public static final boolean OMIT_ST_DEVIATION = true;
	
	public final static String PATH = "res/";
	public final static String PATH_DATA = PATH + "data/";
	public final static String PATH_DATADEFS = PATH + "datadefs/";
	public static final String PATH_DATADEFS_STAKEHOLDERKEY = PATH_DATADEFS + "gemeindekey.csv";
	public static final String PATH_DATADEFS_REPORTS = PATH_DATADEFS + "berichte.csv";
	public static final String PATH_AGGREGATE = PATH_DATADEFS + "aggregate.csv";
	public final static String PATH_CHARTS = PATH + "charts/";
	public final static String PATH_CHART_DEFS = PATH + "chartdefs/";
	public final static String PATH_RECODE_DEFS = PATH + "recodedefs/";
	
	public final static GraphicsWriter.SaveType OUTPUT_FILE_FORMAT = GraphicsWriter.SaveType.PNG_SVG;
//	public final static GraphicsWriter.SaveType OUTPUT_FILE_FORMAT = GraphicsWriter.SaveType.SVG;
//	public final static GraphicsWriter.SaveType OUTPUT_FILE_FORMAT = GraphicsWriter.SaveType.JPEG;

	public final static String[] COMPARE = {"2014", "2013"};

	public static String[] WEIGHTED_STAKEHOLDERS = {"Deutschland", "Bayern", "Rheinland-Pfalz"};
//	public static String[] WEIGHTED_STAKEHOLDERS = {"Deutschland", "Baden-Württemberg", "Bayern", "Rheinland-Pfalz"};
//	public static String[] WEIGHTED_STAKEHOLDERS = {};
	public static String WEIGHTED_STAKEHOLDER_VAR = "bundeslandString";
	
	public final static int DECIMALS = 2;
	public final static int MISSINGVALUE = -77;
	public final static char DECIMAL_SEPARATOR = ',';
	
	//Sets the minimum percentage needed for a bar part to have value prints on it.
	public final static double MINIMUMTOPRINT = 0.02;
	
	//Used by TextContainer
	public final static double LINEHEIGHT = 1.1;
	
	public final static char CSV_DELIMITER = ';';
	public final static Charset CSV_CHARSET = Charset.forName("ISO-8859-1");
	
	public final static String NO_DATA = "";
	public static final String NSYMBOL = "n=";
	public static final String[] YTITLES_MUTLI_BARS = {"Nein", "Ja"};

	public static SimpleDateFormat SIMPLEDATEFORMAT = new SimpleDateFormat("yy-MM-dd HH:mm:ss.S");
	public static boolean report = true;
}
