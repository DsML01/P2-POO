package br.ufal.ic.p2.jackut.Entidades;

/**
 * Classe que representa uma mensagem de comunidade.
 * Mantida como uma classe para clareza semântica e extensibilidade futura.
 */
public class Mensagem {
    private final String mensagem;

    /**
     * Constrói uma nova {@code Mensagem} do Jackut.
     *
     * @param mensagem O texto da mensagem.
     */
    public Mensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    /**
     * Retorna o texto da mensagem.
     * @return A mensagem.
     */
    public String getMensagem() {
        return this.mensagem;
    }
}