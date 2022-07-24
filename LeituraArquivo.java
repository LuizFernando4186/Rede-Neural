import java.io.IOException;
import java.util.*;
import java.io.*;

public class LeituraArquivo {

  private static Scanner entrada;
  static List<Letras> todasLetras = new ArrayList<>();

  // Método para abrir o arquivo arquivo.csv
  public static List<Letras> dadosCarregados(String dir) {

    List<Letras> listaPronta = new ArrayList<>();

    // Passo o diretorio que sera lido, tem dois pacotes teste e treinamento
    File diretorio = new File(dir);

    System.out.println("\nARQUIVOS CARREGADOS DO CONJUNTO: " + dir);
    
    for (File file : diretorio.listFiles()) {

      System.out.println(file);

      try {

        entrada = new Scanner(file);
        listaPronta = lerDados();// metodo q le os dados do arquivo
        fecharArquivo();
        
      }
      
      catch (IOException erroES) {
        System.err.println("Erro ao abrir o arquivo. Finalizando.");
        System.exit(1);// terminar o programa
      }

    }

    return listaPronta;
  }

  // Metodo para ler os registros do arquivo
  public static List<Letras> lerDados() throws IOException {

    List<String[]> lista = new ArrayList<>();

    try {
      // enquanto houver dados para ler, mostrar os registros
      while (entrada.hasNext()) {

        String linha = entrada.nextLine();
        lista.add(linha.split(","));

      }

    } catch (NoSuchElementException erroElemento) {
      System.err.println("Arquivo com problemas. Finalizando.");
    } catch (IllegalStateException erroEstado) {
      System.err.println("Erro ao ler o arquivo. Finalizando.");
    }

    return formatandoParaInt(lista);

  }// fim do método lerDados

  // Metodo para converter os array de String em Int
  public static List<Letras> formatandoParaInt(List<String[]> array) {

    String repreLetra = "";
    int j = 0;
    try {
      // Sera passado 21 de cada arquivo
      for (int i = 0; i < array.size(); i++) {
        List<Integer> dadosFormatados = new ArrayList<>();// dados de entradas
        int[] dadosLetra = new int[7];// vetor contendo a representacao da letra no arquivo
        // percorre cada vetor e pega seus elementos
        for (String n : array.get(i)) {
          j++;

          if (j > (array.get(i).length - 7)) {
            repreLetra = repreLetra.concat(n);// concatenar para pegar a representacao da letra
            dadosLetra[((j - (array.get(i).length - 6)))] = Integer.parseInt(n);

          }

          else
            dadosFormatados.add(Integer.parseInt(n));
        }

        j = Integer.parseInt(repreLetra);// transformando em int e comparando com a classe LetrasEnum.java
        LetrasEnum.getEnum(j).setDadosLetra(dadosLetra);
        todasLetras.add(new Letras(dadosFormatados, LetrasEnum.getEnum(j)));// adiciona cada letra na lista

        j = 0;
        repreLetra = "";

      }
    }

    catch (Exception e) { // Se houver erro na conversao
      System.out.println("ERRO NA CONVERSAO");
    }

    return todasLetras;

  }

  // Metodo para fechar o arquivo
  public static void fecharArquivo() throws IOException {
    if (entrada != null)
      entrada.close();
  }

  // Normalizar pesos
  // X = (X - X min) / (X max - X min)
  public static void normalizarPesos(List<Double> l) {

    for (int i = 0; i < l.size(); i++) {
      double valorNormalizado = (double) (l.get(i) - Collections.min(l)) / (Collections.max(l) - Collections.min(l));

      l.set(i, valorNormalizado);

    }
  }

}