/*
 * Copyright 2010, 2011, 2012, 2013 mapsforge.org
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.mapsforge.map.rendertheme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.SAXException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;

public final class XmlUtils {
	private static final String PREFIX_FILE = "file:";
	private static final String PREFIX_JAR = "jar:";
	private static final String UNSUPPORTED_COLOR_FORMAT = "unsupported color format: ";

	public static void checkMandatoryAttribute(String elementName, String attributeName, Object attributeValue)
			throws SAXException {
		if (attributeValue == null) {
			throw new SAXException("missing attribute '" + attributeName + "' for element: " + elementName);
		}
	}

	public static Bitmap createBitmap(String relativePathPrefix, String src)
			throws IOException {
		if (src == null || src.length() == 0) {
			// no image source defined
			return null;
		}

		InputStream inputStream = createInputStream(relativePathPrefix, src);
		try {
			return BitmapFactory.decodeStream(inputStream);
		} finally {
			inputStream.close();
		}
	}

	public static SAXException createSAXException(String element, String name, String value, int attributeIndex) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("unknown attribute (");
		stringBuilder.append(attributeIndex);
		stringBuilder.append(") in element '");
		stringBuilder.append(element);
		stringBuilder.append("': ");
		stringBuilder.append(name);
		stringBuilder.append('=');
		stringBuilder.append(value);

		return new SAXException(stringBuilder.toString());
	}

	/**
	 * Supported formats are {@code #RRGGBB} and {@code #AARRGGBB}.
	 */
	public static int getColor(String colorString) {
		if (colorString.isEmpty() || colorString.charAt(0) != '#') {
			throw new IllegalArgumentException(UNSUPPORTED_COLOR_FORMAT + colorString);
		} else if (colorString.length() == 7) {
			return getColor(colorString, 255, 1);
		} else if (colorString.length() == 9) {
			return getColor(colorString, Integer.parseInt(colorString.substring(1, 3), 16), 3);
		} else {
			throw new IllegalArgumentException(UNSUPPORTED_COLOR_FORMAT + colorString);
		}
	}

	public static Typeface getfontFamily(String fontFamilyString) {
		if(fontFamilyString.compareToIgnoreCase("DEFAULT") == 0){
			return Typeface.DEFAULT;
		}else if(fontFamilyString.compareToIgnoreCase("MONOSPACE") == 0){
			return Typeface.MONOSPACE;
		}else if(fontFamilyString.compareToIgnoreCase("SANS_SERIF") == 0){
			return Typeface.SANS_SERIF;
		}else if(fontFamilyString.compareToIgnoreCase("SERIF") == 0){
			return Typeface.SERIF;
		}else{
			throw new IllegalArgumentException("unsupported font family: " + fontFamilyString); 
		}
	}
	
	public static int getfontStyle(String fontStyleString) {
		if(fontStyleString.compareToIgnoreCase("BOLD") == 0){
			return Typeface.BOLD;
		}else if(fontStyleString.compareToIgnoreCase("BOLD_ITALIC") == 0){
			return Typeface.BOLD_ITALIC;
		}else if(fontStyleString.compareToIgnoreCase("ITALIC") == 0){
			return Typeface.ITALIC;
		}else if(fontStyleString.compareToIgnoreCase("NORMAL") == 0){
			return Typeface.NORMAL;
		}else{
			throw new IllegalArgumentException("unsupported font style: " + fontStyleString); 
		}
	}
	
	public static byte parseNonNegativeByte(String name, String value) throws SAXException {
		byte parsedByte = Byte.parseByte(value);
		checkForNegativeValue(name, parsedByte);
		return parsedByte;
	}

	public static float parseNonNegativeFloat(String name, String value) throws SAXException {
		float parsedFloat = Float.parseFloat(value);
		checkForNegativeValue(name, parsedFloat);
		return parsedFloat;
	}

	public static int parseNonNegativeInteger(String name, String value) throws SAXException {
		int parsedInt = Integer.parseInt(value);
		checkForNegativeValue(name, parsedInt);
		return parsedInt;
	}

	private static void checkForNegativeValue(String name, float value) throws SAXException {
		if (value < 0) {
			throw new SAXException("Attribute '" + name + "' must not be negative: " + value);
		}
	}

	private static InputStream createInputStream(String relativePathPrefix, String src) throws FileNotFoundException {
		if (src.startsWith(PREFIX_JAR)) {
			String absoluteName = getAbsoluteName(relativePathPrefix, src.substring(PREFIX_JAR.length()));
			InputStream inputStream = XmlUtils.class.getResourceAsStream(absoluteName);
			if (inputStream == null) {
				throw new FileNotFoundException("resource not found: " + absoluteName);
			}
			return inputStream;
		} else if (src.startsWith(PREFIX_FILE)) {
			File file = getFile(relativePathPrefix, src.substring(PREFIX_FILE.length()));
			if (!file.exists()) {
				throw new FileNotFoundException("file does not exist: " + file.getAbsolutePath());
			} else if (!file.isFile()) {
				throw new FileNotFoundException("not a file: " + file.getAbsolutePath());
			} else if (!file.canRead()) {
				throw new FileNotFoundException("cannot read file: " + file.getAbsolutePath());
			}
			return new FileInputStream(file);
		}

		throw new FileNotFoundException("invalid bitmap source: " + src);
	}

	private static String getAbsoluteName(String relativePathPrefix, String name) {
		if (name.charAt(0) == '/') {
			return name;
		}
		return relativePathPrefix + name;
	}

	private static int getColor(String colorString, int alpha, int rgbStartIndex) {
		int red = Integer.parseInt(colorString.substring(rgbStartIndex, rgbStartIndex + 2), 16);
		int green = Integer.parseInt(colorString.substring(rgbStartIndex + 2, rgbStartIndex + 4), 16);
		int blue = Integer.parseInt(colorString.substring(rgbStartIndex + 4, rgbStartIndex + 6), 16);

		return Color.argb(alpha, red, green, blue);
	}

	private static File getFile(String parentPath, String pathName) {
		if (pathName.charAt(0) == File.separatorChar) {
			return new File(pathName);
		}
		return new File(parentPath, pathName);
	}

	private XmlUtils() {
		throw new IllegalStateException();
	}
}
