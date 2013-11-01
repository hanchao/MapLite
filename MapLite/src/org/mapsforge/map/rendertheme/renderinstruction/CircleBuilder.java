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

import org.mapsforge.map.rendertheme.XmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;

/**
 * A builder for {@link Circle} instances.
 */
public class CircleBuilder {
	static final String FILL = "fill";
	static final String RADIUS = "radius";
	static final String SCALE_RADIUS = "scale-radius";
	static final String STROKE = "stroke";
	static final String STROKE_WIDTH = "stroke-width";

	final Paint fill;
	final int level;
	Float radius;
	boolean scaleRadius;
	final Paint stroke;
	float strokeWidth;

	public CircleBuilder(String elementName, Attributes attributes, int level)
			throws SAXException {
		this.level = level;

		this.fill = new Paint();
        this.fill.setAntiAlias(true);
        this.fill.setStrokeCap(Cap.ROUND);
        this.fill.setStrokeJoin(Join.ROUND);
		this.fill.setColor(Color.TRANSPARENT);
		this.fill.setStyle(Style.FILL);

		this.stroke = new Paint();
        this.stroke.setAntiAlias(true);
        this.stroke.setStrokeCap(Cap.ROUND);
        this.stroke.setStrokeJoin(Join.ROUND);
		this.stroke.setColor(Color.TRANSPARENT);
		this.stroke.setStyle(Style.STROKE);

		extractValues(elementName, attributes);
	}

	/**
	 * @return a new {@code Circle} instance.
	 */
	public Circle build() {
		return new Circle(this);
	}

	private void extractValues(String elementName, Attributes attributes)
			throws SAXException {
		for (int i = 0; i < attributes.getLength(); ++i) {
			String name = attributes.getQName(i);
			String value = attributes.getValue(i);

			if (RADIUS.equals(name)) {
				this.radius = Float.valueOf(XmlUtils.parseNonNegativeFloat(name, value));
			} else if (SCALE_RADIUS.equals(name)) {
				this.scaleRadius = Boolean.parseBoolean(value);
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

		XmlUtils.checkMandatoryAttribute(elementName, RADIUS, this.radius);
	}
}
