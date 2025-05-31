package br.ufal.ic.p2.jackut.Servicos;

import br.ufal.ic.p2.jackut.Entidades.User;
import br.ufal.ic.p2.jackut.Exceptions.Sistema.ContaJaExisteException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioNaoRegistradoException;

import java.util.Collection;
import java.util.Map;

/**
 * Serviço responsável por gerenciar o ciclo de vida dos usuários no sistema.
 * <p>
 * Esta classe lida com as operações de criação, recuperação e exclusão de
 * objetos {@link User} no mapa de dados principal.
 *
 * @author Davi
 */
public class UserService {
    private final Map<String, User> usuariosData;

    /**
     * Constrói uma nova instância de UserService.
     *
     * @param usuariosData O mapa que armazena os dados dos usuários (Login -> User).
     */
    public UserService(Map<String, User> usuariosData) {
        this.usuariosData = usuariosData;
    }

    /**
     * Registra um novo usuário no sistema.
     *
     * @param usuario O objeto {@link User} a ser adicionado.
     * @throws ContaJaExisteException se um usuário com o mesmo login já estiver registrado.
     */
    public void registrarNovoUsuario(User usuario) throws ContaJaExisteException {
        if (this.usuariosData.containsKey(usuario.getLogin())) {
            throw new ContaJaExisteException();
        }
        this.usuariosData.put(usuario.getLogin(), usuario);
    }

    /**
     * Busca e retorna um usuário pelo seu login.
     *
     * @param login O login do usuário a ser buscado.
     * @return O objeto {@link User} correspondente.
     * @throws UsuarioNaoRegistradoException se nenhum usuário com o login especificado for encontrado.
     */
    public User getUsuarioPorLogin(String login) throws UsuarioNaoRegistradoException {
        if (!this.usuariosData.containsKey(login)) {
            throw new UsuarioNaoRegistradoException();
        }
        return this.usuariosData.get(login);
    }

    /**
     * Retorna uma coleção com todos os usuários registrados no sistema.
     *
     * @return Uma {@link Collection} de objetos {@link User}.
     */
    public Collection<User> getAllUsers() {
        return this.usuariosData.values();
    }

    /**
     * Remove um usuário do sistema.
     *
     * @param usuario O usuário a ser deletado.
     * @throws UsuarioNaoRegistradoException se o usuário não for encontrado no sistema.
     */
    public void deletarUsuario(User usuario) throws UsuarioNaoRegistradoException {
        if (!this.usuariosData.containsKey(usuario.getLogin())) {
            throw new UsuarioNaoRegistradoException();
        }
        this.usuariosData.remove(usuario.getLogin());
    }
}