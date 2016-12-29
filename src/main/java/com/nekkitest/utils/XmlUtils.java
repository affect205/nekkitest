package com.nekkitest.utils;

import com.nekkitest.entity.EntryXml;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Created by Alex on 28.12.2016.
 */
public class XmlUtils {
    private static final String TAG_CONTENT = "content";
    private static final String TAG_CREATEDATE = "createdate";

    private static SAXParserFactory factory;
    private static SAXParser saxParser;

    static {
        try {
            factory = SAXParserFactory.newInstance();
            saxParser = factory.newSAXParser();
        } catch (Exception e) {}
    }

    public static EntryXml parseEntryXml(String srcXml) {
        try {
            Path pathXml = Paths.get(srcXml);
            EntryXmlHandler handler = new EntryXmlHandler(String.valueOf(pathXml.getFileName()));
            saxParser.parse(Files.newInputStream(pathXml), handler);
            return handler.getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class EntryXmlHandler extends DefaultHandler {
        private String srcXml;
        private String thisElement = "";
        private EntryXml entryXml;
        private String content;
        private String createDate;

        public EntryXmlHandler(String srcXml) {
            this.srcXml = srcXml;
            this.entryXml = new EntryXml();
        }

        public EntryXml getResult() {
            return entryXml;
        }

        @Override
        public void startDocument() throws SAXException {
            System.out.println(String.format("Start parse XML - %s", srcXml));
        }

        @Override
        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
            thisElement = qName;
            if (thisElement.equals(TAG_CONTENT)) content = "";
            if (thisElement.equals(TAG_CREATEDATE)) createDate = "";
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            switch (thisElement) {
                case TAG_CONTENT:
                    content += String.copyValueOf(ch, start, length).trim();
                    break;
                case TAG_CREATEDATE:
                    createDate += String.copyValueOf(ch, start, length).trim();
                    break;
            }
        }

        @Override
        public void endDocument() {
            System.out.println(String.format("End parse XML - %s", srcXml));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // TODO: validation
            if (createDate != null && !createDate.isEmpty()) {
                LocalDateTime localDateTime = LocalDateTime.parse(createDate, formatter);
                Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
                entryXml.setCreateDate(Date.from(instant));
            }
            entryXml.setContent(content);
        }
    }
}
