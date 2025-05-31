package br.ufal.ic.p2.jackut.Exceptions.Comunidade;


/**
 * Exceção que indica que a comunidade requisitada não existe no sistema.
 */

public class ComunidadeNaoExisteException extends RuntimeException {
    public ComunidadeNaoExisteException() {
        super("Comunidade não existe.");
    }
}
