package dao;

import entidades.Cliente;
import entidades.ItemPedido;
import entidades.Pedido;
import entidades.Produto;
import pagamento.FormaPagamento;
import util.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public int salvar(Pedido pedido) {
        int idPedido = -1;
        String insertPedido = "INSERT INTO pedido (cliente_nome, data, forma_pagamento) VALUES (?, ?, ?)";
        String insertItem = "INSERT INTO item_pedido (id_pedido, nome_produto, quantidade, subtotal) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(insertPedido, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, pedido.getCliente().getNome());
                stmt.setDate(2, Date.valueOf(pedido.getData()));
                stmt.setString(3, pedido.getPagamento().name());
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idPedido = rs.getInt(1);
                }
            }

            try (PreparedStatement stmtItem = conn.prepareStatement(insertItem)) {
                for (ItemPedido item : pedido.getItens()) {
                    stmtItem.setInt(1, idPedido);
                    stmtItem.setString(2, item.getProduto().getNome());
                    stmtItem.setInt(3, item.getQuantidade());
                    stmtItem.setDouble(4, item.getSubtotal());
                    stmtItem.addBatch();
                }
                stmtItem.executeBatch();
            }

            conn.commit();
            System.out.println("Pedido salvo com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idPedido;
    }

    public List<Pedido> listarTodos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido";

        try (Connection conn = Conexao.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente c = new Cliente(rs.getString("cliente_nome"), "");
                Pedido p = new Pedido(c, FormaPagamento.valueOf(rs.getString("forma_pagamento")));
                p.setData(rs.getDate("data").toLocalDate());
                p.setId(rs.getInt("id"));

                p.setItens(buscarItensDoPedido(p.getId()));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private List<ItemPedido> buscarItensDoPedido(int idPedido) {
        List<ItemPedido> itens = new ArrayList<>();
        String sql = "SELECT * FROM item_pedido WHERE id_pedido = ?";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPedido);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produto p = new Produto(
                        rs.getString("nome_produto"),
                        rs.getDouble("subtotal") / rs.getInt("quantidade"),
                        0, false, 0
                );
                itens.add(new ItemPedido(p, rs.getInt("quantidade")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itens;
    }
}

