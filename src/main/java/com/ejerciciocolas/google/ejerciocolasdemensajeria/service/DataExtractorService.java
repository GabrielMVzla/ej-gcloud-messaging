package com.ejerciciocolas.google.ejerciocolasdemensajeria.service;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.config.util.CsvResultSetExtractor;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.connector.BigQueryConnector;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DataExtractorService {

    private final JdbcTemplate jdbcTemplate;
    private final BigQueryConnector bigQueryConnector;

    public File queryToCSV(String sqlQuery, List<Pair<String, String>> schema) throws IOException {

        File csvFile = Files.createTempFile("extract_data", ".csv").toFile();

        jdbcTemplate.query(sqlQuery, new CsvResultSetExtractor(csvFile, schema));

        return csvFile;
    }

    public void queryToBigQuery(String sqlQuery, List<Pair<String, String>> schema) throws IOException {

        File csvFile = this.queryToCSV(sqlQuery, schema);

        bigQueryConnector.uploadToBigQuery(csvFile, "tbl_demo", true, schema);

    }
}
