package br.ufal.ic.p2.jackut.Exceptions.Sistema;

public class LoginOuSenhaInvalidoException extends RuntimeException {

    /**
     * Exceção que indica que o login ou senha informados não são validos.
     *
     * @param type Tipo de exceção a ser lançada.
     */

    public LoginOuSenhaInvalidoException(String type) {
        super(type.equals("login") ? "Login inválido." : type.equals("senha") ? "Senha inválida." : "Login ou senha inválidos.");
    }
}

