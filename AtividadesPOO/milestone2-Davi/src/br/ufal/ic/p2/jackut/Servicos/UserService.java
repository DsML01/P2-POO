package br.ufal.ic.p2.jackut.Servicos;

import br.ufal.ic.p2.jackut.Entidades.User;
import br.ufal.ic.p2.jackut.Exceptions.Sistema.ContaJaExisteException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioNaoRegistradoException;

import java.util.Collection;
import java.util.Map;

public class UserService {
    private final Map<String, User> usuariosData;

    public UserService(Map<String, User> usuariosData) {
        this.usuariosData = usuariosData;
    }

    public void registrarNovoUsuario(User usuario) throws ContaJaExisteException {
        if (this.usuariosData.containsKey(usuario.getLogin())) {
            throw new ContaJaExisteException();
        }
        this.usuariosData.put(usuario.getLogin(), usuario);
    }

    public User getUsuarioPorLogin(String login) throws UsuarioNaoRegistradoException {
        if (!this.usuariosData.containsKey(login)) {
            throw new UsuarioNaoRegistradoException();
        }
        return this.usuariosData.get(login);
    }

    public Collection<User> getAllUsers() {
        return this.usuariosData.values();
    }

    public void deletarUsuario(User usuario) throws UsuarioNaoRegistradoException {
        if (!this.usuariosData.containsKey(usuario.getLogin())) {
            throw new UsuarioNaoRegistradoException();
        }
        this.usuariosData.remove(usuario.getLogin());
    }

    // Outros métodos relacionados a usuário podem vir aqui (ex: atualizarPerfil)
}