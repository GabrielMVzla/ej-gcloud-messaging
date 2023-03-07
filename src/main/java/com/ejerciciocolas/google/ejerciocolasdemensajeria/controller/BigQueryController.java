package com.ejerciciocolas.google.ejerciocolasdemensajeria.controller;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.service.DataExtractorService;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/big-query")
public class BigQueryController {

    private final DataExtractorService dataExtractorService;
    private final BigQuery bigQuery;

    @GetMapping("/experts-info/{page}")
    public Page<Map<String, Object>> getExpertsInfoByBigQuery(@PathVariable Integer page) throws InterruptedException {
        return dataExtractorService.getExpertsDataByBigQuery(page);
    }

}
