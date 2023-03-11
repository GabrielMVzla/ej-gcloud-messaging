package com.ejerciciocolas.google.ejerciocolasdemensajeria.controller;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ExpertOperationDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.GeneralInfoExpertDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.Expert;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.service.ExpertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("experts")
public class ExpertController {

    private final ExpertService expertService;

    /**
     * Informaci&#243;n general de los expertos
     *
     * @return ResponseEntity&#60;List&#60;GeneralInfoExpertDTO>>
     */
    @GetMapping("/info")
    public ResponseEntity<List<GeneralInfoExpertDTO>>  getGeneralInfoExpert(){

        List<GeneralInfoExpertDTO> generalInfoExpertDTOs =  expertService.getListGeneralInfoExpert();
        return new ResponseEntity<>(generalInfoExpertDTOs, HttpStatus.OK);
    }

    /**
     * Informaci&#243;n general de un/una expert@ en espec&#237;fico indicado por su id
     *
     * @param id long
     * @return ResponseEntity&#60;Expert>
     */
    @GetMapping("/expert/{id}")
    public ResponseEntity<Expert> getGeneralInfoExpert(@PathVariable long id){

        Expert expert = expertService.findExperById(id);
        HttpStatus status = expert != null ? HttpStatus.CREATED : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(expert, status);

    }


}
