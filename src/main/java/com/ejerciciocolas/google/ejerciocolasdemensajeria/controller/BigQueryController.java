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

    /**
     * Muestra la informaci&#243;n de manera paginada los expertos almacenada en gcloud BigQuery
     *
     * @param page Integer
     * @return Page&#60;Map&#60;String, Object>>
     * @throws InterruptedException
     */
    @GetMapping("/experts-info-bq/{page}")
    public Page<Map<String, Object>> getExpertsInfoByBigQuery(@PathVariable Integer page) throws InterruptedException {
        return dataExtractorService.getExpertsDataByBigQuery(page);
    }

    /**
     * Retorna un listado con la informaci&#243;n general de las/los expert@s
     *
     * @return List&#60;ExpertInfoBigQueryDTO>
     */
    @GetMapping("/experts-info")
    public List<ExpertInfoBigQueryDTO> getExpertInfoFromBigQuery() {
        return expertService.getListExpertInfoToSendBigQuery();
    }
}
