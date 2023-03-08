package com.ejerciciocolas.google.ejerciocolasdemensajeria.config.util;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ExpertInfoFromBigQueryDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.util.Pair;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
    public CsvExtractor(File file, List<ExpertInfoFromBigQueryDTO> experstInfoFromBigQuery, List<Pair<String, String>> columns){
        try {
            FileWriter out = new FileWriter(file);
            CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);

            int cont = -1;
            while (++cont < experstInfoFromBigQuery.size()) {
                List<Object> values = new ArrayList<>();
                ExpertInfoFromBigQueryDTO expertInfo = experstInfoFromBigQuery.get(cont);

                //remueve nombre de clase y paréntesis, así como el atributo, es decir, deja solo el resultado
                String[] arrayExpertInfo = expertInfo.toString()
                        .replaceAll(".*[(](.*)[)]", "$1")
                        .replaceAll("(\\w*=)+", "")
                        .split(",");

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
     /* TODO eliminar en sig. commit
     switch (col.getSecond()) {
                        case "NUMERIC":
                            value = expertInfo.getId(); //id Long
                            break;
                        case "DATETIME":
                            value = expertInfo.getOperationDate(); //id DATETIME

                            break;
                        case "DATE":
                            Date date = rs.getDate(col.getFirst());
                            value = expertInfo.get(); //id Long

                            value = date != null ? date.toLocalDate() : null;
                            break;
                        case "TIMESTAMP":
                            value = rs.getTimestamp(col.getFirst());
                            value = expertInfo.getId(); //id Long

                            break;
                        case "FLOAT":
                            value = rs.getDouble(col.getFirst());
                            value = expertInfo.getId(); //id Long

                            break;
                        case "STRING":
                        default:
                            value = rs.getString(col.getFirst());
                            value = expertInfo.getId(); //id Long

                            break;
                    }*/