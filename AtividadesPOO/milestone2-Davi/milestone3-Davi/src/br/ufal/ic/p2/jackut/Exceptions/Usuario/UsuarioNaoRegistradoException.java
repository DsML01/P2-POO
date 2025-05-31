package br.ufal.ic.p2.jackut.Exceptions.Usuario;


/**
 * Exception que indica que o User não está cadastrado no sistema.
 */

public class UsuarioNaoRegistradoException extends RuntimeException {
    public UsuarioNaoRegistradoException() {
        super("Usuário não cadastrado.");
    }
}
