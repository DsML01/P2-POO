package br.ufal.ic.p2.jackut.Utilidade;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import br.ufal.ic.p2.jackut.Entidades.Comunidade;
import br.ufal.ic.p2.jackut.Entidades.Mensagem;
import br.ufal.ic.p2.jackut.Entidades.Recado;
import br.ufal.ic.p2.jackut.Entidades.User;

import static br.ufal.ic.p2.jackut.tipos.TiposRelacionamento.*;
import br.ufal.ic.p2.jackut.tipos.TiposRelacionamento;


/**
 * Classe com métodos utilitários para a escrita de arquivos.
 */

public class EscritaDeArquivos {

    /**
     * Cria a pasta BaseDeDados caso ela não exista.
     */

    public static void criarPasta() {
        String caminho = "./BaseDeDados";

        File diretorio = new File(caminho);

        if (!diretorio.exists()) {
            diretorio.mkdir();
        }
    }

    /**
     * Escreve um arquivo genérico com o nome e conteúdo passados.
     *
     * @param arquivo  Nome do arquivo.
     * @param conteudo     Conteúdo do arquivo.
     */

    public static void escreverArquivo(String arquivo, String conteudo) {
        try{
            File file = new File("./BaseDeDados/" + arquivo);
            file.createNewFile();

            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(conteudo);
            bw.flush();
            bw.close();
            fw.close();
        } catch (IOException e) {
            System.out.println("Erro ao escrever o arquivo " + arquivo);
        }
    }

    /**
     * Salva os usuários no arquivo "usuarios.txt".
     *
     * @param usuarios  Map com os usuários.
     */

    public static void salvarUsuarios(Map<String, User> usuarios) {
        StringBuilder usuariosData = new StringBuilder();
        for (User user : usuarios.values()) {
            usuariosData.append(user.getLogin()).append(";")
                    .append(user.getSenha()).append(";")
                    .append(user.getNome()).append(";");

            for (String atributo : user.getPerfil().getAtributos().keySet()) {
                usuariosData.append(atributo).append(":")
                        .append(user.getPerfil().getAtributo(atributo)).append(";");
            }

            usuariosData.append(UtilidadeString.formatArrayList(user.getComunidadesParticipantes())).append("\n");
        }

        escreverArquivo("usuarios.txt", usuariosData.toString());
    }

    /**
     * Salva os amigos dos usuários no arquivo "amigos.txt".
     *
     * @param usuarios  Map com os usuários.
     */

    public static void salvarAmigos(Map<String, User> usuarios) {
        StringBuilder amigosData = new StringBuilder();
        for (User user : usuarios.values()) {
            String amigos = user.getAmigosString();
            amigosData.append(user.getLogin()).append(";").append(amigos).append("\n");
        }

        escreverArquivo("amigos.txt", amigosData.toString());
    }

    /**
     * Salva os recados dos usuários no arquivo "recados.txt".
     *
     * @param usuarios  Map com os usuários.
     */

    public static void salvarRecados(Map<String, User> usuarios) {
        StringBuilder recadosData = new StringBuilder();
        for (User user : usuarios.values()) {
            for (Recado recado : user.getRecados()) {
                recadosData.append(user.getLogin()).append(";")
                        .append(recado.getRemetente().getLogin()).append(";")
                        .append(recado.getRecado()).append("\n");
            }
        }

        escreverArquivo("recados.txt", recadosData.toString());
    }

    /**
     * Salva as comunidades dos usuários no arquivo "comunidades.txt".
     *
     * @param comunidades  Map com as comunidades.
     */

    public static void salvarComunidades(Map<String, Comunidade> comunidades) {
        StringBuilder comunidadesData = new StringBuilder();
        for (Comunidade comunidade : comunidades.values()) {
            String membros = comunidade.getMembrosString();
            comunidadesData.append(comunidade.getDono().getLogin()).append(";")
                    .append(comunidade.getNome()).append(";")
                    .append(comunidade.getDescricao()).append(";")
                    .append(membros).append("\n");
        }

        escreverArquivo("comunidades.txt", comunidadesData.toString());
    }

    /**
     * Salva as mensagens dos usuários no arquivo "mensagens.txt".
     *
     * @param usuarios   Map com os usuários.
     */

    public static void salvarMensagens(Map<String, User> usuarios) {
        StringBuilder mensagensData = new StringBuilder();
        for (User user : usuarios.values()) {
            for (Mensagem mensagem : user.getMensagens()) {
                mensagensData.append(user.getLogin()).append(";")
                        .append(mensagem.getMensagem()).append("\n");
            }
        }

        escreverArquivo("mensagens.txt", mensagensData.toString());
    }

    /**
     * Salva as relações dos usuários no arquivo "relacoes.txt".
     *
     * @param usuarios  Map com os usuários.
     *
     * @see TiposRelacionamento
     */

    public static void salvarRelacoes(Map<String, User> usuarios) {
        StringBuilder relacoesData = new StringBuilder();
        for (User user : usuarios.values()) {
            for (User idolo : user.getIdolos()) {
                relacoesData.append(user.getLogin()).append(";")
                        .append(idolo.getLogin()).append(";")
                        .append(IDOLO).append("\n");
            }

            for (User fa : user.getFas()) {
                relacoesData.append(user.getLogin()).append(";")
                        .append(fa.getLogin()).append(";")
                        .append(FA).append("\n");
            }

            for (User paquera : user.getPaqueras()) {
                relacoesData.append(user.getLogin()).append(";")
                        .append(paquera.getLogin()).append(";")
                        .append(PAQUERA).append("\n");
            }

            for (User paquerasRecebidas : user.getPaquerasRecebidas()) {
                relacoesData.append(user.getLogin()).append(";")
                        .append(paquerasRecebidas.getLogin()).append(";")
                        .append(PAQUERARECEBIDA).append("\n");
            }

            for (User inimigos : user.getInimigos()) {
                relacoesData.append(user.getLogin()).append(";")
                        .append(inimigos.getLogin()).append(";")
                        .append(INIMIGO).append("\n");
            }
        }

        escreverArquivo("relacoes.txt", relacoesData.toString());
    }

    /**
     * Persiste os dados, salvando-os em arquivos.
     *
     * @param usuarios      Map com os usuários.
     * @param comunidades   Map com as comunidades.
     */

    public static void persistirDados(Map<String, User> usuarios, Map<String, Comunidade> comunidades) {
        salvarUsuarios(usuarios);
        salvarAmigos(usuarios);
        salvarRecados(usuarios);
        salvarComunidades(comunidades);
        salvarMensagens(usuarios);
        salvarRelacoes(usuarios);
    }

    /**
     * Carrega os usuários do arquivo "usuarios.txt".
     */

    public static void limparArquivos() {
        escreverArquivo("usuarios.txt", "");
        escreverArquivo("amigos.txt", "");
        escreverArquivo("recados.txt", "");
        escreverArquivo("comunidades.txt", "");
        escreverArquivo("mensagens.txt", "");
        escreverArquivo("relacoes.txt", "");
    }
}
