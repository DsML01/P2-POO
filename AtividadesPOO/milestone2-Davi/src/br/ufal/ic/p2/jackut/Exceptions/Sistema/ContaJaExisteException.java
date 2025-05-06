package br.ufal.ic.p2.jackut.Exceptions.Sistema;


/**
 * Exception que indica que uma conta com esse nome ja esta cadastrada no sistema.
 */

public class ContaJaExisteException extends RuntimeException {
    public ContaJaExisteException() {
        super("Conta com esse nome já existe.");
    }
}
