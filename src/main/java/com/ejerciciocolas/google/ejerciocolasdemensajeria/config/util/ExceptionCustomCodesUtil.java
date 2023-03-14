package com.ejerciciocolas.google.ejerciocolasdemensajeria.config.util;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class ExceptionCustomCodesUtil {

    public static final String DEAFULT_CODE_ERROR = "";
    public static final String EXPERT_BAD_REQUEST_CODE = "e-400";
    public static final String EXPERT_CODE_NOT_FOUND = "e-404";

    private static Map<String, String> diccionarioMsjsErrors = ImmutableMap.<String, String>builder()
            .put(DEAFULT_CODE_ERROR, "Error generico.")
            .put(EXPERT_BAD_REQUEST_CODE, "Se ha recibido una request inválida.")
            .put(EXPERT_CODE_NOT_FOUND, "Experto no encontrado.")
            .build();

    public static String getSpecificMessageError(String code){
        code = code != null && !code.isEmpty() ? code : "Error-400"; //Error-400 por default genérico

        return diccionarioMsjsErrors.get(code);
    }
}
