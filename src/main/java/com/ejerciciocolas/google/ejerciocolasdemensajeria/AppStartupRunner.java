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

    @Autowired
    private BigQuery bigQuery;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Pair<String, String>> schema = Arrays.asList(
                Pair.of("id", "NUMERIC"),
                Pair.of("operation_type", "STRING"),
                Pair.of("amount_entered", "NUMERIC"),
                Pair.of("total_points", "NUMERIC"),
                Pair.of("operation_date", "TIMESTAMP")
        );

        String sqlQuery = "SELECT\n" +
                                "e.id, oel.operation_type, oel.amount_entered, ep.total_points, oel.operation_date\n" +
                            "FROM\n" +
                                "experts e\n" +
                            "INNER JOIN experts_points ep ON e.id = ep.id_experts\n" +
                            "INNER JOIN operations_experts_log oel ON e.id = oel.id_experts\n";

        dataExtractorService.queryToBigQuery(sqlQuery, schema);

        /**     TODO remover en próximo commit
                String query = "SELECT subscriptionId, dateBought\n" +
                        "FROM `gcp-pubsub-379420.ds_demo.tbl_demo`\n" +
                        "LIMIT 1000";

                QueryJobConfiguration queryConfig =
                        QueryJobConfiguration.newBuilder(query).build();

                TableResult result = bigQuery.query(queryConfig);

                List<Map<String, Object>> rows = new ArrayList<>();
                if(result.getTotalRows() != 0){
                    result.getValues().forEach( fieldValues -> {
                        Map<String, Object> row = new HashMap<>();
                        log.info(fieldValues.get(0).getValue() + ", " + fieldValues.get(1).getValue()); // + ", " + fieldValues.get(2).getValue()
                        row.put("subscriptionId", fieldValues.get(0).getValue());
                        row.put("dateBought", fieldValues.get(1).getValue());
                        //row.put("OS", fieldValues.get(2).getValue());
                    });
                } else {
                    log.info("No hay info");
                }
         */
    }
}
