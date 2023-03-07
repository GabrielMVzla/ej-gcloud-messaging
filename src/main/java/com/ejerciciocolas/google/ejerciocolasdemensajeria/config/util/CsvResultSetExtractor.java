package com.ejerciciocolas.google.ejerciocolasdemensajeria.config.util;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CsvResultSetExtractor implements ResultSetExtractor<Void> {

    private final File file;
    private final List<Pair<String, String>> columns;

    @Override
    public Void extractData(final ResultSet rs) {
        try {
            FileWriter out = new FileWriter(file);
            CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);

            while (rs.next()) {
                List<Object> values = new ArrayList<>();
                for (Pair<String, String> col : columns) {
                    Object value;
                    switch (col.getSecond()) {
                        case "NUMERIC":
                            value = rs.getLong(col.getFirst());
                            break;
                        case "DATE":
                            Date date = rs.getDate(col.getFirst());
                            value = date != null ? date.toLocalDate() : null;
                            break;
                        case "TIMESTAMP":
                            value = rs.getTimestamp(col.getFirst());
                            break;
                        /*case "INTEGER":
                            value = rs.getInt(col.getFirst());
                            break;¨*/
                        case "STRING":
                        default:
                            value = rs.getString(col.getFirst());
                            break;
                    }
                    values.add(value);
                }

                printer.printRecord(values);
            }

            printer.close(true);

        } catch (IOException | SQLException ex) {
            // handle an error while reading dataset
        }
        return null;
    }
}
