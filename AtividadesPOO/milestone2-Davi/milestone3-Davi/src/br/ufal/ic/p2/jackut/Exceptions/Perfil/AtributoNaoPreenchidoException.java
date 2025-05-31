package br.ufal.ic.p2.jackut.Exceptions.Perfil;


/**
 * Exceção que indica que um atributo não foi preenchido.
 */

public class AtributoNaoPreenchidoException extends RuntimeException {
    public AtributoNaoPreenchidoException() {
        super("Atributo não preenchido.");
    }
}
