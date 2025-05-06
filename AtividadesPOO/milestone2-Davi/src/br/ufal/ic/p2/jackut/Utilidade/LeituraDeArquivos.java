package br.ufal.ic.p2.jackut.Utilidade;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.ufal.ic.p2.jackut.Entidades.User;
import br.ufal.ic.p2.jackut.Servicos.SessionService;

import br.ufal.ic.p2.jackut.Exceptions.Comunidade.ComunidadeNaoExisteException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioEhInimigoException;

import br.ufal.ic.p2.jackut.Entidades.Comunidade;
import br.ufal.ic.p2.jackut.Entidades.Mensagem;

import br.ufal.ic.p2.jackut.tipos.TiposRelacionamento;

/**
 * Classe com os métodos utilitários para a leitura de arquivos.
 */

public class LeituraDeArquivos {

    /**
     * Lê os arquivos do banco de dados carrega eles no sistema.
     *
     * @param sessionService Sistema a ser carregado.
     */

    public static void lerArquivos(SessionService sessionService) {
        Map<String, String[]> comunidades = new HashMap<>();

        lerArquivo("usuarios", sessionService, comunidades);
        lerArquivo("amigos", sessionService, null);
        lerArquivo("recados", sessionService, null);
        lerArquivo("comunidades", sessionService, comunidades);
        lerArquivo("mensagens", sessionService, null);
        lerArquivo("relacoes", sessionService, null);
    }

    /**
     * Lê um arquivo genérico com o nome passado e o carrega no sistema.
     *
     * @param arquivo      Nome do arquivo.
     * @param sessionService      Sistema a ser carregado.
     * @param comunidades  Mapa de comunidades.
     */

    public static void lerArquivo(String arquivo, SessionService sessionService, Map<String, String[]> comunidades) {
        File file = new File("./BaseDeDados/" + arquivo + ".txt");

        if (!file.exists()) return;

        String[] dados;
        String linha;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            while ((linha = br.readLine()) != null) {
                dados = linha.split(";");

                if(arquivo.equals("usuarios")) lerUsuarios(sessionService, dados, comunidades);
                else if(arquivo.equals("amigos")) lerAmigos(sessionService, dados);
                else if(arquivo.equals("recados")) lerRecados(sessionService, dados);
                else if(arquivo.equals("comunidades")) lerComunidades(sessionService, dados, comunidades);
                else if(arquivo.equals("mensagens")) lerMensagens(sessionService, dados);
                else if(arquivo.equals("relacoes")) lerRelacoes(sessionService, dados);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo " + arquivo);
        }
    }

    /**
     * Lê os usuários do arquivo "usuarios.txt".
     *
     * @param sessionService      Sistema a ser carregado.
     * @param dados        Dados do arquivo.
     * @param comunidades  Mapa de comunidades.
     */

    private static void lerUsuarios(SessionService sessionService, String[] dados, Map<String, String[]> comunidades) {
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
        sessionService.setUsuario(user);
    }

    /**
     * Lê os amigos dos usuários do arquivo "amigos.txt".
     *
     * @param sessionService  Sistema a ser carregado.
     * @param dados    Dados do arquivo.
     */

    private static void lerAmigos(SessionService sessionService, String[] dados) {
        User user = sessionService.getUsuario(dados[0]);

        if (dados[1].length() <= 2) {
            return;
        }

        String[] amigos = dados[1].substring(1, dados[1].length() - 1).split(",");

        for (String amigo : amigos) {
            user.setAmigo(sessionService.getUsuario(amigo));
        }
    }

    /**
     * Lê os recados dos usuários do arquivo "recados.txt".
     *
     * @param sessionService  Sistema a ser carregado.
     * @param dados    Dados do arquivo.
     */

    private static void lerRecados(SessionService sessionService, String[] dados) {
        User user = sessionService.getUsuario(dados[0]);
        User amigo = sessionService.getUsuario(dados[1]);
        String recado = dados[2];

        try {
            sessionService.enviarRecado(amigo, user, recado);
        } catch (UsuarioEhInimigoException e) {}
    }

    /**
     * Lê as comunidades dos usuários do arquivo "comunidades.txt".
     *
     * @param sessionService      Sistema a ser carregado.
     * @param dados        Dados do arquivo.
     * @param comunidades  Mapa de comunidades.
     */

    private static void lerComunidades(SessionService sessionService, String[] dados, Map<String, String[]> comunidades) {
        User dono = sessionService.getUsuario(dados[0]);
        String nome = dados[1];
        String descricao = dados[2];

        Comunidade novaComunidade = new Comunidade(dono, nome, descricao);

        dono.setDonoComunidade(novaComunidade);

        String[] membros = dados[3].substring(1, dados[3].length() - 1).split(",");

        for (String membro : membros) {
            if (membro.equals(dono.getLogin())) {
                continue;
            }

            novaComunidade.adicionarMembro(sessionService.getUsuario(membro));
        }

        sessionService.setComunidade(nome, novaComunidade);

        try {
            for (String login : comunidades.keySet()) {
                User user = sessionService.getUsuario(login);
                for (String comunidade : comunidades.get(login)) {
                    user.setParticipanteComunidade(sessionService.getComunidade(comunidade));
                }
            }
        } catch (ComunidadeNaoExisteException e) {}
    }

    /**
     * Lê as mensagens dos usuários do arquivo "mensagens.txt".
     *
     * @param sessionService  Sistema a ser carregado.
     * @param dados    Dados do arquivo.
     */

    private static void lerMensagens(SessionService sessionService, String[] dados) {
        User user = sessionService.getUsuario(dados[0]);
        String mensagem = dados[1];

        Mensagem msg = new Mensagem(mensagem);

        user.receberMensagem(msg);
    }

    /**
     * Lê as relações dos usuários do arquivo "relacoes.txt".
     *
     * @param sessionService  Sistema a ser carregado.
     * @param dados    Dados do arquivo.
     *
     * @see TiposRelacionamento
     */

    private static void lerRelacoes(SessionService sessionService, String[] dados) {
        User user = sessionService.getUsuario(dados[0]);
        User userAlvo = sessionService.getUsuario(dados[1]);
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
