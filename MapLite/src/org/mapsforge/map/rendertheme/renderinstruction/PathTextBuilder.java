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
package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.Locale;

import org.mapsforge.map.rendertheme.XmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Typeface;

/**
 * A builder for {@link PathText} instances.
 */
public class PathTextBuilder {
	static final String FILL = "fill";
	static final String FONT_FAMILY = "font-family";
	static final String FONT_SIZE = "font-size";
	static final String FONT_STYLE = "font-style";
	static final String K = "k";
	static final String STROKE = "stroke";
	static final String STROKE_WIDTH = "stroke-width";

	final Paint fill;
	float fontSize;
	final Paint stroke;
	TextKey textKey;

	public PathTextBuilder(String elementName, Attributes attributes)
			throws SAXException {
		this.fill = new Paint();
        this.fill.setAntiAlias(true);
        this.fill.setStrokeCap(Cap.ROUND);
        this.fill.setStrokeJoin(Join.ROUND);
		this.fill.setColor(Color.BLACK);
		this.fill.setStyle(Style.FILL);
		this.fill.setTextAlign(Align.CENTER);

		this.stroke = new Paint();
        this.stroke.setAntiAlias(true);
        this.stroke.setStrokeCap(Cap.ROUND);
        this.stroke.setStrokeJoin(Join.ROUND);
		this.stroke.setColor(Color.BLACK);
		this.stroke.setStyle(Style.STROKE);
		this.stroke.setTextAlign(Align.CENTER);

		extractValues(elementName, attributes);
	}

	/**
	 * @return a new {@code PathText} instance.
	 */
	public PathText build() {
		return new PathText(this);
	}

	private void extractValues(String elementName, Attributes attributes)
			throws SAXException {
		Typeface fontFamily = Typeface.DEFAULT;
		int fontStyle = Typeface.NORMAL;

		for (int i = 0; i < attributes.getLength(); ++i) {
			String name = attributes.getQName(i);
			String value = attributes.getValue(i);

			if (K.equals(name)) {
				this.textKey = TextKey.getInstance(value);
			} else if (FONT_FAMILY.equals(name)) {
				fontFamily = XmlUtils.getfontFamily(value.toUpperCase(Locale.ENGLISH));
			} else if (FONT_STYLE.equals(name)) {
				fontStyle = XmlUtils.getfontStyle(value.toUpperCase(Locale.ENGLISH));
			} else if (FONT_SIZE.equals(name)) {
				this.fontSize = XmlUtils.parseNonNegativeFloat(name, value);
			} else if (FILL.equals(name)) {
				this.fill.setColor(XmlUtils.getColor(value));
			} else if (STROKE.equals(name)) {
				this.stroke.setColor(XmlUtils.getColor(value));
			} else if (STROKE_WIDTH.equals(name)) {
				this.stroke.setStrokeWidth(XmlUtils.parseNonNegativeFloat(name, value));
			} else {
				throw XmlUtils.createSAXException(elementName, name, value, i);
			}
		}

		this.fill.setTypeface(Typeface.create(fontFamily, fontStyle));
		this.stroke.setTypeface(Typeface.create(fontFamily, fontStyle));

		XmlUtils.checkMandatoryAttribute(elementName, K, this.textKey);
	}
}
