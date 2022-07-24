import java.io.IOException;

class Main {

  private static double taxa = 0.1;
  private static int quantidadeNeuroniosOculta = 10;
  
  public static void main(String[] args) {
    
    try {
      
      Log.apagarLog();
      Log.gravarParametrosIniciais("NeuroniosOculta;taxaAprendizado;epoca;funcaoAtivacao");
      Log.gravarErroInteracao("erro;epoca;taxaAprendizado;quantidadeNeuroniosOculta;conjunto\n");

      if (args.length == 0) executaSimulacaoTotal();// Primeiro se passar argumento quando for compilar, e executado a simulacao
      else executaUmaSimulacao();// Se nao passar e executado um teste padrao
                            

    } catch (Exception e) {
      System.out.println("ERRO NA EXECUCAO DO CLASSIFICADOR!!!\n");
      e.printStackTrace();
    }

    System.out.println("\n----------------------------FIMMM-----------------------------\n");

  }

  public static void executaSimulacaoTotal() throws IOException {
    
    for (int i = 0; i < 9; i++, taxa += 0.1) {

      for (int j = 0; j < 9; j++, quantidadeNeuroniosOculta += 5) {

        System.out.println("\n----------------------------SIMULACAO-----------------------------\n");
        System.out.println("TAXA DE APRENDIZADO: " + taxa + " - QTD NEURONIOS OCULTOS: " + quantidadeNeuroniosOculta);

        Log.gravarGabarito("\nTAXA DE APRENDIZADO: " + taxa + " - QTD NEURONIOS OCULTOS: " + quantidadeNeuroniosOculta + "\n");

        Simulacao simula = new Simulacao(10000, quantidadeNeuroniosOculta);
        Simulacao.taxaAprendizado = taxa;
        
        simula.simulandoTeste();
        System.gc();

        // Grava a separacao de uma simulacao da outra
        Log.gravarPesosIniciais("\nFim da Simulacao: " + (i + 1) + "." + (j + 1) + "\n\n");
        Log.gravarGabarito("\nFim da Simulacao: " + (i + 1) + "." + (j + 1) + "\n\n");
        Log.gravarPesosFinais("\nFim da Simulacao: " + (i + 1) + "." + (j + 1) + "\n\n");
        Log.gravarMatrizConfusao("\nFim da Simulacao: " + (i + 1) + "." + (j + 1) + "\n\n");

      }

      quantidadeNeuroniosOculta = 10;

    }
    
  }

  public static void executaUmaSimulacao() throws IOException {
    Log.gravarGabarito("\nInicio da Simulacao: \n");
    Simulacao simula = new Simulacao(10000, quantidadeNeuroniosOculta);
    Simulacao.taxaAprendizado = taxa;
    
    simula.simulandoTeste();

  }

}
