package br.ufal.ic.p2.jackut.Exceptions.Comunidade;

/**
 * Exception que indica que o User já faz parte da comunidade.
 */

public class UsuarioJaNaComunidadeException extends RuntimeException {
    public UsuarioJaNaComunidadeException() {
        super("Usuario já faz parte dessa comunidade.");
    }
    
}
