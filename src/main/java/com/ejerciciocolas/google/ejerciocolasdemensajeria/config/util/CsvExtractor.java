package com.ejerciciocolas.google.ejerciocolasdemensajeria.config.util;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ExpertInfoBigQueryDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class CsvExtractor
{
    /**
     * Toma la información de los expertos y la guarda en el archivo que se manda <u>(1st parametro)</u>.
     *
     * @param file - archivo temporal donde se guarda para después utilizar en guardado de ETL
     * @param experstInfoFromBigQuery - info a guardar
     * @param columns - schema
     */
    public CsvExtractor(File file, List<ExpertInfoBigQueryDTO> experstInfoFromBigQuery, List<Pair<String, String>> columns){
        try {
            FileWriter out = new FileWriter(file);
            CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);

            int cont = -1;
            while (++cont < experstInfoFromBigQuery.size()) {
                List<Object> values = new ArrayList<>();
                ExpertInfoBigQueryDTO expertInfo = experstInfoFromBigQuery.get(cont);

                //remueve nombre de clase y paréntesis, así como el atributo, es decir, deja solo el resultado
                String[] arrayExpertInfo = AttributesToStringToArrayUtil.AttributesToStringToArray(expertInfo.toString());

                //Arrays.asList(arrayExpertInfo).forEach(System.out::println);

                Object value;
                for (String expInf: arrayExpertInfo ) {
                    value = expInf;
                    values.add(value);
                }
                printer.printRecord(values);
            }
            printer.close(true);
        } catch (IOException ex) {
            // handle an error while reading dataset
        }
    }
}