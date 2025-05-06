package br.ufal.ic.p2.jackut.Entidades;


/**
 * Classe que representa uma mensagem.
 */

public class Mensagem {
    private final String mensagem;

    /**
     * Constrói uma nova {@code Mensagem} do Jackut.
     *
     * @param mensagem Mensagem
     */

    public Mensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    /**
     * Retorna a mensagem.
     *
     * @return Mensagem
     */

    public String getMensagem() {
        return this.mensagem;
    }
}
