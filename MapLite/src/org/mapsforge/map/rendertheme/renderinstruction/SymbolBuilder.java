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

import android.graphics.Bitmap;

/**
 * A builder for {@link Symbol} instances.
 */
public class SymbolBuilder {
	static final String SRC = "src";

	Bitmap bitmap;

	public SymbolBuilder(String elementName, Attributes attributes,
			String relativePathPrefix) throws IOException, SAXException {
		extractValues(elementName, attributes, relativePathPrefix);
	}

	/**
	 * @return a new {@code Symbol} instance.
	 */
	public Symbol build() {
		return new Symbol(this);
	}

	private void extractValues(String elementName, Attributes attributes,
			String relativePathPrefix) throws IOException, SAXException {
		for (int i = 0; i < attributes.getLength(); ++i) {
			String name = attributes.getQName(i);
			String value = attributes.getValue(i);

			if (SRC.equals(name)) {
				this.bitmap = XmlUtils.createBitmap(relativePathPrefix, value);
			} else {
				throw XmlUtils.createSAXException(elementName, name, value, i);
			}
		}

		XmlUtils.checkMandatoryAttribute(elementName, SRC, this.bitmap);
	}
}
