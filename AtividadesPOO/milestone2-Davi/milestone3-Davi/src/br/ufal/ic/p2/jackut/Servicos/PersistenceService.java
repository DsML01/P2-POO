package br.ufal.ic.p2.jackut.Servicos;

import br.ufal.ic.p2.jackut.Entidades.Comunidade;
import br.ufal.ic.p2.jackut.Entidades.User;
import br.ufal.ic.p2.jackut.Utilidade.EscritaDeArquivos;
import br.ufal.ic.p2.jackut.Utilidade.LeituraDeArquivos;

import java.util.Map;

/**
 * Serviço responsável por gerenciar a persistência dos dados do sistema.
 * <p>
 * Esta classe abstrai as operações de leitura e escrita de dados em arquivos,
 * atuando como um intermediário entre os dados em memória (gerenciados pela
 * {@link JackutServicesFacade}) e as classes utilitárias de I/O.
 *
 * @author Davi
 */
public class PersistenceService {
    private final Map<String, User> usuariosData;
    private final Map<String, Comunidade> comunidadesData;
    private final JackutServicesFacade facadeContext;

    /**
     * Constrói uma nova instância de PersistenceService.
     *
     * @param usuariosData      O mapa de usuários em memória.
     * @param comunidadesData   O mapa de comunidades em memória.
     * @param facadeContext     A instância da fachada principal, usada para popular os dados durante a leitura.
     */
    public PersistenceService(Map<String, User> usuariosData,
                              Map<String, Comunidade> comunidadesData,
                              JackutServicesFacade facadeContext) {
        this.usuariosData = usuariosData;
        this.comunidadesData = comunidadesData;
        this.facadeContext = facadeContext;
    }

    /**
     * Carrega os dados iniciais do sistema a partir dos arquivos de persistência.
     * Garante que o diretório de dados exista e delega a leitura dos arquivos
     * para a classe {@link LeituraDeArquivos}.
     */
    public void carregarDadosIniciais() {
        EscritaDeArquivos.criarPasta();
        LeituraDeArquivos.lerArquivos(this.facadeContext);
    }

    /**
     * Salva o estado atual dos dados em memória para os arquivos de persistência.
     * Delega a escrita dos dados para a classe {@link EscritaDeArquivos}.
     */
    public void salvarDadosAtuais() {
        EscritaDeArquivos.criarPasta();
        EscritaDeArquivos.persistirDados(this.usuariosData, this.comunidadesData);
    }

    /**
     * Limpa o conteúdo de todos os arquivos de dados persistidos.
     * Esta operação não afeta os dados atualmente em memória.
     */
    public void limparDadosPersistidos() {
        EscritaDeArquivos.limparArquivos();
    }
}