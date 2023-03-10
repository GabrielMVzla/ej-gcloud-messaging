package com.ejerciciocolas.google.ejerciocolasdemensajeria.config.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;

import java.util.Arrays;
import java.util.List;

public class ConstantsUtil {

    @Value("${table.data_set.value}")
    public static String TABLE_DATA_SET = "tbl_demo";
    //Esquema que tomará nuestro CSV, coincide con tabla de data-set en gcloud BigQuery
    public static List<Pair<String, String>> SCHEMA_TBL_DEMO = Arrays.asList(
            Pair.of("id", "NUMERIC"),
            Pair.of("operation_type", "STRING"),
            Pair.of("amount_entered", "NUMERIC"),
            Pair.of("points_operation", "NUMERIC"),
            Pair.of("total_points", "NUMERIC"),
            Pair.of("operation_date", "TIMESTAMP")
    );
}
