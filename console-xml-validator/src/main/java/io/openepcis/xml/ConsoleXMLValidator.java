package io.openepcis.xerces;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.validation.Schema;
import java.io.FileInputStream;

public class ConsoleXMLValidator {

    public static void main(String[] args) throws SAXException {
        if (args.length != 2) {
            System.err.println("Usage: xsd_file xml_file");
            System.exit(-1);
        }
        final String schemaName = args[0];
        final String xmlName = args[1];
        final Schema schema = XmlSchema11Validator.loadSchema(schemaName);
        validate(schema, xmlName);
    }

    public static void validate(Schema schema, String xmlName) {
        try {
            final ConsoleErrorHandler errorHandler = new ConsoleErrorHandler();
            XmlSchema11Validator.validate(schema, new FileInputStream(xmlName), errorHandler);
            System.out.println();
            if (errorHandler.errorCount > 0) {
                System.out.println("Failed with errors: " + errorHandler.errorCount);
                System.exit(-1);
            } else {
                System.out.println("Passed.");
                System.exit(0);
            }

        } catch (Exception e) {
            System.out.println();
            System.out.println(e.toString());
        }
    }

    public static class ConsoleErrorHandler implements ErrorHandler {
        private int errorCount = 0;
        public void warning(SAXParseException e) throws SAXException {
            System.out.println("Warning: ");
            printException(e);
        }

        public void error(SAXParseException e) throws SAXException {
            System.out.println("Error: ");
            printException(e);
        }

        public void fatalError(SAXParseException e) throws SAXException {
            System.out.println("Fattal error: ");
            printException(e);
        }

        private void printException(SAXParseException e) {
            errorCount++;
            System.out.println("   Line number: " + e.getLineNumber());
            System.out.println("   Column number: " + e.getColumnNumber());
            System.out.println("   Message: " + e.getMessage());
            System.out.println();
        }
    }
}
