import java.io.IOException;
import java.util.*;

public class GerenciadorRedeNeural extends Calculo{

  List<Letras> conjuntoTreinamento = new ArrayList<>();
  List<Letras> conjuntoValidacao = new ArrayList<>();
  List<Letras> conjuntoCompleto = new ArrayList<>();
  List<List<Letras>> conjuntoParticionado = new ArrayList<>();//sao os k do cross-validation


  //Soma ponderada usada nos calculos
  List<Double> somaPonderadaOcultaList = new ArrayList<>();
  List<Double> somaPonderadaSaidaList = new ArrayList<>();
  //Listas com os neuronios da MLP
  List<Neuronio> neuroniosOculta = new ArrayList<>();
  List<Neuronio> neuroniosSaida = new ArrayList<>();

  //Serao usado no log
  private double somatorioErro2 = 0, maiorValor = -1;
  public int idLetra = -1, acertos, erros;


  
  public GerenciadorRedeNeural(){}



  //Carrega e separa o conjunto de dados de teste e treinamento
  public void importarExemplos(){
    
  System.out.println("\n ------- INICIANDO REDE NEURAL -------- \n");
  
  this.conjuntoCompleto = LeituraArquivo.dadosCarregados("treinamento");

  }



   /*
      Recebo os dados com os pesos de cada letra 
      Aqui processo e atualizo utilizando a classe Calculo
   */
public void feedforward(Letras m, String conjuntoTreinoOuTeste) throws IOException{
    
  List<Double> listaValorFuncaoAtivacaoOculta = new ArrayList<>();
  List<Double> listaValorFuncaoAtivacaoSaida = new ArrayList<>();


   //1° Soma ponderada de cada neuronio da camada oculta
   //2° Funcao de ativacao na soma ponderada da camada oculta
   funcaoAtivacaoCamadaOculta(m, listaValorFuncaoAtivacaoOculta);

   //3° Soma ponderada da funcao de ativacao dos neuronios da camada oculta
   //4° Funcao de ativacao na soma ponderada da camada de saida
   funcaoAtivacaoCamadaSaida(m,listaValorFuncaoAtivacaoOculta,listaValorFuncaoAtivacaoSaida);

   //5° Comparo o valor gerado com o valor esperado, se for diferente atualizo os pesos e pesoBias
   validandoResultados(listaValorFuncaoAtivacaoSaida,conjuntoTreinoOuTeste, m);
   
   
   //(esperado,real)
   Log.matrizDeConfusaoAtt(m.getRepresentadaDaLetra().getId(),this.idLetra);

   //Chamo o metodo backpropagation para atualizar os pesos e bias
   backpropagation(listaValorFuncaoAtivacaoSaida,listaValorFuncaoAtivacaoOculta,m,conjuntoTreinoOuTeste);
   

}



  
public void backpropagation(List<Double> listaValorFuncaoAtivacaoSaida,List<Double> listaValorFuncaoAtivacaoOculta, Letras m, String conjuntoTreinoOuTeste) throws IOException{
    
    List<Double> delta_inj = new ArrayList<>();
    boolean condicaoDeParada = true;
    
    //1° Calculo o somatorio do E^2 
    calculandoErro2(listaValorFuncaoAtivacaoSaida,m); 

    if(conjuntoTreinoOuTeste.equals("Treinamento")){ 

    //2° Atualizo o peso do Bias da camada de Saida
    //3° Atualizo os pesos da camada de saida
    atualizandoBiasPesosSaida(m,condicaoDeParada, listaValorFuncaoAtivacaoSaida, listaValorFuncaoAtivacaoOculta,delta_inj);


     //4° Atualizo o peso do Bias da camada oculta
     //5° Atualizo os pesos da camada oculta
    atualizandoPesosBiasOculta(m,listaValorFuncaoAtivacaoOculta,delta_inj);
    } 

     //Limpo as listas usadas para ser usado na outra letra e atualiza dist
     somaPonderadaOcultaList.clear();
     somaPonderadaSaidaList.clear();

}

  

  //////////////////////////////////
  //     USADO NO FEEDFORWARD	    //
  //////////////////////////////////


public void funcaoAtivacaoCamadaOculta(Letras m, List<Double> listaValorFuncaoAtivacaoOculta){
    //1° Soma ponderada de cada neuronio da camada oculta
    for(Neuronio n2 : this.neuroniosOculta){
    double somaPonderadaOculta = 0;
      
    //Percorre os 63 neuronios da camada de entrada
    for(int i = 0; i < m.getDadosLetra().size(); i++){
    //bias+x1*w1+x2*w2...
    double valor = (m.getDadosLetra().get(i) * n2.getPeso().get(i));
    somaPonderadaOculta += valor;
    }
      
    //Adiciono o Bias na conta
    somaPonderadaOculta += n2.getPesoBias();
    somaPonderadaOcultaList.add(somaPonderadaOculta);
    
     //2° Funcao de ativacao na soma ponderada da camada oculta
    listaValorFuncaoAtivacaoOculta.add(funcaoDeAtivacaoSigmoide(somaPonderadaOculta));
    }
}

  
public void funcaoAtivacaoCamadaSaida(Letras m, List<Double> listaValorFuncaoAtivacaoOculta, List<Double> listaValorFuncaoAtivacaoSaida){
    //3° Soma ponderada da funcao de ativacao dos neuronios da camada oculta
    for(Neuronio n3 : this.neuroniosSaida){
    double somaPonderadaSaida = 0;

    //Percorre os neuronios da camada oculta
    for(int j = 0; j < this.neuroniosOculta.size(); j++){
    double valor = (listaValorFuncaoAtivacaoOculta.get(j) * n3.getPeso().get(j));
    somaPonderadaSaida += valor;
    }

    //Adiciono o Bias na conta
    somaPonderadaSaida += n3.getPesoBias();
    somaPonderadaSaidaList.add(somaPonderadaSaida);

    //4° Funcao de ativacao na soma ponderada da camada de saida
    listaValorFuncaoAtivacaoSaida.add(funcaoDeAtivacaoSigmoide(somaPonderadaSaida));
    }
    
  
}

  
public void validandoResultados(List<Double> listaValorFuncaoAtivacaoSaida, String conjuntoTreinoOuTeste, Letras m){
    this.maiorValor = -1;

    //5° Comparo o valor gerado com o valor esperado, se for diferente atualizo os pesos e pesoBias
    for(int k = 0; k < listaValorFuncaoAtivacaoSaida.size(); k++){
        
    if(this.maiorValor == -1){
    this.maiorValor = listaValorFuncaoAtivacaoSaida.get(k);
    this.idLetra = k;
    } 

    else if(listaValorFuncaoAtivacaoSaida.get(k) > this.maiorValor){
    this.maiorValor = listaValorFuncaoAtivacaoSaida.get(k);
    this.idLetra = k;
    }
     
   }
   

   if(conjuntoTreinoOuTeste.equals("Teste")){ 
   if(LetrasEnum.getEnumID(this.idLetra).equals(m.getRepresentadaDaLetra())){
   Log.gravarGabarito(m.getRepresentadaDaLetra()+": ACERTOU!"+"\n");
   this.acertos++;
   } else {
   Log.gravarGabarito(m.getRepresentadaDaLetra()+": ERROU!"+"\n");
   this.erros++;
   }
  }
}


  //////////////////////////////////
  //   USADO NO BACKPROPAGATION	  //
  //////////////////////////////////
  

public void calculandoErro2(List<Double> listaValorFuncaoAtivacaoSaida, Letras m) throws IOException{ 
    this.somatorioErro2 = 0;
    for(int i = 0; i < listaValorFuncaoAtivacaoSaida.size(); i++){
    this.somatorioErro2 = erro2(m.getRepresentadaDaLetra().getDadosLetra() 
                                [i],listaValorFuncaoAtivacaoSaida.get(i));
    
    }
}



  
public void atualizandoBiasPesosSaida(Letras m, boolean condicaoDeParada,List<Double> listaValorFuncaoAtivacaoSaida,List<Double> listaValorFuncaoAtivacaoOculta, List<Double> delta_inj){
    //2° Atualizo o peso do Bias da camada de Saida
    for(int k = 0; k < this.neuroniosSaida.size(); k++){

    double termoErro = termoDeErroCadaNeuronio(
    m.getRepresentadaDaLetra().getDadosLetra()[k],listaValorFuncaoAtivacaoSaida.get(k),somaPonderadaSaidaList.get(k));

    
    //Atualizo o peso dos bias de cada neuronio
    this.neuroniosSaida.get(k).setBias(atualizacaoPesosBiasCamadaSaida
                                           (this.neuroniosSaida.get(k).getPesoBias(),
                                            Simulacao.taxaAprendizado,termoErro));

    //3° Atualizo os pesos da camada de saida 
    for(int j = 0; j < this.neuroniosSaida.get(k).getPeso().size(); j++){
        
    this.neuroniosSaida.get(k).getPeso()
    .set(j,atualizacaoPesosCamadaSaida(this.neuroniosSaida.get(k).getPeso().get(j),
    Simulacao.taxaAprendizado, listaValorFuncaoAtivacaoOculta.get(j), 
    termoErro)); 
        
    if(condicaoDeParada){ 
    double delta_inj_Somatorio = 0;
          
    //δ_inj = ∑ k=1 δk * wjk
    //Sera usado no passo 4 e 5 
    for(int s = 0; s < this.neuroniosSaida.size();s++){
        
    double termoErro2 = termoDeErroCadaNeuronio(
    m.getRepresentadaDaLetra().getDadosLetra()[s],listaValorFuncaoAtivacaoSaida.get(s),somaPonderadaSaidaList.get(s));
        
    delta_inj_Somatorio += (termoErro2 * this.neuroniosSaida.get(s).getPeso().get(j));
        
    }
    delta_inj.add(delta_inj_Somatorio);
  }
        
         
  }
      
    condicaoDeParada = false;    
  }
    
}


  
public void atualizandoPesosBiasOculta(Letras m, List<Double> listaValorFuncaoAtivacaoOculta,List<Double> delta_inj){

     for(int n = 0; n < this.neuroniosOculta.size(); n++){
     //δj = δinj * f′(z inj)
     double deltaj = (delta_inj.get(n)*derivadaFuncaoAtivacao(somaPonderadaOcultaList.get(n)));
      
      
     //4° Atualizo o peso do Bias da camada oculta
     this.neuroniosOculta.get(n).setBias(atualizacaoPesosBiasCamadaOculta(
     this.neuroniosOculta.get(n).getPesoBias(), Simulacao.taxaAprendizado,deltaj));

     
     for(int l = 0; l < this.neuroniosOculta.get(n).getPeso().size(); l++){

     //5° Atualizo os pesos da camada oculta 
     this.neuroniosOculta.get(n).getPeso().set(l,
     atualizacaoPesosCamadaOculta(this.neuroniosOculta.get(n).getPeso().get(l),
     Simulacao.taxaAprendizado,m.getDadosLetra().get(l),deltaj));
     
      }     
       
    }
     
}

 

  //////////////////////////////////
  //		   	CROSS-VALIDATION	    //
  //////////////////////////////////


  public void organizandoOsConjuntos(int k_fold_validacao) {

    this.conjuntoTreinamento.clear();
    this.conjuntoValidacao.clear();
    int qtdPorPasta = (int) this.conjuntoCompleto.size() / Simulacao.K_FOLDS;

    int inicio = qtdPorPasta * (k_fold_validacao-1);
    int fim = inicio + qtdPorPasta - 1;

    for (int i = 0; i < conjuntoCompleto.size(); i++) {

      if (i >= inicio && i <= fim) 
        this.conjuntoValidacao.add(conjuntoCompleto.get(i));
      else
        this.conjuntoTreinamento.add(conjuntoCompleto.get(i));

    }
    
  }


  

  public double getSomatorioErro2(){
    return this.somatorioErro2;
  }


  public void setSomatorioErro2(double erro){
    this.somatorioErro2 = erro;
  }

  

  
}