package br.ufal.ic.p2.jackut.Exceptions.Usuario;

public class UsuarioEhInimigoException extends Exception {

    /**
     * Exceção para quando o User eh inimigo de outro User.
     * 
     * @param usuario Usuario que é inimigo do usuário logado.
     */

    public UsuarioEhInimigoException(String usuario) {
        super("Função inválida: " + usuario + " é seu inimigo.");
    }
}
