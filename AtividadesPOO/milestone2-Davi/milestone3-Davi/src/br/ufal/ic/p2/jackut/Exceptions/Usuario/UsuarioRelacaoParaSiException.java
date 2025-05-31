package br.ufal.ic.p2.jackut.Exceptions.Usuario;


/**
 * Exceção que indica que o User não pode se relacionar consigo mesmo.
 */

public class UsuarioRelacaoParaSiException extends RuntimeException {

    /**
     *
     * Se deve ter uma destas relacoes
     *  Amigo
     *  Ídolo
     *  Paquera
     *  Inimigo
     *
     * @param relacao Relação que está sendo adicionada.
     */

    public UsuarioRelacaoParaSiException(String relacao) {super(relacao.toLowerCase().equals("amizade")?"Usuário não pode adicionar a si mesmo como amigo.":"Usuário não pode ser " + relacao + " de si mesmo.");}
}
