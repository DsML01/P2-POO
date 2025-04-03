package br.ufal.ic.p2.jackut;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que representa um recado/mensagem enviada entre usu�rios.
 * @author Davi Silva
 */

public class Recado {
    /** Login do usu�rio remetente */
    private String remetente;
    /** Conte�do da mensagem */
    private String mensagem;

    /**
     * Construtor vazio para serializa��o JSON.
     */
    public Recado(){};

    /**
     * Constr�i um novo recado com remetente e mensagem.
     *
     * @param remetente Login do usu�rio remetente
     * @param mensagem Conte�do da mensagem
     */
    @JsonCreator
    public Recado(@JsonProperty("remetente") String remetente, @JsonProperty("mensagem") String mensagem)
    {
        this.remetente = remetente;
        this.mensagem = mensagem;
    }

    /**
     * Obt�m o login do remetente.
     *
     * @return Login do remetente
     */
    public String getRemetente()
    {
        return remetente;
    }

    /**
     * Obt�m o conte�do da mensagem.
     *
     * @return Texto da mensagem
     */
    public String getMensagem()
    {
        return mensagem;
    }

}
