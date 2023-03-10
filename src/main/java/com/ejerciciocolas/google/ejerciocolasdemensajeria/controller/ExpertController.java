package com.ejerciciocolas.google.ejerciocolasdemensajeria.controller;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.GeneralInfoExpertDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.Expert;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.service.ExpertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("experts")
public class ExpertController {

    private final ExpertService expertService;

    @GetMapping("/info")
    public List<GeneralInfoExpertDTO> getGeneralInfoExpert(){

        return expertService.getListGeneralInfoExpert();
    }

    @GetMapping("/expert/{id}")
    public Expert getGeneralInfoExpert(@PathVariable long id){
        return expertService.findExperById(id);
    }


}
