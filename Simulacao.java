import java.io.IOException;
import java.util.*;

public class Simulacao {

static double taxaAprendizado;//Tem que ser diferente de 0
private int epoca, quantidadeNeuroniosOculta;
List<Double> valores = new ArrayList<>();
public static final int CURVAVALIDACAO = 50, K_FOLDS = 7;
double erroValidacao = 0;
double erroTeste = 0;

  
Random gerador = new Random();
GerenciadorRedeNeural mlp = new GerenciadorRedeNeural();
List<Letras> listaDeTeste = new ArrayList<>();;


  
  public Simulacao(int epoca, int quantidadeNeuroniosOculta){
    this.epoca = epoca;
    this.quantidadeNeuroniosOculta = quantidadeNeuroniosOculta;
  }



  
  public void simulandoTeste() throws IOException{  
      
      mlp.importarExemplos();
      inicializandoRedeNeuralComPesosAleatorios();
    
      Log.gravarParametrosIniciais(mlp.neuroniosOculta.size()+
                               ";"+taxaAprendizado+";"+epoca+";"+"Sigmoide");

      System.out.println("\n TREINANDO REDE NEURAL ...\n");
  
      //Laco das epocas
      for(int i = 0; i <= epoca; i++){
        

      double erroTreinamento = 0;

      //Collections.shuffle(mlp.conjuntoCompleto);//Embaralha o conjunto completo

      if (i % 500 == 0) System.out.print(i + ", ");
        
      //Laco que muda os K
      for(int k = 1; k <= K_FOLDS; k++){

      mlp.organizandoOsConjuntos(k);

      //Laco passando os conjuntos de treinamento
      for(Letras m : mlp.conjuntoTreinamento){
      mlp.feedforward(m, "Treinamento");
      erroTreinamento += mlp.getSomatorioErro2();
           
      }

      //Testa a cada treinamento na rede
      testeDeValidacao();
      

      }

      double mediaTreinamento = (double) (erroTreinamento/K_FOLDS);
      double mediaValidacao = (double) (erroValidacao/K_FOLDS);
      this.erroValidacao = 0;

      Log.gravarErroInteracao(mediaTreinamento + ";" + i + ";" + taxaAprendizado + ";" + quantidadeNeuroniosOculta + ";" + "Treinamento" + "\n");
      Log.gravarErroInteracao(mediaValidacao + ";" + i + ";" + taxaAprendizado + ";" + quantidadeNeuroniosOculta + ";" + "Validacao" + "\n");



      if(i == epoca) epoca += 10000;//Basicamente adiciona mais epocas, pq a parada e por erro

      //Condicao de parada seria o erro e precisa ser muito pequeno e parada antecipada
      if(mlp.getSomatorioErro2() <= 0.0001 || paradaAntecipada(mediaValidacao)){
      
      Log.gravarPesosFinais("PesosDaCamadaOculta" + "\n");
      gerandoLogOculta(false);
        
      Log.gravarPesosFinais("\nPesosDaCamadaSaida" + "\n");
      gerandoLogSaida(false);
        
      Log.imprimeMatrizConfusao();//imprime a matriz de confusao no final do aprendizado

      testeFinal();//Passa o diretorio "teste" e testa se a rede aprendeu
      //double mediaTeste = (double) (this.erroTeste/K_FOLDS);
      //Log.gravarErroInteracao(mediaTeste + ";" + i + ";" + taxaAprendizado + ";" + quantidadeNeuroniosOculta + ";" + "Teste" + "\n");

      return;
      
      }

    }
    
  }




  
  //Determina os pesos iniciais na rede neural
  public List<Double> pesos(int quantidade){

    List<Double> listaPesos = new ArrayList<>();
    
    for(int i = 0; i < quantidade; i++){
      listaPesos.add(gerador.nextDouble());
    }

    return listaPesos;

  }




  

  public void inicializandoRedeNeuralComPesosAleatorios() {

    System.out.println("\n ------- INICIANDO REDE NEURAL COM PESOS ALEATORIOS -------- \n");

    int quantidadePesos_CamadaOculta = mlp.conjuntoCompleto.get(0).getDadosLetra().size();
    int quantidadePesos_CamadaSaida = mlp.conjuntoCompleto.get(0).getRepresentadaDaLetra().getDadosLetra().length;

    mlp.neuroniosOculta.clear();
    mlp.neuroniosSaida.clear();

    for (int j = 0; j < quantidadeNeuroniosOculta; j++) {

    // Primeiro determino a quantidade de neuronios da camada oculta
    Neuronio n2 = new Neuronio(pesos(quantidadePesos_CamadaOculta), gerador.nextDouble());
    mlp.neuroniosOculta.add(n2);

    // Segundo determino a quantidade de neuronios da camada de saida
    if (j < quantidadePesos_CamadaSaida) {
    Neuronio n3 = new Neuronio(pesos(quantidadeNeuroniosOculta), gerador.nextDouble());
    mlp.neuroniosSaida.add(n3);

    }

  }


    Log.gravarPesosIniciais("PesosDaquantidadeNeuroniosOculta" + "\n");
    gerandoLogOculta(true);

    Log.gravarPesosIniciais("\nPesosDaCamadaSaida" + "\n");
    gerandoLogSaida(true);

  }




public boolean paradaAntecipada(double valor){
  int contagem = 0;


  //Se o valor 50 for igual ou menor ele adiciona, preciso de 51 valores
  if(this.valores.size() <= CURVAVALIDACAO) this.valores.add(valor);
  
  //Aqui se for 51 eles entram no if 
  else if(this.valores.size() == (CURVAVALIDACAO+1)){

  for(int i = 1; i < this.valores.size(); i++){

  //Verifico se todos os valores acima do primeiro indice
  boolean condicao = this.valores.get(i) > this.valores.get(0);

  //Se for ta com tendencia de crescimento
  if(condicao) contagem++;
  }

  this.valores.clear();
  this.valores.add(valor);
  }

  //Se todos capturados da lista for maior que o primeiro indice e retornado true 
  if(contagem == CURVAVALIDACAO) return true;


  return false;
}









  //////////////////////////////////
  //     TESTE DOS CONJUNTOS      //
  //////////////////////////////////

  public void testeFinal() throws IOException {

    LeituraArquivo.todasLetras.clear();
    this.listaDeTeste = LeituraArquivo.dadosCarregados("teste");

    for (Letras m : this.listaDeTeste) {

    mlp.feedforward(m, "Teste");
    this.erroTeste += mlp.getSomatorioErro2();
  }

    Log.gravarGabarito("\nQuantidade de Acertos: " + mlp.acertos);
    Log.gravarGabarito("\nQuantidade de Erros: " + mlp.erros + "\n");

  }



  public void testeDeValidacao() throws IOException {

    for (Letras m : mlp.conjuntoValidacao) {
    mlp.feedforward(m, "Validacao");
    this.erroValidacao += mlp.getSomatorioErro2();
  }

  }







  //////////////////////////////////
  //        GERANDO LOGS          //
  //////////////////////////////////

  public void gerandoLogOculta(boolean qualLog) {

    for (int i = 0; i < mlp.neuroniosOculta.size(); i++) {

      if (qualLog) {
        Log.gravarPesosIniciais("n" + (i + 1) + "\n");
        Log.gravarPesos(mlp.neuroniosOculta.get(i).getPeso(), true);
      } else {
        Log.gravarPesosFinais("n" + (i + 1) + "\n");
        Log.gravarPesos(mlp.neuroniosOculta.get(i).getPeso(), false);
      }

    }

  }

  public void gerandoLogSaida(boolean qualLog) {

    for (int i = 0; i < mlp.neuroniosSaida.size(); i++) {

      if (qualLog) {
        Log.gravarPesosIniciais("n" + (i + 1) + "\n");
        Log.gravarPesos(mlp.neuroniosSaida.get(i).getPeso(), true);
      } else {
        Log.gravarPesosFinais("n" + (i + 1) + "\n");
        Log.gravarPesos(mlp.neuroniosSaida.get(i).getPeso(), false);

      }
    }
  }


  


  
}