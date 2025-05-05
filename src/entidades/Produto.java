package entidades;

public class Produto {
    private String nome;
    private double preco;
    private int estoque;
    private boolean promocao;
    private double desconto; // percentual, ex: 10% = 0.10

    public Produto(String nome, double preco, int estoque, boolean promocao, double desconto) {
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
        this.promocao = promocao;
        this.desconto = desconto;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return promocao ? preco * (1 - desconto) : preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public boolean reduzirEstoque(int quantidade) {
        if (estoque >= quantidade) {
            estoque -= quantidade;
            return true;
        }
        return false;
    }

    public boolean isPromocao() {
        return promocao;
    }

    public double getDesconto() {
        return desconto;
    }
}