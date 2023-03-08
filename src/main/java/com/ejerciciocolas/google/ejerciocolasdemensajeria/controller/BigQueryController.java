package com.ejerciciocolas.google.ejerciocolasdemensajeria.controller;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ExpertInfoBigQueryDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.service.DataExtractorService;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.service.ExpertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/big-query")
public class BigQueryController {

    private final DataExtractorService dataExtractorService;
    private final ExpertService expertService;

    @GetMapping("/experts-info-bq/{page}")
    public Page<Map<String, Object>> getExpertsInfoByBigQuery(@PathVariable Integer page) throws InterruptedException {
        return dataExtractorService.getExpertsDataByBigQuery(page);
    }

    @GetMapping("/experts-info")
    public List<ExpertInfoBigQueryDTO> getExpertInfoFromBigQuery() {
        return expertService.getExpertInfoToSendBigQuery();
    }
}
