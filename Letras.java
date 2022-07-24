import java.util.*;

public class Letras {

  private List<Integer> letra;
  private LetrasEnum representacaoDaLetra;
  

  public Letras(List<Integer> letra, LetrasEnum representacaoDaLetra){
  this.letra = letra;
  this.representacaoDaLetra = representacaoDaLetra;
  }

  public List<Integer> getDadosLetra(){
    return this.letra;
  }

  public LetrasEnum getRepresentadaDaLetra(){
    return this.representacaoDaLetra;
  }

  public void setLetra(List<Integer> letra){
    this.letra = letra;
  }


}