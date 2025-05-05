package servicos;

import dao.ClienteDAO;
import dao.ProdutoDAO;
import dao.PedidoDAO;

import entidades.*;
import pagamento.FormaPagamento;

import java.util.List;

public class SistemaVendas {
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();

    public void cadastrarProduto(String nome, double preco, int estoque, boolean promocao, double desconto) {
        Produto produto = new Produto(nome, preco, estoque, promocao, desconto);
        produtoDAO.salvar(produto);
    }

    public void cadastrarCliente(String nome, String email) {
        Cliente cliente = new Cliente(nome, email);
        clienteDAO.salvar(cliente);
    }

    public Produto buscarProduto(String nome) {
        return produtoDAO.buscarPorNome(nome);
    }

    public Cliente buscarCliente(String nome) {
        return clienteDAO.buscarPorNome(nome);
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


        pedidoDAO.salvar(pedido);
    }

    public List<Produto> listarProdutos() {
        return produtoDAO.listarTodos();
    }

    public void listarPedidos() {
        List<Pedido> pedidos = pedidoDAO.listarTodos();
        System.out.println("Pedidos realizados:");
        for (Pedido p : pedidos) {
            p.exibirResumo();
        }
    }
}
