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

    /** The bigQuery uses the default data source.*/
    private final BigQuery bigQuery;
    private QueryJobConfiguration queryConfig;

    @Value("${page_size.value}")
    private int PAGE_SIZE;
    //private static final String WHERE_PAGEABLE = "\nWHERE id > %s AND id <= %s";
    private static final String OFFSET = "\nOFFSET %s";
    private static final String LIMIT = "\nLIMIT %s" ;
    private static final String SEMI_COLON = ";";

    private String query;

    private void setPageableQuery(String query, Pageable pageable){
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

        log.info(this.query);
    }

    public Page<Map<String, Object>> getPagedExpertsDataByBigQuery(String query, int page) throws InterruptedException {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        setPageableQuery(query, pageable);

        return findAllPage( pageable);
    }
    //TODO corregir obtención del dato mostrado operation_date TIMESTAMP
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    /**
     * Find all the rows.
     * You _could_ create the query using LIMIT and OFFSET...
     * But, I went with a plain WHERE clause that selects a range of IDs because it's faster.
     */
    private Page<Map<String, Object>> findAllPage(Pageable pageable) throws InterruptedException {
       // QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();
        TableResult result = bigQuery.query(queryConfig);

        List<Map<String, Object>> rows = new ArrayList<>();

        //TODO - gestionar null exception
        if (result.getTotalRows() > 0) {
            result.getValues().forEach(fieldValues -> {

                Map<String, Object> row = new HashMap<>();
                log.info("[" + fieldValues.get(0).getValue() + ", " + fieldValues.get(1).getValue()  + ", " + fieldValues.get(2).getValue()  + ", " + fieldValues.get(3).getValue()  + ", " + fieldValues.get(4).getValue() + "]");

                row.put("id", fieldValues.get(0).getValue());
                row.put("operation_type", fieldValues.get(1).getValue());
                row.put("amount_entered", fieldValues.get(2).getValue());
                row.put("total_points", fieldValues.get(3).getValue());
                row.put("operation_date", fieldValues.get(4).getValue().toString());

                rows.add(row);
            });
        } else {
            log.info("No hay info");
        }
        log.info("findAll sql: {}", query);

        long total = countAll(); //result.getTotalRows()

        return new PageImpl<>(rows, pageable, total);
    }

    /*** Count all the rows.*/
    private long countAll() throws InterruptedException {
        //log.info("countAll sql: {}", query);
        return bigQuery.query(queryConfig).getTotalRows();
    }
    /**
     * Actually do something with each row.
     * In this demo, I'm just logging the row.
     * In a real scenario, maybe you're building up a bulk request to send somewhere else, etc.
     */
    private void handleRow(Map<String, Object> row) {
        log.info(row.toString());
    }
}


/**     TODO remover en próximo commit
 *
 *
 *     public Object getExpertsDataByBigQuery(String query, int page) throws InterruptedException {
 *
 *         QueryJobConfiguration queryConfig =
 *                 QueryJobConfiguration.newBuilder(query).build();
 *
 *         TableResult result = bigQuery.query(queryConfig);
 *
 *         List<Map<String, Object>> rows = new ArrayList<>();
 *         if (result.getTotalRows() != 0) {
 *             result.getValues().forEach(fieldValues -> {
 *
 *                 Map<String, Object> row = new HashMap<>();
 *                 log.info(fieldValues.get(0).getValue() + ", " + fieldValues.get(1).getValue()); // + ", " + fieldValues.get(2).getValue()
 *                 row.put("subscriptionId", fieldValues.get(0).getValue());
 *                 row.put("dateBought", fieldValues.get(1).getValue());
 *                 //row.put("OS", fieldValues.get(2).getValue());
 *             });
 *         } else {
 *             log.info("No hay info");
 *         }
 *         return rows;
 *     }
 *
 */