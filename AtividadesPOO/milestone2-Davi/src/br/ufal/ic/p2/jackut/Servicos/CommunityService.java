package br.ufal.ic.p2.jackut.Servicos;

import br.ufal.ic.p2.jackut.Entidades.Comunidade;
import br.ufal.ic.p2.jackut.Entidades.User;
import br.ufal.ic.p2.jackut.Exceptions.Comunidade.ComunidadeJaExisteException;
import br.ufal.ic.p2.jackut.Exceptions.Comunidade.ComunidadeNaoExisteException;
import br.ufal.ic.p2.jackut.Exceptions.Comunidade.UsuarioJaNaComunidadeException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioNaoRegistradoException;


import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;


public class CommunityService {
    private final Map<String, Comunidade> comunidadesData;
    private final UserService userService; // Para validar usuários, por exemplo

    public CommunityService(Map<String, Comunidade> comunidadesData, UserService userService) {
        this.comunidadesData = comunidadesData;
        this.userService = userService;
    }

    public void registrarNovaComunidade(User dono, String nome, String descricao) throws ComunidadeJaExisteException {
        if (this.comunidadesData.containsKey(nome)) {
            throw new ComunidadeJaExisteException();
        }
        Comunidade comunidade = new Comunidade(dono, nome, descricao);
        this.comunidadesData.put(nome, comunidade);
        dono.setDonoComunidade(comunidade);
        dono.setParticipanteComunidade(comunidade); // Adiciona explicitamente como participante
    }

    public Comunidade getComunidadePorNome(String nome) throws ComunidadeNaoExisteException {
        if (!this.comunidadesData.containsKey(nome)) {
            throw new ComunidadeNaoExisteException();
        }
        return this.comunidadesData.get(nome);
    }

    public void adicionarMembroComunidade(User usuario, Comunidade comunidade)
            throws UsuarioJaNaComunidadeException {

        // Verificação igual à versão antiga (mais confiável)
        if (usuario.getComunidadesParticipantes().contains(comunidade)) {
            throw new UsuarioJaNaComunidadeException();
        }

        // Mantém a verificação adicional por segurança
        if (comunidade.getMembros().contains(usuario)) {
            throw new UsuarioJaNaComunidadeException();
        }

        // Adição bidirecional (igual à versão antiga)
        comunidade.adicionarMembro(usuario);
        usuario.getComunidadesParticipantes().add(comunidade);

        // Remove a verificação redundante do dono (já resolvida no construtor da Comunidade)
    }

    // Em br/ufal/ic/p2/jackut/Servicos/CommunityService.java

    public void carregarComunidade(Comunidade comunidade) {
        if (!this.comunidadesData.containsKey(comunidade.getNome())) {
            this.comunidadesData.put(comunidade.getNome(), comunidade);
        }
    }

    public Collection<Comunidade> getAllComunidades() {
        return this.comunidadesData.values();
    }

    public void removerUsuarioDeTodasComunidades(User usuario) {
        for (Comunidade comunidade : this.comunidadesData.values()) {
            if (comunidade.getMembros().contains(usuario)) {
                comunidade.getMembros().remove(usuario); // Remove da lista de membros da comunidade
                usuario.sairComunidade(comunidade); // Remove da lista de comunidades do usuário
            }
            // Se o usuário for dono, a comunidade pode precisar ser removida ou ter um novo dono
            // A lógica atual de removerUsuario na Facade remove a comunidade se ele for o dono.
        }
    }

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