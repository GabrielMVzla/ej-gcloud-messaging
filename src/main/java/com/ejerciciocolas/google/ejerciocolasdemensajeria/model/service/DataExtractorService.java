package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.service;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.config.util.CsvResultSetExtractor;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.connector.BigQueryConnector;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dao.BigQueryDAO;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class DataExtractorService {

    private final JdbcTemplate jdbcTemplate;
    private final BigQueryConnector bigQueryConnector;
    private final BigQueryDAO bigQueryDAO;

    /**
     * Crea un archivo temporal con info de la base de datos <em><u>H2 en este proyecto</u></em> que pueda ser envíado a gcloud BigQuery
     *
     * @param sqlQuery String
     * @param schema List&#60;Pair&#60;String, String>>
     * @return File
     * @throws IOException
     */
    public File queryToCSV(String sqlQuery, List<Pair<String, String>> schema) throws IOException {

        File csvFile = Files.createTempFile("extract_data", ".csv").toFile();

        jdbcTemplate.query(sqlQuery, new CsvResultSetExtractor(csvFile, schema));

        return csvFile;
    }

    /**
     * Envía la data seleccionada por la query dada a gcloud -> data-set -> tabla
     *
     * @param sqlQuery String
     * @param schema List&#60;Pair&#60;String, String>>
     * @throws IOException
     */
    public void queryToBigQuery(String sqlQuery, List<Pair<String, String>> schema) throws IOException {

        File csvFile = this.queryToCSV(sqlQuery, schema);

        bigQueryConnector.uploadToBigQuery(csvFile, "tbl_demo", true, schema);
    }

    /**
     * Obtiene la data de l@s expert@s desde gcloud BigQuery
     *
     * @param page int
     * @return Page&#60;Map&#60;String, Object>>
     * @throws InterruptedException
     */
    public Page<Map<String, Object>> getExpertsDataByBigQuery(int page) throws InterruptedException {

        String query = "\nSELECT id, operation_type, amount_entered, total_points, operation_date\n" +
                "FROM `gcp-pubsub-379420.ds_demo.tbl_demo`";

        return bigQueryDAO.getPagedExpertsDataByBigQuery(query, page);
    }
}
