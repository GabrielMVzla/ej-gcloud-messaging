package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.service;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.config.util.ConstantsUtil;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.config.util.CsvExtractor;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.config.gcloud_bigquery.connector.*;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dao.BigQueryDAO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ExpertInfoBigQueryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class DataExtractorService {

    private final BigQueryConnector bigQueryConnector;
    private final BigQueryDAO bigQueryDAO;
    private final ExpertService expertService;

    /**
     * Crea un archivo temporal con una lista de informaci&#243;n de la base de datos <em><u>H2 en este proyecto</u></em> que pueda ser enviado a gcloud BigQuery
     *
     * @param expertInfoBigQueryDTO ExpertInfoBigQueryDTO
     * @throws IOException
     */
    public void queryToCSVAndBigQuery(ExpertInfoBigQueryDTO expertInfoBigQueryDTO) throws IOException {
        File csvFile = Files.createTempFile("extract_data", ".csv").toFile();

        CsvExtractor csvExtractor = CsvExtractor.getSingletonIntance();
        csvExtractor.oneExtractor(csvFile, expertInfoBigQueryDTO);

        bigQueryConnector.uploadToBigQuery(csvFile, ConstantsUtil.TABLE_DATA_SET, false, ConstantsUtil.SCHEMA_TBL_DEMO);
    }

    /**
     * Crea un archivo temporal con una lista de informaci&#243;n de la base de datos <em><u>H2 en este proyecto</u></em> que pueda ser enviado a gcloud BigQuery
     *
     * @throws IOException
     */
    public void queryListToCSVAndBigQuery() throws IOException {

        File csvFile = Files.createTempFile("extract_data", ".csv").toFile();
        List<ExpertInfoBigQueryDTO> experstInfoFromBigQuery =
                expertService.getListExpertInfoToSendBigQuery();

        CsvExtractor csvExtractor = CsvExtractor.getSingletonIntance();
        csvExtractor.listExtractor(csvFile, experstInfoFromBigQuery);

        bigQueryConnector.uploadToBigQuery(csvFile, ConstantsUtil.TABLE_DATA_SET, true, ConstantsUtil.SCHEMA_TBL_DEMO);
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
