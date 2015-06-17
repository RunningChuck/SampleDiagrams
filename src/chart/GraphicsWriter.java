package chart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;


import java.util.Date;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.gvt.renderer.ImageRenderer;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.svg.SVGDocument;

import base.Settings;

public class GraphicsWriter {

	public enum SaveType {
		JPEG, SVG, JPEG_SVG, PNG, PNG_SVG
	};

	private SVGDocument document;
	private SVGGraphics2D svgGenerator;

	public GraphicsWriter() {
		// Get a DOMImplementation
		DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();
		String svgNamespaceURI = "http://www.w3.org/2000/svg";

		// Create an instance of org.w3c.dom.Document
		document = (SVGDocument) domImpl.createDocument(svgNamespaceURI, "svg", null);

		svgGenerator = new SVGGraphics2D(document);
	}

	public SVGGraphics2D getGraphics() {
		return svgGenerator;
	}

	public void writeOutput(String filename, Rectangle2D AOI, SaveType type) throws Exception {
		//set canvas size
		svgGenerator.setSVGCanvasSize(new Dimension((int)AOI.getWidth(), (int)AOI.getHeight()));
		
		// character to byte encoding
		boolean useCSS = true; // we want to use CSS style attribute

		Writer out = null;
		File folder = new File(filename.substring(0, filename.lastIndexOf("/")));
		if (!folder.exists()) {
			System.out.print(Settings.SIMPLEDATEFORMAT.format(new Date()) + ": " + "Try to create folder \'" + folder.getAbsolutePath() + "\': ");
			if (folder.mkdirs()) {
				System.out.println("	Succesful.");
			} else {
				System.out.println("	Bad.");
			}
		}
		
		File svgFile = new File(filename + ".svg");
		out = new OutputStreamWriter(new FileOutputStream(svgFile), "UTF-8");

		svgGenerator.stream(out, useCSS);

		out.close();

		if (type == SaveType.PNG || type == SaveType.PNG_SVG) {
			PNGTranscoder transcoder = new PNGTranscoder()
			{
			     @Override
			     protected ImageRenderer createRenderer()
			     {
			         ImageRenderer r = super.createRenderer();

			         RenderingHints rh = r.getRenderingHints();

			         rh.add(new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION,
			             RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY));
			         rh.add(new RenderingHints(RenderingHints.KEY_INTERPOLATION,
			             RenderingHints.VALUE_INTERPOLATION_BICUBIC));

			         rh.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
			             RenderingHints.VALUE_ANTIALIAS_ON));

			         rh.add(new RenderingHints(RenderingHints.KEY_COLOR_RENDERING,
			             RenderingHints.VALUE_COLOR_RENDER_QUALITY));
			         rh.add(new RenderingHints(RenderingHints.KEY_DITHERING,
			             RenderingHints.VALUE_DITHER_DISABLE));

			         rh.add(new RenderingHints(RenderingHints.KEY_RENDERING,
			             RenderingHints.VALUE_RENDER_QUALITY));

			         rh.add(new RenderingHints(RenderingHints.KEY_STROKE_CONTROL,
			             RenderingHints.VALUE_STROKE_PURE));

			         rh.add(new RenderingHints(RenderingHints.KEY_FRACTIONALMETRICS,
			             RenderingHints.VALUE_FRACTIONALMETRICS_ON));
			         rh.add(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
			             RenderingHints.VALUE_TEXT_ANTIALIAS_OFF));

			         r.setRenderingHints(rh);

			         return r;
			     }
			}; 

			transcoder.addTranscodingHint(
					JPEGTranscoder.KEY_XML_PARSER_CLASSNAME,
					"org.apache.crimson.parser.XMLReaderImpl");
			transcoder.addTranscodingHint(PNGTranscoder.KEY_BACKGROUND_COLOR, Color.white);
			TranscoderInput input = new TranscoderInput(new FileInputStream(
					svgFile));// new File(filename + "_exp.svg")));
			OutputStream ostream = new FileOutputStream(filename + ".png");
			TranscoderOutput output = new TranscoderOutput(ostream);

			transcoder.transcode(input, output);
			ostream.close();
			
			if (type == SaveType.PNG)
				svgFile.delete();
		}
		if (type == SaveType.JPEG || type == SaveType.JPEG_SVG) {
			JPEGTranscoder transcoder = new JPEGTranscoder();

			transcoder.addTranscodingHint(
					JPEGTranscoder.KEY_XML_PARSER_CLASSNAME,
					"org.apache.crimson.parser.XMLReaderImpl");
			transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,
					new Float(1.0));
			// transcoder.addTranscodingHint(JPEGTranscoder.KEY_HEIGHT,
			// (float)AOI.getHeight()+500);
			// transcoder.addTranscodingHint(JPEGTranscoder.KEY_WIDTH,
			// (float)AOI.getWidth());
			// transcoder.addTranscodingHint(JPEGTranscoder.KEY_AOI,
			// new Rectangle2D.Double(83.5, 48.4, 60, 90));
			TranscoderInput input = new TranscoderInput(new FileInputStream(
					svgFile));// new File(filename + "_exp.svg")));
			OutputStream ostream = new FileOutputStream(filename + ".jpg");
			TranscoderOutput output = new TranscoderOutput(ostream);

			transcoder.transcode(input, output);
			ostream.close();
		}
		if (type == SaveType.JPEG) {
			svgFile.delete();
		}
	}

	public void writeOutput(String filename) {
		File svgFile = new File(filename + ".svg");
		writeOutput(svgFile);
	}

	public void writeOutput(File svgFile) {
		// Finally, stream out SVG to the standard output using UTF-8
		// character to byte encoding
		boolean useCSS = true; // we want to use CSS style attribute

		Writer out = null;
		try {
			out = new OutputStreamWriter(new FileOutputStream(svgFile), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			((SVGGraphics2D) svgGenerator).stream(out, useCSS);
		} catch (SVGGraphics2DIOException e) {
			e.printStackTrace();
		}
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
