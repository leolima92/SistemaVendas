import entidades.Cliente;
import entidades.ItemPedido;
import entidades.Produto;
import pagamento.FormaPagamento;
import servicos.SistemaVendas;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SistemaVendas sistema = new SistemaVendas();
        Scanner scanner = new Scanner(System.in);
        boolean executando = true;

        while (executando) {
            System.out.println("\n==== MENU ====");
            System.out.println("1. Cadastrar produto");
            System.out.println("2. Cadastrar cliente");
            System.out.println("3. Listar produtos");
            System.out.println("4. Realizar pedido");
            System.out.println("5. Listar pedidos");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            switch (opcao) {
                case 1:
                    System.out.print("Nome do produto: ");
                    String nomeProd = scanner.nextLine();

                    System.out.print("Preço: ");
                    double preco = scanner.nextDouble();
                    scanner.nextLine(); // limpar buffer

                    System.out.print("Estoque: ");
                    int estoque = scanner.nextInt();
                    scanner.nextLine(); // limpar buffer

                    System.out.print("Está em promoção? (sim/não): ");
                    String resposta = scanner.nextLine().trim().toLowerCase();
                    boolean promo = resposta.equals("sim");

                    double desconto = 0.0;
                    if (promo) {
                        System.out.print("Percentual de desconto (ex: 10 para 10%): ");
                        try {
                            desconto = Double.parseDouble(scanner.nextLine()) / 100.0;
                        } catch (NumberFormatException e) {
                            System.out.println("Desconto inválido.");
                            break;
                        }
                    }

                    sistema.cadastrarProduto(nomeProd, preco, estoque, promo, desconto);
                    break;

                case 2:
                    System.out.print("Nome do cliente: ");
                    String nomeCli = scanner.nextLine();
                    System.out.print("Email do cliente: ");
                    String email = scanner.nextLine();
                    sistema.cadastrarCliente(nomeCli, email);
                    break;

                case 3:
                    sistema.listarProdutos();
                    break;

                case 4:
                    System.out.print("Nome do cliente: ");
                    String nomeCliente = scanner.nextLine();
                    List<ItemPedido> itens = new ArrayList<>();

                    while (true) {
                        System.out.print("Nome do produto (ou 'fim' para encerrar): ");
                        String nomeItem = scanner.nextLine();
                        if (nomeItem.equalsIgnoreCase("fim")) break;

                        Produto produto = sistema.buscarProduto(nomeItem);
                        if (produto == null) {
                            System.out.println("Produto não encontrado.");
                            continue;
                        }

                        System.out.print("Quantidade: ");
                        int qtd = scanner.nextInt();
                        scanner.nextLine(); // limpar buffer
                        itens.add(new ItemPedido(produto, qtd));
                    }

                    System.out.println("Método de pagamento (1-DÉBITO, 2-CRÉDITO, 3-PIX, 4-BOLETO): ");
                    int pgto = scanner.nextInt();
                    scanner.nextLine();

                    FormaPagamento forma = switch (pgto) {
                        case 1 -> FormaPagamento.DEBITO;
                        case 2 -> FormaPagamento.CREDITO;
                        case 3 -> FormaPagamento.PIX;
                        case 4 -> FormaPagamento.BOLETO;
                        default -> FormaPagamento.PIX;
                    };

                    sistema.realizarPedido(nomeCliente, itens, forma);
                    break;

                case 5:
                    sistema.listarPedidos();
                    break;

                case 0:
                    executando = false;
                    System.out.println("Sistema encerrado.");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }
        }

        scanner.close();
    }
}
