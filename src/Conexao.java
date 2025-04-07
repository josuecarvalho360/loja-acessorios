
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author josue
 */
public class Conexao {
    
    private Connection conn;
    private PreparedStatement st;
    private ResultSet rs;
    
    public boolean conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/loja", "root", "");
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Erro ao conectar: " + ex.getMessage());
            return false;
        }
    }
    
    
    public void desconectar() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao desconectar: " + ex.getMessage());
        }
    }
    
    
    public int cadastrarProduto(Produto produto) {
        int status;
        try {
            st = conn.prepareStatement("INSERT INTO produto (nome, valor) VALUES(?,?)");
            st.setString(1,produto.getNome());
            st.setDouble(2,produto.getValor());
            
                    
            status = st.executeUpdate();
            return status; 
        } catch (SQLException ex) {
            System.out.println("Erro ao conectar: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Erro ao inserir dados! Por favor, tente novamente.");
            return ex.getErrorCode();
        }
    }
    
    
    public int cadastrarVenda(Venda venda) {
        int status;
        try {
            st = conn.prepareStatement("INSERT INTO venda (cliente, data, produto, valor) VALUES(?, ?, ?, ?)");
            st.setString(1, venda.getCliente());
            st.setString(2, venda.getData());
            st.setString(3, venda.getProduto());
            st.setDouble(4, venda.getValor());
            
            status = st.executeUpdate();
            return status; 
        } catch (SQLException ex) {
            System.out.println("Erro ao conectar: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Erro ao inserir dados! Por favor, tente novamente.");
            return ex.getErrorCode();
        }
    }
    
    
    public List<Produto> consultarProdutos(String nomePesquisa) {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE nome LIKE ?";
        try {
            st = conn.prepareStatement(sql);
            st.setString(1, "%" + nomePesquisa + "%");
            rs = st.executeQuery();
            while (rs.next()) {
                Produto p = new Produto(rs.getString("nome"), rs.getDouble("valor"));
                lista.add(p);
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao consultar produtos: " + ex.getMessage());
        }
        return lista;
    }
    
    public List<Venda> consultarVendas(String produtoPesquisa) {
        List<Venda> lista = new ArrayList<>();
        String sql = "SELECT * FROM venda WHERE produto LIKE ?";
        try {
            st = conn.prepareStatement(sql);
            st.setString(1, "%" + produtoPesquisa + "%");
            rs = st.executeQuery();
            while (rs.next()) {
                Venda v = new Venda(
                    rs.getString("cliente"),
                    rs.getString("data"),
                    rs.getDouble("valor"),
                    rs.getString("produto")
                );
                lista.add(v);
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao consultar vendas: " + ex.getMessage());
        }
        return lista;
    }

    

    
    
}
