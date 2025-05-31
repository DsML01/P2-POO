package br.ufal.ic.p2.jackut.Servicos;

import br.ufal.ic.p2.jackut.Entidades.User;
import br.ufal.ic.p2.jackut.Exceptions.Sistema.LoginOuSenhaInvalidoException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioNaoRegistradoException;

import java.util.Map;
import java.util.UUID;

/**
 * Serviço responsável por gerenciar a autenticação de usuários e as sessões ativas.
 * <p>
 * Esta classe lida com o processo de login, validação de credenciais,
 * criação, recuperação e invalidação de sessões de usuários.
 *
 * @author Davi
 */
public class AuthenticationService {
    private final Map<String, User> sessoesData;
    private final UserService userService;

    /**
     * Constrói uma nova instância de AuthenticationService.
     *
     * @param sessoesData Mapa para armazenar as sessões ativas (ID da sessão -> Usuário).
     * @param userService Serviço de usuário para buscar informações dos usuários.
     */
    public AuthenticationService(Map<String, User> sessoesData, UserService userService) {
        this.sessoesData = sessoesData;
        this.userService = userService;
    }

    /**
     * Tenta autenticar um usuário e criar uma nova sessão.
     *
     * @param login O login do usuário.
     * @param senha A senha do usuário.
     * @return Uma string com o ID da sessão única criada em caso de sucesso.
     * @throws LoginOuSenhaInvalidoException se as credenciais forem inválidas ou o usuário não existir.
     */
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

    /**
     * Obtém o objeto {@link User} associado a um ID de sessão ativo.
     *
     * @param idSessao O ID da sessão a ser validada.
     * @return O objeto {@link User} correspondente à sessão.
     * @throws UsuarioNaoRegistradoException se o ID da sessão for inválido ou não existir.
     */
    public User getUsuarioDaSessao(String idSessao) throws UsuarioNaoRegistradoException {
        if (!this.sessoesData.containsKey(idSessao)) {
            throw new UsuarioNaoRegistradoException();
        }
        return this.sessoesData.get(idSessao);
    }

    /**
     * Invalida uma sessão específica pelo seu ID, efetivamente desconectando o usuário.
     *
     * @param idSessao O ID da sessão a ser removida.
     */
    public void invalidarSessaoPorId(String idSessao) {
        this.sessoesData.remove(idSessao);
    }

    /**
     * Invalida todas as sessões ativas de um determinado usuário.
     *
     * @param usuario O usuário cujas sessões serão removidas.
     */
    public void invalidarTodasSessoesDeUsuario(User usuario) {
        this.sessoesData.values().removeIf(u -> u.equals(usuario));
    }
}