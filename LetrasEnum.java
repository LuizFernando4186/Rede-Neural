public enum LetrasEnum{

  //o valor seria o valor esperado da rede neural
  
  LETRA_A(1000000, "A",0),
  LETRA_B(100000, "B",1),
  LETRA_C(10000, "C",2),
  LETRA_D(1000, "D",3),
  LETRA_E(100, "E",4),
  LETRA_J(10, "J",5),
  LETRA_K(1, "K",6);


    private int valor;//So para distinguir as letras quando le o arquivo
    private int id;//Usa quando for atualizar a matriz de confusao
    private String descricao;
    private int[] dadosLetra;
  
    private LetrasEnum(int valor, String descricao, int id) {
        this.valor = valor;
        this.descricao = descricao;
        this.id = id;
        this.dadosLetra = new int[7];
    }

    //Busca a letra pelo valor que foi lido no arquivo
    public static LetrasEnum getEnum(int valor) {
        for (LetrasEnum elementoEnum : LetrasEnum.values()) {
            if (elementoEnum.getValor() == valor) {
                return elementoEnum;
            }
        }

        return null;
    }

   //Busca a letra pelo valor que foi lido no arquivo
    public static LetrasEnum getEnumID(int id) {
        for (LetrasEnum elementoEnum : LetrasEnum.values()) {
            if (elementoEnum.getId() == id) {
                return elementoEnum;
            }
        }

        return null;
    }

   public int getValor() {
        return valor;
    }

   public int getId() {
        return this.id;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int[] getDadosLetra() {
        return this.dadosLetra;
    }

    public void setDadosLetra(int[] dadosLetra) {
        this.dadosLetra = dadosLetra;
    }

}