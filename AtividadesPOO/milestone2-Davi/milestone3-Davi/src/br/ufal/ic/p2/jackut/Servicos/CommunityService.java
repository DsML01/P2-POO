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
     * @param comunidadesData Mapa para armazenar as comunidades existentes (Nome -> Comunidade)
     * @param userService Serviço de usuário para interações e validações
     */
    public CommunityService(Map<String, Comunidade> comunidadesData, UserService userService) {
        this.comunidadesData = comunidadesData;
        this.userService = userService;
    }

    /**
     * Cria e registra uma nova comunidade no sistema.
     * O dono é automaticamente adicionado como o primeiro membro.
     *
     * @param dono Usuário que será o dono da comunidade
     * @param nome Nome da comunidade (deve ser único)
     * @param descricao Descrição da comunidade
     * @throws ComunidadeJaExisteException se uma comunidade com o mesmo nome já existir
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
     * @param nome Nome da comunidade
     * @return Objeto Comunidade correspondente
     * @throws ComunidadeNaoExisteException se nenhuma comunidade com o nome especificado for encontrada
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
     * @param usuario Usuário a ser adicionado
     * @param comunidade Comunidade à qual o usuário se juntará
     * @throws UsuarioJaNaComunidadeException se o usuário já for membro da comunidade
     * @throws ModeradorException se o usuário estiver banido da comunidade
     */
    public void adicionarMembroComunidade(User usuario, Comunidade comunidade)
            throws UsuarioJaNaComunidadeException, ModeradorException {

        if (comunidade.getMembrosBanidos().contains(usuario)) {
            throw new ModeradorException("Usuário está banido desta comunidade.");
        }

        if (comunidade.getMembros().contains(usuario) ||
                usuario.getComunidadesParticipantes().contains(comunidade)) {
            throw new UsuarioJaNaComunidadeException();
        }

        comunidade.adicionarMembro(usuario);
        usuario.getComunidadesParticipantes().add(comunidade);
    }

    /**
     * Banir um membro de uma comunidade.
     *
     * @param executor Usuário que está executando a ação
     * @param comunidade Comunidade da qual o membro será banido
     * @param membro Membro a ser banido
     * @throws ModeradorException se o executor não tiver permissão ou tentar banir a si mesmo
     * @throws UsuarioNaoRegistradoException se o membro não for membro da comunidade
     */
    public void banirMembro(User executor, Comunidade comunidade, User membro)
            throws ModeradorException, UsuarioNaoRegistradoException {

        if (!comunidade.getDono().equals(executor) && !comunidade.isModerador(executor)) {
            throw new ModeradorException("Apenas o dono ou moderadores podem realizar esta ação.");
        }

        if (executor.equals(membro)) {
            throw new ModeradorException("Não é possível banir a si mesmo.");
        }

        if (!comunidade.isMembro(membro)) {
            throw new ModeradorException("Usuário não é membro da comunidade.");
        }

        comunidade.banirMembro(membro);
        membro.sairComunidade(comunidade);
    }

    /**
     * Desbanir um membro de uma comunidade.
     *
     * @param executor Usuário que está executando a ação
     * @param comunidade Comunidade da qual o membro será desbanido
     * @param membro Membro a ser desbanido
     * @throws ModeradorException se o executor não tiver permissão
     * @throws UsuarioNaoRegistradoException se o membro não estiver banido
     */
    public void desbanirMembro(User executor, Comunidade comunidade, User membro)
            throws ModeradorException, UsuarioNaoRegistradoException {

        if (!comunidade.getDono().equals(executor) && !comunidade.isModerador(executor)) {
            throw new ModeradorException("Apenas o dono ou moderadores podem realizar esta ação.");
        }

        comunidade.desbanirMembro(membro);
    }

    /**
     * Verifica se um usuário está banido de uma comunidade.
     *
     * @param user Usuário a ser verificado
     * @param comunidade Comunidade a ser verificada
     * @throws ModeradorException se o usuário estiver banido
     */
    public void verificarBanimento(User user, Comunidade comunidade) throws ModeradorException {
        if (comunidade.isBanido(user)) {
            throw new ModeradorException("Usuário está banido desta comunidade.");
        }
    }

    /**
     * Carrega um objeto de comunidade pré-existente no mapa de dados em memória.
     * Usado principalmente durante a inicialização do sistema a partir da persistência.
     *
     * @param comunidade Objeto Comunidade a ser carregado
     */
    public void carregarComunidade(Comunidade comunidade) {
        if (!this.comunidadesData.containsKey(comunidade.getNome())) {
            this.comunidadesData.put(comunidade.getNome(), comunidade);
        }
    }

    /**
     * Atribui um moderador a uma comunidade.
     *
     * @param user Usuário que está executando a ação
     * @param comunidade Comunidade que terá o novo moderador
     * @param moderador Usuário a ser tornado moderador
     * @throws ModeradorException se o executor não tiver permissão ou se o usuário já for moderador
     * @throws UsuarioNaoRegistradoException se o usuário não for membro da comunidade
     */
    public void atribuirModerador(User user, Comunidade comunidade, User moderador)
            throws ModeradorException, UsuarioNaoRegistradoException {

        if (!comunidade.getDono().equals(user) && !comunidade.isModerador(user)) {
            throw new ModeradorException("Apenas o dono ou moderadores podem realizar esta ação.");
        }

        if (!comunidade.getMembros().contains(moderador)) {
            throw new ModeradorException("Usuário não é membro da comunidade.");
        }

        if (comunidade.isModerador(moderador)) {
            throw new ModeradorException("Usuário já é moderador.");
        }

        comunidade.adicionarModerador(moderador);
    }

    /**
     * Retorna uma coleção com todas as comunidades registradas no sistema.
     *
     * @return Coleção de objetos Comunidade
     */
    public Collection<Comunidade> getAllComunidades() {
        return this.comunidadesData.values();
    }

    /**
     * Expulsa um membro de uma comunidade.
     *
     * @param executor Usuário que está executando a ação
     * @param comunidade Comunidade da qual o membro será expulso
     * @param membro Membro a ser expulso
     * @throws ModeradorException se o executor não tiver permissão ou se o membro não existir
     * @throws UsuarioNaoRegistradoException se o membro não for membro da comunidade
     */
    public void expulsarMembro(User executor, Comunidade comunidade, User membro)
            throws ModeradorException, UsuarioNaoRegistradoException {

        if (!comunidade.getDono().equals(executor) && !comunidade.isModerador(executor)) {
            throw new ModeradorException("Apenas o dono ou moderadores podem realizar esta ação.");
        }

        if (!comunidade.isMembro(membro)) {
            throw new ModeradorException("Usuário não é membro da comunidade.");
        }

        comunidade.removerMembro(membro);
        membro.sairComunidade(comunidade);
    }

    /**
     * Remove um usuário de todas as comunidades das quais ele participa.
     * Nota: A lógica para lidar com a remoção de uma comunidade se o usuário for o dono
     * é gerenciada em um nível superior (Facade).
     *
     * @param usuario Usuário a ser removido das comunidades
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
     * @param nomeComunidade Nome da comunidade a ser removida
     * @throws ComunidadeNaoExisteException se a comunidade não for encontrada
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