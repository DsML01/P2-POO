// Exceptions/Comunidade/UsuarioNaoBanidoException.java
package br.ufal.ic.p2.jackut.Exceptions.Comunidade;

public class UsuarioNaoBanidoException extends RuntimeException {
    public UsuarioNaoBanidoException() {
        super("Usuário não está banido desta comunidade.");
    }
}