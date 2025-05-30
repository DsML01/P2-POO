package br.ufal.ic.p2.jackut.Servicos;

import br.ufal.ic.p2.jackut.Entidades.User;
import br.ufal.ic.p2.jackut.Exceptions.Sistema.LoginOuSenhaInvalidoException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioNaoRegistradoException;

import java.util.Map;
import java.util.UUID;

public class AuthenticationService {
    private final Map<String, User> sessoesData; // ID de sessão -> User
    private final UserService userService;

    public AuthenticationService(Map<String, User> sessoesData, UserService userService) {
        this.sessoesData = sessoesData;
        this.userService = userService;
    }

    public String login(String login, String senha) throws LoginOuSenhaInvalidoException, UsuarioNaoRegistradoException {
        User user = this.userService.getUsuarioPorLogin(login); // Lança UsuarioNaoRegistradoException se não existir

        if (!user.verificarSenha(senha)) {
            throw new LoginOuSenhaInvalidoException("Senha inválida."); // Mensagem mais específica
        }

        String idSessao = UUID.randomUUID().toString();
        this.sessoesData.put(idSessao, user);
        return idSessao;
    }

    public User getUsuarioDaSessao(String idSessao) throws UsuarioNaoRegistradoException { // Ou uma SessionNotFoundException
        if (!this.sessoesData.containsKey(idSessao)) {
            // Lançar uma exceção mais específica para sessão inválida/expirada seria melhor
            throw new UsuarioNaoRegistradoException();
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