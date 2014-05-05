package com.zallek.collide.level.index;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import com.zallek.collide.util.constants.XmlLevelIndexConstants;

public class LevelIndexXmlHandler extends DefaultHandler {

	List<LevelIndexXml> resultList;
	LevelIndexXml currentLevel;
	
	public LevelIndexXmlHandler() {
		resultList = new ArrayList<LevelIndexXml>();
	}
	
	@Override
	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException
	{
		if (qName.equals(XmlLevelIndexConstants.TAG_LEVEL)) {
			currentLevel = new LevelIndexXml();
			currentLevel.id = Long.valueOf(attributes.getValue(XmlLevelIndexConstants.TAG_LEVEL_ATTRIBUTE_ID));
			currentLevel.category = Long.valueOf(attributes.getValue(XmlLevelIndexConstants.TAG_LEVEL_ATTRIBUTE_CATEGORY));
			currentLevel.number = Integer.valueOf(attributes.getValue(XmlLevelIndexConstants.TAG_LEVEL_ATTRIBUTE_NUMBER));
			currentLevel.path = attributes.getValue(XmlLevelIndexConstants.TAG_LEVEL_ATTRIBUTE_PATH);
		}
	}
 
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException 
	{
		if (qName.equals(XmlLevelIndexConstants.TAG_LEVEL)) {
			resultList.add(currentLevel);
		}
	}

	
	public List<LevelIndexXml> getResult() {
		return resultList;
	}
	
	
	public static List<LevelIndexXml> loadLevels(final InputStream pInputStream) {
		try {
			final SAXParserFactory spf = SAXParserFactory.newInstance();
			final SAXParser sp = spf.newSAXParser();

			final XMLReader xr = sp.getXMLReader();

			final LevelIndexXmlHandler levelContentHandler = new LevelIndexXmlHandler();
			xr.setContentHandler(levelContentHandler);

			xr.parse(new InputSource(new BufferedInputStream(pInputStream)));

			return levelContentHandler.getResult();
		} catch (final Exception e) {
		//TODO
		}
		return null;
	}
}
