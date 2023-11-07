package dao;
 
import java.sql.Statement;
import connection.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jonnie
 */
public class PurchaseDao {

    PreparedStatement ps;
    Statement st;
    ResultSet rs;
    Connection conn;
    DBConnection dbconnection = new DBConnection();

    public PurchaseDao() {
        try {
            conn = dbconnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //get maximum row
    public int getMaxRow() {
        int row = 0;
        try {
            ps = conn.prepareStatement("select max(pid) from purchase");
            rs = ps.executeQuery();
            while (rs.next()) {
                row = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row + 1;
    }

    //get user value
    public String[] getUserValue(String email) {
        String[] value = new String[5];
        try {
            ps = conn.prepareStatement("select uid, uname, uphone, uaddress1, uaddress2 from user where uemail = ?");
            ps.setString(1, email); // Bind the email parameter
            rs = ps.executeQuery();
            if (rs.next()) {
                value[0] = rs.getString(1);
                value[1] = rs.getString(2);
                value[2] = rs.getString(3);
                value[3] = rs.getString(4);
                value[4] = rs.getString(5);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    // insert data into purchase table
    public void insert(int id, int uid, String uname, String uphone, int pid, String pname, int qty,
            double price, double total, String purchaseDate, String address, String receivedDate,
            String supplier, String status) {
        try {
            ps = conn.prepareStatement("INSERT INTO purchase (uid, uname, uphone, pid, product_name, qty, "
                    + "price, total, p_date, uaddress, receive_date, supplier, status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, uid);
            ps.setString(2, uname);
            ps.setString(3, uphone);
            ps.setInt(4, pid);
            ps.setString(5, pname);
            ps.setInt(6, qty);
            ps.setDouble(7, price);
            ps.setDouble(8, total);
            ps.setString(9, purchaseDate);
            ps.setString(10, address);
            ps.setString(11, receivedDate);
            ps.setString(12, supplier);
            ps.setString(13, status);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    //get product quantity
    public int getQty(int pid) {
        int qty = 0;
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select pqty from product where pid = " + pid + "");
            if (rs.next()) {
                qty = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return qty;
    }

    //update product quantity
    public void qtyUpdate(int pid, int qty) {
        try {
            String query = "UPDATE product SET pqty = ? WHERE pid = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, qty);
            ps.setInt(2, pid);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //get specific user purchased product
    public void getProductsvalue(JTable table, String search, int uid) {
        String sql = "select * from purchase where concat(id , pid, product_name) like ? and uid = ? order by id asc ";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + search + "%");
            ps.setInt(2, uid);
            rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;
            while (rs.next()) {
                row = new Object[10];
                row[0] = rs.getInt(1);
                row[1] = rs.getInt(2);
                row[2] = rs.getString(6);
                row[3] = rs.getInt(7);
                row[4] = rs.getDouble(8);
                row[5] = rs.getDouble(9);
                row[6] = rs.getString(10);
                row[7] = rs.getString(12);
                row[8] = rs.getString(13);
                row[9] = rs.getString(14);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //ask the user whether they want to refund
    public void refund(int pid) {
        int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to refund this purchase?", "Delete purchase", JOptionPane.OK_CANCEL_OPTION);
        if (confirmation == JOptionPane.OK_OPTION) {
            String query = "delete from purchase where pid = ?";
            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, pid);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(null, "Product refund successful");
                }
            } catch (SQLException ex) {
                Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
