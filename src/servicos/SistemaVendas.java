package servicos;

import entidades.*;
import pagamento.FormaPagamento;

import java.util.ArrayList;
import java.util.List;

public class SistemaVendas {
    private List<Produto> produtos = new ArrayList<>();
    private List<Cliente> clientes = new ArrayList<>();
    private List<Pedido> pedidos = new ArrayList<>();

    public void cadastrarProduto(String nome, double preco, int estoque, boolean promocao, double desconto) {
        produtos.add(new Produto(nome, preco, estoque, promocao, desconto));
    }

    public void cadastrarCliente(String nome, String email) {
        clientes.add(new Cliente(nome, email));
    }

    public Produto buscarProduto(String nome) {
        return produtos.stream().filter(p -> p.getNome().equalsIgnoreCase(nome)).findFirst().orElse(null);
    }

    public Cliente buscarCliente(String nome) {
        return clientes.stream().filter(c -> c.getNome().equalsIgnoreCase(nome)).findFirst().orElse(null);
    }

    public void realizarPedido(String nomeCliente, List<ItemPedido> itens, FormaPagamento pagamento) {
        Cliente cliente = buscarCliente(nomeCliente);
        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        Pedido pedido = new Pedido(cliente, pagamento);
        for (ItemPedido item : itens) {
            pedido.adicionarItem(item.getProduto(), item.getQuantidade());
        }

        pedidos.add(pedido);
        pedido.exibirResumo();
    }

    public void listarProdutos() {
        System.out.println("Produtos cadastrados:");
        for (Produto p : produtos) {
            String infoPromocao = p.isPromocao() ? " (Promoção: " + (int)(p.getDesconto() * 100) + "%)" : "";
            System.out.println("- " + p.getNome() + " | R$" + p.getPreco() + " | Estoque: " + p.getEstoque() + infoPromocao);
        }
        System.out.println();
    }

    public void listarPedidos() {
        System.out.println("Pedidos realizados:");
        for (Pedido p : pedidos) {
            p.exibirResumo();
        }
    }
}