package br.ufal.ic.p2.jackut.Utilidade;

import java.util.ArrayList;


/**
 * Classe com métodos utilitários para as Strings da aplicação.
 */

public class UtilidadeString {

    /**
     * Formata um ArrayList para uma String.
     * Tendo o seguinte formato: {1,2,...,n}.
     *
     * @param arrayList ArrayList a ser formatado.
     * @param <T>       Tipo do ArrayList.
     * @return          String formatada.
     */

    public static <T> String formatArrayList(ArrayList<T> arrayList) {
        if (arrayList.isEmpty()) {
            return "{}";
        }

        StringBuilder formattedString = new StringBuilder("{");
        for (int i = 0; i < arrayList.size(); i++) {
            formattedString.append(arrayList.get(i).toString());
            if (i != arrayList.size() - 1) {
                formattedString.append(",");
            }
        }
        formattedString.append("}");
        return formattedString.toString();
    }
}
