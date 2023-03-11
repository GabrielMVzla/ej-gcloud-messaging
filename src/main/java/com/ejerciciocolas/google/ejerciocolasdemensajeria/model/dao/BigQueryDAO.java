package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dao;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BigQueryDAO {

    private final BigQuery bigQuery;
    private QueryJobConfiguration queryConfig;

    private long totalRows;

    @Value("${page_size.value}")
    private int PAGE_SIZE;

    private static final String OFFSET = "\nOFFSET %s";
    private static final String LIMIT = "\nLIMIT %s" ;
    private static final String SEMI_COLON = ";";

    private String query;

    /**
     * Establece desde donde comienza la busqueda de los datos hasta su l&#237;mite, es as&#237; como se dividir&#225; la p&#225;gina
     *
     * @param query String
     * @param pageable Pageable
     * @throws InterruptedException
     */
    private void setPageableQuery(String query, Pageable pageable) throws InterruptedException {
        this.query = query + LIMIT + OFFSET + SEMI_COLON;

        long offset = pageable.getOffset();
        long limit = pageable.getPageSize();

        this.query = String.format(
                this.query,
                limit,
                offset
        );

        queryConfig = QueryJobConfiguration
                .newBuilder(this.query)
                .build();

        totalRows = bigQuery.query(queryConfig).getTotalRows();

        log.info(this.query);
    }

    /**
     * Se especifica la query y la cantidadd de datos que debe devolver cada p&#225;gina
     *
     * @param query String
     * @param page int
     * @return Page&#60;Map&#60;String, Object>>
     * @throws InterruptedException
     */
    public Page<Map<String, Object>> getPagedExpertsDataByBigQuery(String query, int page) throws InterruptedException {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        setPageableQuery(query, pageable);

        return findAllPage( pageable);
    }

    /**
     * Retorna los datos de la página utilizando como referencia LIMIT y OFFSET en la Query.
     *
     * @param pageable Pageable
     * @return Page&#60;Map<&#60;tring, Object>>
     * @throws InterruptedException
     */
    private Page<Map<String, Object>> findAllPage(Pageable pageable) throws InterruptedException {
       // QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();
        TableResult result = bigQuery.query(queryConfig);

        List<Map<String, Object>> rows = new ArrayList<>();

        if (result != null && result.getTotalRows() > 0) {
            result.getValues().forEach(fieldValues -> {

                Map<String, Object> row = new HashMap<>();
                log.info("[" + fieldValues.get(0).getValue() + ", " + fieldValues.get(1).getValue()  + ", " + fieldValues.get(2).getValue()  + ", " + fieldValues.get(3).getValue()  + ", " + fieldValues.get(4).getValue()  + ", " + fieldValues.get(5).getValue()  + ", " + fieldValues.get(6).getValue() + "]");

                row.put("id", fieldValues.get(0).getValue());
                row.put("operation_type", fieldValues.get(1).getValue());
                row.put("amount_entered", fieldValues.get(2).getValue());
                row.put("points_operation", fieldValues.get(3).getValue());
                row.put("acumulated_residual_points", fieldValues.get(4).getValue());
                row.put("total_points", fieldValues.get(5).getValue());
                row.put("operation_date", fieldValues.get(6).getValue().toString());

                rows.add(row);
            });
        } else {
            log.info("No hay info");
        }
        log.info("findAll sql: {}", query);

        long total = countAll();

        return new PageImpl<>(rows, pageable, total);
    }

    /*** Count all the rows.*/
    private long countAll() throws InterruptedException {
        return totalRows;
    }
    /*
     * Actually do something with each row.
     * In this demo, I'm just logging the row.
     * In a real scenario, maybe you're building up a bulk request to send somewhere else, etc.

    private void handleRow(Map<String, Object> row) {
        log.info(row.toString());
    }
     */
}
