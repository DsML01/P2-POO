package br.ufal.ic.p2.jackut;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que representa um recado/mensagem enviada entre usuários.
 * @author Davi Silva
 */

public class Recado {
    /** Login do usuário remetente */
    private String remetente;
    /** Conteúdo da mensagem */
    private String mensagem;

    /**
     * Construtor vazio para serialização JSON.
     */
    public Recado(){};

    /**
     * Constrói um novo recado com remetente e mensagem.
     *
     * @param remetente Login do usuário remetente
     * @param mensagem Conteúdo da mensagem
     */
    @JsonCreator
    public Recado(@JsonProperty("remetente") String remetente, @JsonProperty("mensagem") String mensagem)
    {
        this.remetente = remetente;
        this.mensagem = mensagem;
    }

    /**
     * Obtém o login do remetente.
     *
     * @return Login do remetente
     */
    public String getRemetente()
    {
        return remetente;
    }

    /**
     * Obtém o conteúdo da mensagem.
     *
     * @return Texto da mensagem
     */
    public String getMensagem()
    {
        return mensagem;
    }

}
