package br.ufal.ic.p2.jackut.Exceptions.Comunidade;


/**
 * Exception para indicar que nao ha mensagens na comunidade.
 */

public class SemMensagensException extends Exception {
    public SemMensagensException() {
        super("Não há mensagens.");
    }
}
