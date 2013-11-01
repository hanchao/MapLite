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

import java.io.IOException;

import org.mapsforge.map.rendertheme.XmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;

/**
 * A builder for {@link Area} instances.
 */
public class AreaBuilder {
	static final String FILL = "fill";
	static final String SRC = "src";
	static final String STROKE = "stroke";
	static final String STROKE_WIDTH = "stroke-width";

	final Paint fill;
	final int level;
	final Paint stroke;
	float strokeWidth;

	public AreaBuilder(String elementName, Attributes attributes, int level,
			String relativePathPrefix) throws IOException, SAXException {
		this.level = level;

		this.fill = new Paint();
        this.fill.setAntiAlias(true);
        this.fill.setStrokeJoin(Join.ROUND);
		this.fill.setColor(Color.BLACK);
		this.fill.setStyle(Style.FILL);
		this.fill.setStrokeCap(Cap.ROUND);
		

		this.stroke = new Paint();
        this.stroke.setAntiAlias(true);
        this.stroke.setStrokeJoin(Join.ROUND);
		this.stroke.setColor(Color.TRANSPARENT);
		this.stroke.setStyle(Style.STROKE);
		this.stroke.setStrokeCap(Cap.ROUND);

		extractValues(elementName, attributes, relativePathPrefix);
	}

	/**
	 * @return a new {@code Area} instance.
	 */
	public Area build() {
		return new Area(this);
	}

	private void extractValues(String elementName, Attributes attributes,
			String relativePathPrefix) throws IOException, SAXException {
		for (int i = 0; i < attributes.getLength(); ++i) {
			String name = attributes.getQName(i);
			String value = attributes.getValue(i);

			if (SRC.equals(name)) {
				BitmapShader bitmapShader = new BitmapShader(XmlUtils.createBitmap(relativePathPrefix, value),
						TileMode.REPEAT, TileMode.REPEAT);
				this.fill.setShader(bitmapShader);
			} else if (FILL.equals(name)) {
				this.fill.setColor(XmlUtils.getColor(value));
			} else if (STROKE.equals(name)) {
				this.stroke.setColor(XmlUtils.getColor(value));
			} else if (STROKE_WIDTH.equals(name)) {
				this.strokeWidth = XmlUtils.parseNonNegativeFloat(name, value);
			} else {
				throw XmlUtils.createSAXException(elementName, name, value, i);
			}
		}
	}
}
