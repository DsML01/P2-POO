package br.ufal.ic.p2.jackut.Servicos;

import br.ufal.ic.p2.jackut.Entidades.Comunidade; // Necessário se EscritaDeArquivos.persistirDados os usa
import br.ufal.ic.p2.jackut.Entidades.User;    // Necessário se EscritaDeArquivos.persistirDados os usa
import br.ufal.ic.p2.jackut.Utilidade.EscritaDeArquivos;
import br.ufal.ic.p2.jackut.Utilidade.LeituraDeArquivos;

import java.util.Map;

public class PersistenceService {
    // Os mapas de dados são gerenciados pela JackutServicesFacade.
    // O PersistenceService os recebe para operações de salvar,
    // e o LeituraDeArquivos os popula através da instância da JackutServicesFacade.
    private final Map<String, User> usuariosData;
    private final Map<String, Comunidade> comunidadesData;
    private final JackutServicesFacade facadeContext; // Referência à Facade para o LeituraDeArquivos

    public PersistenceService(Map<String, User> usuariosData,
                              Map<String, Comunidade> comunidadesData,
                              JackutServicesFacade facadeContext) {
        this.usuariosData = usuariosData;
        this.comunidadesData = comunidadesData;
        this.facadeContext = facadeContext;
    }

    /**
     * Garante que a pasta de dados exista e chama LeituraDeArquivos
     * para popular os dados no sistema através da facadeContext.
     */
    public void carregarDadosIniciais() {
        EscritaDeArquivos.criarPasta(); // Garante que a pasta de dados existe

        // LeituraDeArquivos.lerArquivos espera uma instância de JackutServicesFacade
        // para chamar métodos como setUsuario, getUsuario, setComunidade, etc.,
        // populando assim os mapas que são mantidos na JackutServicesFacade.
        LeituraDeArquivos.lerArquivos(this.facadeContext);
    }

    /**
     * Salva os dados atuais dos mapas em arquivos, utilizando EscritaDeArquivos.
     */
    public void salvarDadosAtuais() {
        EscritaDeArquivos.criarPasta(); // Garante que a pasta de dados existe
        // EscritaDeArquivos.persistirDados usará os mapas passados aqui.
        EscritaDeArquivos.persistirDados(this.usuariosData, this.comunidadesData);
    }

    /**
     * Limpa o conteúdo dos arquivos de dados, utilizando EscritaDeArquivos.
     * Os mapas em memória são limpos pela JackutServicesFacade em seu método zerarSistema().
     */
    public void limparDadosPersistidos() {
        EscritaDeArquivos.limparArquivos();
    }
}