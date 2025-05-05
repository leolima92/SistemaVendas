package entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import pagamento.FormaPagamento;

public class Pedido {
    private int id;
    private Cliente cliente;
    private LocalDate data;
    private List<ItemPedido> itens;
    private FormaPagamento pagamento;

    public Pedido(Cliente cliente, FormaPagamento pagamento) {
        this.cliente = cliente;
        this.data = LocalDate.now();
        this.itens = new ArrayList<>();
        this.pagamento = pagamento;
    }

    public void adicionarItem(Produto produto, int quantidade) {
        if (produto.reduzirEstoque(quantidade)) {
            itens.add(new ItemPedido(produto, quantidade));
        } else {
            System.out.println("Estoque insuficiente para o produto: " + produto.getNome());
        }
    }

    public double getTotal() {
        return itens.stream().mapToDouble(ItemPedido::getSubtotal).sum();
    }

    public void exibirResumo() {
        System.out.println("Pedido de: " + cliente.getNome());
        System.out.println("Data: " + data);
        for (ItemPedido item : itens) {
            System.out.println("- " + item.getProduto().getNome() + " x " + item.getQuantidade() +
                    " = R$" + item.getSubtotal());
        }
        System.out.println("Pagamento: " + pagamento);
        System.out.println("Total: R$" + getTotal());
        System.out.println();
    }

   

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public FormaPagamento getPagamento() {
        return pagamento;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

}
