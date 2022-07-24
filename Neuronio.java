import java.util.*;

public class Neuronio {

private List<Double> peso;
private double pesoBias; 
  
  public Neuronio(List<Double> peso, double pesoBias){
     this.peso = peso;
     this.pesoBias = pesoBias;
  }


 //////////////////////////////////
 //		   	GETs e SETs	           //
 //////////////////////////////////


  public List<Double> getPeso(){
    return this.peso;
  }

  public double getPesoBias(){
    return this.pesoBias;
  }

  public void setPeso(List<Double> peso){
    this.peso = peso;
  }

  public void setBias(double pesoBias){
    this.pesoBias = pesoBias;
  }


  
}