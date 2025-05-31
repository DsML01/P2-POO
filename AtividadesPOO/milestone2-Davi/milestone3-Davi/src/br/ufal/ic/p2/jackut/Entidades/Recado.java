package br.ufal.ic.p2.jackut.Entidades;

/**
 * Classe que representa um recado enviado de um usuário para outro.
 */
public class Recado {
    private final User remetente;
    private final User destinatario;
    private final String recado;

    /**
     * Cria um novo Recado.
     *
     * @param remetente     Remetente do recado.
     * @param destinatario  Destinatário do recado.
     * @param recado        O texto do recado.
     */
    public Recado(User remetente, User destinatario, String recado) {
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.recado = recado;
    }

    /**
     * Retorna o remetente do recado.
     * @return Remetente do recado.
     */
    public User getRemetente() {
        return this.remetente;
    }

    /**
     * Retorna o destinatário do recado.
     * @return Destinatário do recado.
     */
    public User getDestinatario() {
        return this.destinatario;
    }

    /**
     * Retorna o texto do recado.
     * @return O recado.
     */
    public String getRecado() {
        return this.recado;
    }
}