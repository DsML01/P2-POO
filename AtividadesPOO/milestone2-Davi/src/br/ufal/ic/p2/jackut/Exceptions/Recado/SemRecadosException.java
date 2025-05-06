package br.ufal.ic.p2.jackut.Exceptions.Recado;


/**
 * Exception que indica que o User nao tem recados a serem lidos.
 */

public class SemRecadosException extends RuntimeException {
    public SemRecadosException() {
        super("Não há recados.");
    }
}
