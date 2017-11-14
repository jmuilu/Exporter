package org.vaadin.haijian;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;

import com.vaadin.data.Container;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import org.vaadin.haijian.filegenerator.FileBuilder;

public abstract class Exporter extends Button implements StreamSource {
    protected FileBuilder fileBuilder;
    private FileDownloader fileDownloader;
    protected String downloadFileName;

    public Exporter() {
        fileDownloader = new FileDownloader(new StreamResource(this,
                getDownloadFileName()));
        fileDownloader.extend(this);
    }

    public Exporter(Table table) {
        this();
        setTableToBeExported(table);
    }

    public Exporter(Container container, Object[] visibleColumns) {
        this();
        setCaption("Exporter");
        setContainerToBeExported(container);
        setVisibleColumns(visibleColumns);
    }

    public Exporter(Container container) {
        this(container, null);
    }

    public void setTableToBeExported(Table table) {
        setContainerToBeExported(table.getContainerDataSource());
        setVisibleColumns(table.getVisibleColumns());
        setHeader(table.getCaption());
        for (Object column: table.getVisibleColumns()) {
            String header = table.getColumnHeader(column);
            if (header != null) {
                setColumnHeader(column, header);
            }
        }
    }

    private void setContainerToBeExported(Container container) {
        fileBuilder = createFileBuilder(container);
    }

    public void setVisibleColumns(Object[] visibleColumns) {
        fileBuilder.setVisibleColumns(visibleColumns);
    }

    public void setColumnHeader(Object propertyId, String header) {
        fileBuilder.setColumnHeader(propertyId, header);
    }

    public void setHeader(String header) {
        fileBuilder.setHeader(header);
    }

    public void setLocale(Locale locale) {
        fileBuilder.setLocale(locale);
    }

    public void setDateFormat(String dateFormat) {
        fileBuilder.setDateFormat(dateFormat);
    }

    public void setSqlDateFormat(String sqlDateFormat) {
        fileBuilder.setSqlDateFormat(sqlDateFormat);
    }

    public void setTimestampFormat(String timestampFormat) {
        fileBuilder.setTimestampFormat(timestampFormat);
    }

    protected abstract FileBuilder createFileBuilder(Container container);

    protected abstract String getDownloadFileName();

    public void setDownloadFileName(String fileName) {
        downloadFileName = fileName;
        ((StreamResource) fileDownloader.getFileDownloadResource()).setFilename(getDownloadFileName());
    }

    @Override
    public InputStream getStream() {
        try {
            return new FileInputStream(fileBuilder.getFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
