package org.vaadin.haijian.filegenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.vaadin.data.Container;

public class CSVFileBuilder extends FileBuilder {

    public static final Logger log = Logger.getLogger(CSVFileBuilder.class);

    private FileWriter writer;
    private int rowNr;
    private int colNr;

    public CSVFileBuilder(Container container) {
        super(container);
        log.debug("* csv builder created");
    }

    @Override
    protected void resetContent() {
        try {
            colNr = 0;
            rowNr = 0;
            writer = new FileWriter(file);
        } catch (IOException e) {
            log.fatal(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void buildCell(Object value) {
        try {
            if (value == null) {
                writer.append("");
            }
            else if (value instanceof java.sql.Date) {
                writer.append(formatSqlDate((java.sql.Date) value));
            }
            else if (value instanceof Timestamp) {
                writer.append(formatTimestamp((Timestamp) value));
            }
            else if (value instanceof Date ) {
                writer.append(formatDate((Date) value));
            }
            else if (value instanceof Calendar) {
                Calendar calendar = (Calendar) value;
                writer.append(formatDate(calendar.getTime()));
            }
            else {
                String mValue = value.toString() ;
                if ( value instanceof String  && !(mValue.startsWith("\"") && mValue.endsWith("\""))) {
                    mValue = "\""+mValue.replaceAll("\"", "\"\"") +"\"";
                }
                writer.append( mValue);
            }
        } catch (IOException e) {
            log.fatal(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getFileExtension() {
        return ".csv";
    }

    @Override
    protected void writeToFile() {
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            log.fatal(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onNewRow() {
        if (rowNr > 0) {
            try {
                writer.append("\n");
            } catch (IOException e) {
                log.fatal(e);
                throw new RuntimeException(e);
            }
        }
        rowNr++;
        colNr = 0;
    }

    @Override
    protected void onNewCell() {
        if (colNr > 0 && colNr < getNumberofColumns()) {
            try {
                writer.append(",");
            } catch (IOException e) {
                log.fatal(e);
                throw new RuntimeException(e);
            }
        }
        colNr++;
    }

    @Override
    protected void buildColumnHeaderCell(String header) {
        try {
            writer.append(header);
        } catch (IOException e) {
            log.fatal(e);
            throw new RuntimeException(e);
        }
    }

}
