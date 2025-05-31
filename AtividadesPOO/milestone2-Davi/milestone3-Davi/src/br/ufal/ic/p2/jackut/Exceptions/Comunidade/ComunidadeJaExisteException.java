package br.ufal.ic.p2.jackut.Exceptions.Comunidade;


/**
 * Exception que indica que uma comunidade com esse mesmo nome ja existe no sistema.
 */

public class ComunidadeJaExisteException extends RuntimeException {
    public ComunidadeJaExisteException() {
        super("Comunidade com esse nome já existe.");
    }
}
