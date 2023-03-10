package com.ejerciciocolas.google.ejerciocolasdemensajeria.config.util;

public class AttributesToStringToArrayUtil {

    /**
     * Convierte la información del método toString() de las clases en un arreglo que su tamaño depende de la cantidad
     * de atributos, la información de los atributos se almacena en cada índice del arreglo
     *
     * @param cadena String
     * @return String[]
     */
    public static String[] AttributesToStringToArray (String cadena) {
        return cadena.replaceAll("^\\w+\\((.*)\\)$", "$1")
                .replaceAll("(^|[, ])\\w+=", "")
                .split(",");
    }

}
