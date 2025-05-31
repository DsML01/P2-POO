package br.ufal.ic.p2.jackut.Servicos;

import br.ufal.ic.p2.jackut.Entidades.User;
import br.ufal.ic.p2.jackut.Exceptions.Sistema.LoginOuSenhaInvalidoException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioNaoRegistradoException;

import java.util.Map;
import java.util.UUID;

public class AuthenticationService {
    private final Map<String, User> sessoesData;
    private final UserService userService;

    public AuthenticationService(Map<String, User> sessoesData, UserService userService) {
        this.sessoesData = sessoesData;
        this.userService = userService;
    }

    public String login(String login, String senha) throws LoginOuSenhaInvalidoException {
        try {
            User user = this.userService.getUsuarioPorLogin(login);
            if (!user.verificarSenha(senha)) {
                throw new LoginOuSenhaInvalidoException("Login ou senha inválidos.");
            }
            String idSessao = UUID.randomUUID().toString();
            this.sessoesData.put(idSessao, user);
            return idSessao;
        } catch (UsuarioNaoRegistradoException e) {
            throw new LoginOuSenhaInvalidoException("Login ou senha inválidos.");
        }
    }

    public User getUsuarioDaSessao(String idSessao) throws UsuarioNaoRegistradoException {
        if (!this.sessoesData.containsKey(idSessao)) {
            throw new UsuarioNaoRegistradoException(); // Ou uma exceção de sessão
        }
        return this.sessoesData.get(idSessao);
    }

    public void invalidarSessaoPorId(String idSessao) {
        this.sessoesData.remove(idSessao);
    }

    public void invalidarTodasSessoesDeUsuario(User usuario) {
        this.sessoesData.values().removeIf(u -> u.equals(usuario));
    }
}