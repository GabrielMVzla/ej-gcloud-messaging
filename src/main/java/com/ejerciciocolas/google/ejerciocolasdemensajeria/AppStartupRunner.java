package com.ejerciciocolas.google.ejerciocolasdemensajeria;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.service.DataExtractorService;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Component
public class AppStartupRunner implements ApplicationRunner{

    private final DataExtractorService dataExtractorService;

   @Override
    public void run(ApplicationArguments args) throws Exception {
       //Esquema que tomará nuestro CSV, coincide con tabla de data-set en gcloud BigQuery
        List<Pair<String, String>> schema = Arrays.asList(
                Pair.of("id", "NUMERIC"),
                Pair.of("operation_type", "STRING"),
                Pair.of("amount_entered", "NUMERIC"),
                Pair.of("total_points", "NUMERIC"),
                Pair.of("operation_date", "TIMESTAMP")
        );

        dataExtractorService.queryToBigQuery( schema );
    }
}
