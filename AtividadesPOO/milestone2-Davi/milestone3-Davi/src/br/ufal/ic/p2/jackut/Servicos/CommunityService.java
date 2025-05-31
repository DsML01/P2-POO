package br.ufal.ic.p2.jackut.Servicos;

import br.ufal.ic.p2.jackut.Entidades.Comunidade;
import br.ufal.ic.p2.jackut.Entidades.User;
import br.ufal.ic.p2.jackut.Exceptions.Comunidade.ComunidadeJaExisteException;
import br.ufal.ic.p2.jackut.Exceptions.Comunidade.ComunidadeNaoExisteException;
import br.ufal.ic.p2.jackut.Exceptions.Comunidade.UsuarioJaNaComunidadeException;
import br.ufal.ic.p2.jackut.Exceptions.Comunidade.*;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioNaoRegistradoException;

import java.util.Collection;
import java.util.Map;

/**
 * Serviço responsável por gerenciar a lógica de negócios para comunidades.
 * <p>
 * Esta classe lida com a criação, exclusão, busca e gerenciamento
 * de membros das comunidades do sistema.
 *
 * @author Davi
 */
public class CommunityService {
    private final Map<String, Comunidade> comunidadesData;
    private final UserService userService;

    /**
     * Constrói uma nova instância de CommunityService.
     *
     * @param comunidadesData Mapa para armazenar as comunidades existentes (Nome -> Comunidade).
     * @param userService     Serviço de usuário para interações e validações.
     */
    public CommunityService(Map<String, Comunidade> comunidadesData, UserService userService) {
        this.comunidadesData = comunidadesData;
        this.userService = userService;
    }

    /**
     * Cria e registra uma nova comunidade no sistema.
     * O dono é automaticamente adicionado como o primeiro membro.
     *
     * @param dono      O usuário que será o dono da comunidade.
     * @param nome      O nome da comunidade (deve ser único).
     * @param descricao A descrição da comunidade.
     * @throws ComunidadeJaExisteException se uma comunidade com o mesmo nome já existir.
     */
    public void registrarNovaComunidade(User dono, String nome, String descricao) throws ComunidadeJaExisteException {
        if (this.comunidadesData.containsKey(nome)) {
            throw new ComunidadeJaExisteException();
        }
        Comunidade comunidade = new Comunidade(dono, nome, descricao);
        this.comunidadesData.put(nome, comunidade);
        dono.setDonoComunidade(comunidade);
        dono.setParticipanteComunidade(comunidade);
    }

    /**
     * Busca e retorna uma comunidade pelo seu nome.
     *
     * @param nome O nome da comunidade.
     * @return O objeto {@link Comunidade} correspondente.
     * @throws ComunidadeNaoExisteException se nenhuma comunidade com o nome especificado for encontrada.
     */
    public Comunidade getComunidadePorNome(String nome) throws ComunidadeNaoExisteException {
        if (!this.comunidadesData.containsKey(nome)) {
            throw new ComunidadeNaoExisteException();
        }
        return this.comunidadesData.get(nome);
    }

    /**
     * Adiciona um usuário como membro de uma comunidade.
     * A adição é bidirecional: o usuário é adicionado à lista de membros da comunidade,
     * e a comunidade é adicionada à lista de participações do usuário.
     *
     * @param usuario    O usuário a ser adicionado.
     * @param comunidade A comunidade à qual o usuário se juntará.
     * @throws UsuarioJaNaComunidadeException se o usuário já for membro da comunidade.
     */
    public void adicionarMembroComunidade(User usuario, Comunidade comunidade)
            throws UsuarioJaNaComunidadeException {

        if (usuario.getComunidadesParticipantes().contains(comunidade) || comunidade.getMembros().contains(usuario)) {
            throw new UsuarioJaNaComunidadeException();
        }

        comunidade.adicionarMembro(usuario);
        usuario.getComunidadesParticipantes().add(comunidade);
    }

    /**
     * Carrega um objeto de comunidade pré-existente no mapa de dados em memória.
     * Usado principalmente durante a inicialização do sistema a partir da persistência.
     *
     * @param comunidade O objeto {@link Comunidade} a ser carregado.
     */
    public void carregarComunidade(Comunidade comunidade) {
        if (!this.comunidadesData.containsKey(comunidade.getNome())) {
            this.comunidadesData.put(comunidade.getNome(), comunidade);
        }
    }

    public void atribuirModerador(User user, Comunidade comunidade, User moderador)
            throws ModeradorException, UsuarioNaoRegistradoException {

        // Verifica se o usuário tem permissão (dono ou moderador)
        if (!comunidade.getDono().equals(user) && !comunidade.isModerador(user)) {
            throw new ModeradorException("Apenas o dono ou moderadores podem realizar esta ação.");
        }

        // Verifica se o alvo é membro da comunidade
        if (!comunidade.getMembros().contains(moderador)) {
            throw new ModeradorException("Usuário não é membro da comunidade.");
        }

        // Verifica se já é moderador
        if (comunidade.isModerador(moderador)) {
            throw new ModeradorException("Usuário já é moderador.");
        }

        comunidade.adicionarModerador(moderador);
    }

    /**
     * Retorna uma coleção com todas as comunidades registradas no sistema.
     *
     * @return Uma {@link Collection} de objetos {@link Comunidade}.
     */
    public Collection<Comunidade> getAllComunidades() {
        return this.comunidadesData.values();
    }

    public void expulsarMembro(User executor, Comunidade comunidade, User membro)
            throws ModeradorException, UsuarioNaoRegistradoException {

        // Verifica permissões (dono ou moderador)
        if (!comunidade.getDono().equals(executor) && !comunidade.isModerador(executor)) {
            throw new ModeradorException("Apenas o dono ou moderadores podem realizar esta ação.");
        }

        // Verifica se o usuário é membro
        if (!comunidade.isMembro(membro)) {
            throw new ModeradorException("Usuário não é membro da comunidade.");
        }

        // Remove o membro
        comunidade.removerMembro(membro);

        // Remove das comunidades do usuário
        membro.sairComunidade(comunidade);
    }

    /**
     * Remove um usuário de todas as comunidades das quais ele participa.
     * Nota: A lógica para lidar com a remoção de uma comunidade se o usuário for o dono
     * é gerenciada em um nível superior (Facade).
     *
     * @param usuario O usuário a ser removido das comunidades.
     */
    public void removerUsuarioDeTodasComunidades(User usuario) {
        for (Comunidade comunidade : this.comunidadesData.values()) {
            if (comunidade.getMembros().contains(usuario)) {
                comunidade.getMembros().remove(usuario);
                usuario.sairComunidade(comunidade);
            }
        }
    }

    /**
     * Remove permanentemente uma comunidade do sistema e desvincula todos os seus membros.
     *
     * @param nomeComunidade O nome da comunidade a ser removida.
     * @throws ComunidadeNaoExisteException se a comunidade não for encontrada.
     */
    public void removerComunidade(String nomeComunidade) throws ComunidadeNaoExisteException {
        if (!this.comunidadesData.containsKey(nomeComunidade)) {
            throw new ComunidadeNaoExisteException();
        }
        Comunidade comunidadeRemovida = this.comunidadesData.remove(nomeComunidade);
        if (comunidadeRemovida != null) {
            for (User membro : comunidadeRemovida.getMembros()) {
                membro.sairComunidade(comunidadeRemovida);
            }
        }
    }
}