import java.io.*;
import java.util.*;

public class Log {

  static int[][] matrizDeConfusao = new int[7][7];
  static List<String> acumuladorLog = new ArrayList<>();//Usado para acumular em RAM e descarregar depois melhorando o desempenho

  //////////////////////////////////
  //           GRAVAÇÃO           //
  //////////////////////////////////

  static void gravarArquivoLog(String nomeArquivo, String log) {

    try {

      RandomAccessFile arq = new RandomAccessFile(nomeArquivo, "rw");

      Writer csv = new BufferedWriter(new FileWriter(nomeArquivo, true));
      csv.append(log);
      csv.close();
      arq.close();

    } catch (IOException e) {
      System.out.println("!!! ERRO NA GRAVACAO DO LOG !!!");
      e.printStackTrace();
    }

  }

  static void gravarMatrizConfusao(String log) {
    gravarArquivoLog("matrizDeConfusao.txt", log);
  }

  static void gravarParametrosIniciais(String log) {
    gravarArquivoLog("parametrosIniciais.txt", log + "\n");
  }

  static void gravarPesosIniciais(String log) {
    gravarArquivoLog("pesosIniciais.txt", log);
  }

  static void gravarPesosFinais(String log) {
    gravarArquivoLog("pesosFinais.txt", log);
  }

  static void gravarGabarito(String log) {
    gravarArquivoLog("gabarito.txt", log);
  }



  static void gravarErroInteracao(String log) throws IOException {

    acumuladorLog.add(log);

    if (acumuladorLog.size() >= 10000){

      String temp = String.join("", acumuladorLog);

      gravarArquivoLog("erroDaInteracao.txt", temp);
      acumuladorLog.clear();
      temp = "";
    }

  }

  static void gravarSaidaFinal(String log) {
    gravarArquivoLog("saidaFinal.txt", log);
  }

  
  static void apagarLog() {

    String[] nomes = { "parametrosIniciais.txt", "matrizDeConfusao.txt", "pesosIniciais.txt", "pesosFinais.txt",
        "erroDaInteracao.txt", "saidaFinal.txt" , "gabarito.txt"};

    for (String nome : nomes) {
      File file = new File(nome);
      if (file.exists())
        file.delete();
    }

  }

  /*
   * Valor esperado sao as linhas
   * Valor real sao as colunas
   */
  static int total = 0;

  public static void matrizDeConfusaoAtt(int esperado, int real) {
    matrizDeConfusao[real][esperado] += 1;
    total++;
  }

  public static void imprimeMatrizConfusao() {

    for (int l = 0; l < matrizDeConfusao.length; l++) {
      for (int c = 0; c < matrizDeConfusao[0].length; c++) {
        gravarMatrizConfusao(matrizDeConfusao[l][c] + "    "); // imprime caracter a caracter
      }
      gravarMatrizConfusao("\n\n"); // muda de linha
    }

    gravarMatrizConfusao("\n" + "Quantidade total de interacoes: " + total);
  }

  public static void gravarPesos(List<Double> pesos, boolean a) {

    for (double p : pesos) {

      if (a)
        gravarPesosIniciais(p + ";");
      else
        gravarPesosFinais(p + ";");

    }

    if (a)
      gravarPesosIniciais("\n");
    else
      gravarPesosFinais("\n");

  }

}