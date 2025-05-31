// Exceptions/Comunidade/UsuarioNaoMembroException.java
package br.ufal.ic.p2.jackut.Exceptions.Comunidade;

public class UsuarioNaoMembroException extends RuntimeException {
    public UsuarioNaoMembroException() {
        super("Usuário não é membro da comunidade.");
    }
}