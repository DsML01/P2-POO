package br.ufal.ic.p2.jackut.Utilidade;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.ufal.ic.p2.jackut.Entidades.User;
import br.ufal.ic.p2.jackut.Servicos.JackutServicesFacade;
import br.ufal.ic.p2.jackut.Entidades.Mensagem;

import br.ufal.ic.p2.jackut.Exceptions.Comunidade.ComunidadeNaoExisteException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioEhInimigoException;

import br.ufal.ic.p2.jackut.Entidades.Comunidade;

import br.ufal.ic.p2.jackut.tipos.TiposRelacionamento;

/**
 * Classe com os métodos utilitários para a leitura de arquivos.
 */

public class LeituraDeArquivos {

    /**
     * Lê os arquivos do banco de dados carrega eles no sistema.
     *
     * @param jackutServicesFacade Sistema a ser carregado.
     */

    public static void lerArquivos(JackutServicesFacade jackutServicesFacade) {
        Map<String, String[]> comunidades = new HashMap<>();

        lerArquivo("usuarios", jackutServicesFacade, comunidades);
        lerArquivo("amigos", jackutServicesFacade, null);
        lerArquivo("recados", jackutServicesFacade, null);
        lerArquivo("comunidades", jackutServicesFacade, comunidades);
        lerArquivo("mensagens", jackutServicesFacade, null);
        lerArquivo("relacoes", jackutServicesFacade, null);
    }

    /**
     * Lê um arquivo genérico com o nome passado e o carrega no sistema.
     *
     * @param arquivo      Nome do arquivo.
     * @param jackutServicesFacade      Sistema a ser carregado.
     * @param comunidades  Mapa de comunidades.
     */

    public static void lerArquivo(String arquivo, JackutServicesFacade jackutServicesFacade, Map<String, String[]> comunidades) {
        File file = new File("./BaseDeDados/" + arquivo + ".txt");

        if (!file.exists()) return;

        String[] dados;
        String linha;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            while ((linha = br.readLine()) != null) {
                dados = linha.split(";");

                if(arquivo.equals("usuarios")) lerUsuarios(jackutServicesFacade, dados, comunidades);
                else if(arquivo.equals("amigos")) lerAmigos(jackutServicesFacade, dados);
                else if(arquivo.equals("recados")) lerRecados(jackutServicesFacade, dados);
                else if(arquivo.equals("comunidades")) lerComunidades(jackutServicesFacade, dados, comunidades);
                else if(arquivo.equals("mensagens")) lerMensagens(jackutServicesFacade, dados);
                else if(arquivo.equals("relacoes")) lerRelacoes(jackutServicesFacade, dados);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo " + arquivo);
        }
    }

    /**
     * Lê os usuários do arquivo "usuarios.txt".
     *
     * @param jackutServicesFacade      Sistema a ser carregado.
     * @param dados        Dados do arquivo.
     * @param comunidades  Mapa de comunidades.
     */

    private static void lerUsuarios(JackutServicesFacade jackutServicesFacade, String[] dados, Map<String, String[]> comunidades) {
        String login = dados[0];
        String senha = dados[1];
        String nome = "";

        if (dados.length > 2) {
            nome = dados[2];
        }

        User user = new User(login, senha, nome);

        for (int i = 3; i < dados.length - 1; i++) {
            String[] atributo = dados[i].split(":");
            user.getPerfil().setAtributo(atributo[0], atributo[1]);
        }

        String[] comunidadesUsuario = dados[dados.length - 1]
                .substring(1, dados[dados.length - 1].length() - 1)
                .split(",");

        comunidades.put(login, comunidadesUsuario);
        jackutServicesFacade.setUsuario(user);
    }

    /**
     * Lê os amigos dos usuários do arquivo "amigos.txt" e estabelece a relação de amizade de forma bidirecional.
     * A lógica foi atualizada para garantir que se A é amigo de B, B também se torna amigo de A durante o carregamento.
     *
     * @param jackutServicesFacade  Sistema a ser carregado.
     * @param dados    Dados do arquivo (linha atual).
     */
    private static void lerAmigos(JackutServicesFacade jackutServicesFacade, String[] dados) {
        // Pega o usuário principal da linha
        User user = jackutServicesFacade.getUsuario(dados[0]);

        // Verificação de segurança para evitar erro se a lista de amigos estiver vazia ou mal formatada
        if (dados.length < 2 || dados[1].length() <= 2) {
            return;
        }

        // Extrai os logins dos amigos da string "{amigo1,amigo2,...}"
        String[] amigosLogins = dados[1].substring(1, dados[1].length() - 1).split(",");

        for (String amigoLogin : amigosLogins) {
            // .trim() é adicionado para remover quaisquer espaços em branco acidentais
            String loginLimpo = amigoLogin.trim();
            if (loginLimpo.isEmpty()) {
                continue;
            }

            User amigo = jackutServicesFacade.getUsuario(loginLimpo);

            // Estabelece a amizade nos dois sentidos para garantir a consistência do estado.
            // Assumindo que o método em User foi renomeado de 'setAmigo' para 'adicionarAmigo' para maior clareza.

            // Adiciona 'amigo' à lista de amigos de 'user'
            user.adicionarAmigo(amigo);

            // PONTO CRÍTICO DA CORREÇÃO:
            // Adiciona 'user' à lista de amigos de 'amigo' para completar a relação
            amigo.adicionarAmigo(user);
        }
    }

    /**
     * Lê os recados dos usuários do arquivo "recados.txt".
     *
     * @param jackutServicesFacade  Sistema a ser carregado.
     * @param dados    Dados do arquivo.
     */

    private static void lerRecados(JackutServicesFacade jackutServicesFacade, String[] dados) {
        User user = jackutServicesFacade.getUsuario(dados[0]);
        User amigo = jackutServicesFacade.getUsuario(dados[1]);
        String recado = dados[2];

        try {
            jackutServicesFacade.enviarRecado(amigo, user, recado);
        } catch (UsuarioEhInimigoException e) {}
    }

    /**
     * Lê as comunidades dos usuários do arquivo "comunidades.txt".
     *
     * @param jackutServicesFacade      Sistema a ser carregado.
     * @param dados        Dados do arquivo.
     * @param comunidades  Mapa de comunidades.
     */

//    private static void lerComunidades(JackutServicesFacade jackutServicesFacade, String[] dados, Map<String, String[]> comunidades) {
//        User dono = jackutServicesFacade.getUsuario(dados[0]);
//        String nome = dados[1];
//        String descricao = dados[2];
//
//        Comunidade novaComunidade = new Comunidade(dono, nome, descricao);
//
//        dono.setDonoComunidade(novaComunidade);
//
//        String[] membros = dados[3].substring(1, dados[3].length() - 1).split(",");
//
//        for (String membro : membros) {
//            if (membro.equals(dono.getLogin())) {
//                continue;
//            }
//
//            novaComunidade.adicionarMembro(jackutServicesFacade.getUsuario(membro));
//        }
//
//        jackutServicesFacade.setComunidade(dono, nome, descricao);
//
//        try {
//            for (String login : comunidades.keySet()) {
//                User user = jackutServicesFacade.getUsuario(login);
//                for (String comunidade : comunidades.get(login)) {
//                    user.setParticipanteComunidade(jackutServicesFacade.getComunidade(comunidade));
//                }
//            }
//        } catch (ComunidadeNaoExisteException e) {}
//    }

//    private static void lerComunidades(JackutServicesFacade jackutServicesFacade, String[] dados, Map<String, String[]> comunidades) {
//        User dono = jackutServicesFacade.getUsuario(dados[0]);
//        String nome = dados[1];
//        String descricao = dados[2];
//
//        Comunidade novaComunidade = new Comunidade(dono, nome, descricao);
//
//        // O dono já foi adicionado como membro no construtor da Comunidade,
//        // e como participante em registrarNovaComunidade, então vamos garantir a consistência aqui também.
//        dono.setDonoComunidade(novaComunidade);
//        // Não precisa chamar dono.setParticipanteComunidade, pois a lógica final do método já fará isso.
//
//        String[] membros = dados[3].substring(1, dados[3].length() - 1).split(",");
//
//        for (String membroLogin : membros) {
//            if (membroLogin.isEmpty() || membroLogin.equals(dono.getLogin())) {
//                continue;
//            }
//            User membro = jackutServicesFacade.getUsuario(membroLogin);
//            novaComunidade.adicionarMembro(membro);
//        }
//
//        // --- CORREÇÃO AQUI ---
//        // ANTES (INCORRETO):
//        // jackutServicesFacade.setComunidade(dono, nome, descricao);
//
//        // DEPOIS (CORRETO):
//        jackutServicesFacade.carregarComunidade(novaComunidade);
//        // --- FIM DA CORREÇÃO ---
//
//
//        // Esta parte final do seu método agora funcionará corretamente,
//        // pois 'getComunidade' retornará o objeto completo que acabamos de carregar.
//        try {
//            for (String login : comunidades.keySet()) {
//                User user = jackutServicesFacade.getUsuario(login);
//                for (String comunidadeNome : comunidades.get(login)) {
//                    if(!comunidadeNome.trim().isEmpty()){
//                        Comunidade c = jackutServicesFacade.getComunidade(comunidadeNome);
//                        user.setParticipanteComunidade(c);
//                    }
//                }
//            }
//        } catch (ComunidadeNaoExisteException e) {
//            // Este erro não deve mais acontecer para comunidades válidas.
//        }
//    }

//    private static void lerComunidades(JackutServicesFacade jackutServicesFacade, String[] dados, Map<String, String[]> comunidades) {
//        User dono = jackutServicesFacade.getUsuario(dados[0]);
//        String nome = dados[1];
//        String descricao = dados[2];
//
//        Comunidade novaComunidade = new Comunidade(dono, nome, descricao);
//
//        // Membros
//        String[] membros = dados[3].substring(1, dados[3].length() - 1).split(",");
//        for (String membroLogin : membros) {
//            if (!membroLogin.trim().isEmpty() && !membroLogin.equals(dono.getLogin())) {
//                novaComunidade.adicionarMembro(jackutServicesFacade.getUsuario(membroLogin.trim()));
//            }
//        }
//
//        // Moderadores (novo campo)
//        if (dados.length > 4) {
//            String[] moderadores = dados[4].substring(1, dados[4].length() - 1).split(",");
//            for (String moderadorLogin : moderadores) {
//                if (!moderadorLogin.trim().isEmpty()) {
//                    novaComunidade.adicionarModerador(jackutServicesFacade.getUsuario(moderadorLogin.trim()));
//                }
//            }
//        }
//
//        jackutServicesFacade.carregarComunidade(novaComunidade);
//
//
//        // Esta parte final, que lê os dados de participação do arquivo usuarios.txt, permanece.
//        // Ela vai garantir que os membros (incluindo o dono) sejam corretamente associados
//        // às suas comunidades, e o User.setParticipanteComunidade() tem a trava anti-duplicatas.
//        try {
//            for (String login : comunidades.keySet()) {
//                User user = jackutServicesFacade.getUsuario(login);
//                for (String comunidadeNome : comunidades.get(login)) {
//                    if(!comunidadeNome.trim().isEmpty()){
//                        Comunidade c = jackutServicesFacade.getComunidade(comunidadeNome);
//                        // O método setParticipanteComunidade na classe User deve ter uma verificação
//                        // "if (list.contains(c)) return;" para ser 100% seguro.
//                        user.setParticipanteComunidade(c);
//                    }
//                }
//            }
//        } catch (ComunidadeNaoExisteException e) {
//            // Tratar exceção, se necessário
//        }
//    }

    private static void lerComunidades(JackutServicesFacade jackutServicesFacade, String[] dados, Map<String, String[]> comunidades) {
        User dono = jackutServicesFacade.getUsuario(dados[0]);
        String nome = dados[1];
        String descricao = dados[2];

        Comunidade novaComunidade = new Comunidade(dono, nome, descricao);

        // Membros (campo 3)
        String[] membros = dados[3].substring(1, dados[3].length() - 1).split(",");
        for (String membroLogin : membros) {
            if (!membroLogin.trim().isEmpty() && !membroLogin.equals(dono.getLogin())) {
                novaComunidade.adicionarMembro(jackutServicesFacade.getUsuario(membroLogin.trim()));
            }
        }

        // Moderadores (campo 4 - opcional)
        if (dados.length > 4 && !dados[4].isEmpty()) {
            String[] moderadores = dados[4].substring(1, dados[4].length() - 1).split(",");
            for (String moderadorLogin : moderadores) {
                if (!moderadorLogin.trim().isEmpty()) {
                    novaComunidade.adicionarModerador(jackutServicesFacade.getUsuario(moderadorLogin.trim()));
                }
            }
        }

        // Membros banidos (campo 5 - opcional)
        if (dados.length > 5 && !dados[5].isEmpty()) {
            String[] banidos = dados[5].substring(1, dados[5].length() - 1).split(",");
            for (String banidoLogin : banidos) {
                if (!banidoLogin.trim().isEmpty()) {
                    User usuarioBanido = jackutServicesFacade.getUsuario(banidoLogin.trim());
                    // Adiciona à lista de banidos sem verificar duplicatas (o Set já cuida disso)
                    novaComunidade.getMembrosBanidos().add(usuarioBanido);

                    // Remove da lista de membros ativos se estiver lá
                    novaComunidade.getMembros().remove(usuarioBanido);
                    novaComunidade.getModeradores().remove(usuarioBanido);
                }
            }
        }

        // Carrega a comunidade no sistema
        jackutServicesFacade.carregarComunidade(novaComunidade);

        // Associa a comunidade aos usuários (parte de persistência dos usuários)
        try {
            for (String login : comunidades.keySet()) {
                User user = jackutServicesFacade.getUsuario(login);
                for (String comunidadeNome : comunidades.get(login)) {
                    if (!comunidadeNome.trim().isEmpty()) {
                        try {
                            Comunidade c = jackutServicesFacade.getComunidade(comunidadeNome);

                            // Verifica se o usuário não está banido antes de adicionar
                            if (!c.getMembrosBanidos().contains(user)) {
                                user.setParticipanteComunidade(c);
                            }
                        } catch (ComunidadeNaoExisteException e) {
                            //System.err.println("Comunidade não encontrada: " + comunidadeNome);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao associar comunidades aos usuários: " + e.getMessage());
        }
    }

    /**
     * Lê as mensagens dos usuários do arquivo "mensagens.txt".
     *
     * @param jackutServicesFacade  Sistema a ser carregado.
     * @param dados    Dados do arquivo.
     */

    private static void lerMensagens(JackutServicesFacade jackutServicesFacade, String[] dados) {
        User user = jackutServicesFacade.getUsuario(dados[0]);
        String mensagem = dados[1];
        Mensagem msg = new Mensagem(mensagem);

        // ANTES: user.receberMensagem(msg);
        user.getCaixaDeEntrada().receberMensagem(msg); // DEPOIS
    }

    /**
     * Lê as relações dos usuários do arquivo "relacoes.txt".
     *
     * @param jackutServicesFacade  Sistema a ser carregado.
     * @param dados    Dados do arquivo.
     *
     * @see TiposRelacionamento
     */

    private static void lerRelacoes(JackutServicesFacade jackutServicesFacade, String[] dados) {
        User user = jackutServicesFacade.getUsuario(dados[0]);
        User userAlvo = jackutServicesFacade.getUsuario(dados[1]);
        TiposRelacionamento tipo = TiposRelacionamento.valueOf(dados[2]);

        switch (tipo) {
            case IDOLO:
                user.setIdolo(userAlvo);
                break;
            case FA:
                user.setFa(userAlvo);
                break;
            case PAQUERA:
                user.setPaquera(userAlvo);
                break;
            case PAQUERARECEBIDA:
                user.setPaquerasRecebidas(userAlvo);
                break;
            case INIMIGO:
                user.setInimigo(userAlvo);
                userAlvo.setInimigo(user);
                break;
        }
    }
}
