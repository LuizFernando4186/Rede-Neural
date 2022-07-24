public class Calculo {
  
  //f(x)=1/1+e^-x
  public double funcaoDeAtivacaoSigmoide(double somaPonderada){
    return (double) (1/(1+Math.exp(-(somaPonderada))));
  }

  //deltak <- (tk - yk)f'(y_ink)
  public double termoDeErroCadaNeuronio(double valorEsperado, double funcaoDeAtivacao, double somaPonderada){
  return (double) ((valorEsperado-funcaoDeAtivacao) * derivadaFuncaoAtivacao(somaPonderada));
  }

  //somatorio E^2= (y-yk)^2
  public double erro2(double valorEsperado, double funcaoAtivacao){
    return (double) Math.pow((valorEsperado-funcaoAtivacao),2);
  }

  //delta(wjk) = a*deltak*zj
  //wjk(new) = wjk(old) + delta(wjk)
  public double atualizacaoPesosCamadaSaida(double pesoCamadaDeSaida, double taxaAprendizado, double funcaoAtivacaoCamadaOculta, double termoDeErro){
    return (double) (pesoCamadaDeSaida + (taxaAprendizado*termoDeErro*funcaoAtivacaoCamadaOculta));
  }

  //delta(wok) = a*deltak
  //wok(new) = wok(old) + delta(wok)
  public double atualizacaoPesosBiasCamadaSaida(double pesoBiasCamadaDeSaida, double taxaAprendizado, double termoDeErro){
    return (double) (pesoBiasCamadaDeSaida + (taxaAprendizado * termoDeErro));
  }
  
  //deltaj = delta_inj * f'(z_inj)
  //delta(vij) = a*deltaj*xi
  //vij(new) = vij(old) + delta(vij)
  public double atualizacaoPesosCamadaOculta(double pesoCamadaDeOculta, double taxaAprendizado, double valoresNeuronioEntrada, double somatorioErroEderivadaFuncaoAtivacao){
   return (double) (pesoCamadaDeOculta + (taxaAprendizado*somatorioErroEderivadaFuncaoAtivacao*valoresNeuronioEntrada));
  }

  //delta0j = sigma_inj * f'(z_inj)
  //delta(v0j) = a*delta0j
  //v0j(new) = v0j(old) + delta(v0j)
  public double atualizacaoPesosBiasCamadaOculta(double pesoBiasCamadaOculta, double taxaAprendizado, double somatorioErroEderivadaFuncaoAtivacao){
    return (double) (pesoBiasCamadaOculta + (taxaAprendizado * somatorioErroEderivadaFuncaoAtivacao));
  }

  //sigmoide*(1-sigmoide)
  public double derivadaFuncaoAtivacao(double somaPonderada){
    return (double) (funcaoDeAtivacaoSigmoide(somaPonderada)*(1-funcaoDeAtivacaoSigmoide(somaPonderada)));
  }


}