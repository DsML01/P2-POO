package br.ufal.ic.p2.jackut.Entidades;

import java.util.HashMap;
import java.util.Map;

import br.ufal.ic.p2.jackut.Exceptions.Perfil.*;


/**
 * Classe que representa um perfil de usuário.
 */

public class Perfil {
    private final Map<String, String> atributos = new HashMap<>();

    /**
     * Retorna o valor de um atributo do perfil.
     *
     * @param chave  Chave do atributo.
     * @return       Valor do atributo.
     *
     * @throws AtributoNaoPreenchidoException Exceção que indica que o atributo não foi preenchido.
     */

    public String getAtributo(String chave) throws AtributoNaoPreenchidoException {
        if (this.atributos.containsKey(chave)) {
            return this.atributos.get(chave);
        } else {
            throw new AtributoNaoPreenchidoException();
        }
    }

    /**
     * Retorna todos os atributos do perfil.
     *
     * @return Map com todos os atributos do perfil.
     */

    public Map<String, String> getAtributos() {
        return this.atributos;
    }

    /**
     * Adiciona um atributo ao perfil.
     *
     * @param chave Nome do atributo.
     * @param valor Valor do atributo.
     */

    public void setAtributo(String chave, String valor) {
        this.atributos.put(chave, valor);
    }
}
