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
package org.mapsforge.map.rendertheme.rule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.mapsforge.core.util.IOUtils;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.renderinstruction.Area;
import org.mapsforge.map.rendertheme.renderinstruction.AreaBuilder;
import org.mapsforge.map.rendertheme.renderinstruction.Caption;
import org.mapsforge.map.rendertheme.renderinstruction.CaptionBuilder;
import org.mapsforge.map.rendertheme.renderinstruction.Circle;
import org.mapsforge.map.rendertheme.renderinstruction.CircleBuilder;
import org.mapsforge.map.rendertheme.renderinstruction.Line;
import org.mapsforge.map.rendertheme.renderinstruction.LineBuilder;
import org.mapsforge.map.rendertheme.renderinstruction.LineSymbol;
import org.mapsforge.map.rendertheme.renderinstruction.LineSymbolBuilder;
import org.mapsforge.map.rendertheme.renderinstruction.PathText;
import org.mapsforge.map.rendertheme.renderinstruction.PathTextBuilder;
import org.mapsforge.map.rendertheme.renderinstruction.Symbol;
import org.mapsforge.map.rendertheme.renderinstruction.SymbolBuilder;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX2 handler to parse XML render theme files.
 */
public final class RenderThemeHandler extends DefaultHandler {
	private static enum Element {
		RENDER_THEME, RENDERING_INSTRUCTION, RULE;
	}

	private static final String ELEMENT_NAME_RULE = "rule";
	private static final Logger LOGGER = Logger.getLogger(RenderThemeHandler.class.getName());
	private static final String UNEXPECTED_ELEMENT = "unexpected element: ";

	public static RenderTheme getRenderTheme(XmlRenderTheme xmlRenderTheme)
			throws SAXException, ParserConfigurationException, IOException {
		RenderThemeHandler renderThemeHandler = new RenderThemeHandler(xmlRenderTheme.getRelativePathPrefix());
		XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
		xmlReader.setContentHandler(renderThemeHandler);
		InputStream inputStream = null;
		try {
			inputStream = xmlRenderTheme.getRenderThemeAsStream();
			xmlReader.parse(new InputSource(inputStream));
			return renderThemeHandler.renderTheme;
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	private Rule currentRule;
	private final Stack<Element> elementStack = new Stack<Element>();
	private int level;
	private final String relativePathPrefix;
	private RenderTheme renderTheme;
	private final Stack<Rule> ruleStack = new Stack<Rule>();

	private RenderThemeHandler(String relativePathPrefix) {
		super();
		this.relativePathPrefix = relativePathPrefix;
	}

	@Override
	public void endDocument() {
		if (this.renderTheme == null) {
			throw new IllegalArgumentException("missing element: rules");
		}

		this.renderTheme.setLevels(this.level);
		this.renderTheme.complete();
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		this.elementStack.pop();

		if (ELEMENT_NAME_RULE.equals(qName)) {
			this.ruleStack.pop();
			if (this.ruleStack.empty()) {
				this.renderTheme.addRule(this.currentRule);
			} else {
				this.currentRule = this.ruleStack.peek();
			}
		}
	}

	@Override
	public void error(SAXParseException exception) {
		LOGGER.log(Level.SEVERE, null, exception);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		try {
			if ("rendertheme".equals(qName)) {
				checkState(qName, Element.RENDER_THEME);
				this.renderTheme = new RenderThemeBuilder(qName, attributes).build();
			}

			else if (ELEMENT_NAME_RULE.equals(qName)) {
				checkState(qName, Element.RULE);
				Rule rule = new RuleBuilder(qName, attributes, this.ruleStack).build();
				if (!this.ruleStack.empty()) {
					this.currentRule.addSubRule(rule);
				}
				this.currentRule = rule;
				this.ruleStack.push(this.currentRule);
			}

			else if ("area".equals(qName)) {
				checkState(qName, Element.RENDERING_INSTRUCTION);
				Area area = new AreaBuilder(qName, attributes, this.level++,
						this.relativePathPrefix).build();
				this.currentRule.addRenderingInstruction(area);
			}

			else if ("caption".equals(qName)) {
				checkState(qName, Element.RENDERING_INSTRUCTION);
				Caption caption = new CaptionBuilder(qName, attributes).build();
				this.currentRule.addRenderingInstruction(caption);
			}

			else if ("circle".equals(qName)) {
				checkState(qName, Element.RENDERING_INSTRUCTION);
				Circle circle = new CircleBuilder(qName, attributes, this.level++).build();
				this.currentRule.addRenderingInstruction(circle);
			}

			else if ("line".equals(qName)) {
				checkState(qName, Element.RENDERING_INSTRUCTION);
				Line line = new LineBuilder(qName, attributes, this.level++,
						this.relativePathPrefix).build();
				this.currentRule.addRenderingInstruction(line);
			}

			else if ("lineSymbol".equals(qName)) {
				checkState(qName, Element.RENDERING_INSTRUCTION);
				LineSymbol lineSymbol = new LineSymbolBuilder(qName, attributes,
						this.relativePathPrefix).build();
				this.currentRule.addRenderingInstruction(lineSymbol);
			}

			else if ("pathText".equals(qName)) {
				checkState(qName, Element.RENDERING_INSTRUCTION);
				PathText pathText = new PathTextBuilder(qName, attributes).build();
				this.currentRule.addRenderingInstruction(pathText);
			}

			else if ("symbol".equals(qName)) {
				checkState(qName, Element.RENDERING_INSTRUCTION);
				Symbol symbol = new SymbolBuilder(qName, attributes, this.relativePathPrefix)
						.build();
				this.currentRule.addRenderingInstruction(symbol);
			}

			else {
				throw new SAXException("unknown element: " + qName);
			}
		} catch (IOException e) {
			throw new SAXException(null, e);
		}
	}

	@Override
	public void warning(SAXParseException exception) {
		LOGGER.log(Level.SEVERE, null, exception);
	}

	private void checkElement(String elementName, Element element) throws SAXException {
		switch (element) {
			case RENDER_THEME:
				if (!this.elementStack.empty()) {
					throw new SAXException(UNEXPECTED_ELEMENT + elementName);
				}
				return;

			case RULE:
				Element parentElement = this.elementStack.peek();
				if (parentElement != Element.RENDER_THEME && parentElement != Element.RULE) {
					throw new SAXException(UNEXPECTED_ELEMENT + elementName);
				}
				return;

			case RENDERING_INSTRUCTION:
				if (this.elementStack.peek() != Element.RULE) {
					throw new SAXException(UNEXPECTED_ELEMENT + elementName);
				}
				return;
		}

		throw new SAXException("unknown enum value: " + element);
	}

	private void checkState(String elementName, Element element) throws SAXException {
		checkElement(elementName, element);
		this.elementStack.push(element);
	}
}
