package br.ufal.ic.p2.jackut.Entidades;


/**
 * Classe que representa um recado.
 */

public class Recado {
    private final User remetente;
    private final User destinatario;
    private final String recado;

    /**
     * Cria um novo Recado enviado de um User para outro.
     *
     * @param remetente     Remetente do recado.
     * @param destinatario  Destinatário do recado.
     * @param recado        Recado.
     *
     * @see User
     */

    public Recado(User remetente, User destinatario, String recado) {
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.recado = recado;
    }

    /**
     * Retorna o remetente do recado.
     *
     * @return Remetente do recado.
     * 
     * @see User
     */

    public User getRemetente() {
        return this.remetente;
    }

    /**
     * Retorna o destinatário do recado.
     *
     * @return Destinatário do recado.
     *
     * @see User
     */

    public User getDestinatario() {
        return this.destinatario;
    }

    /**
     * Retorna o recado.
     *
     * @return Recado.
     */

    public String getRecado() {
        return this.recado;
    }
}
