package dao;

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

public class ProductDao {

    PreparedStatement ps;
    ResultSet rs;
    Connection conn;
    DBConnection dbconnection = new DBConnection();

    public ProductDao() {
        try {
            conn = dbconnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //get category table max row
    public int getMaxRow() {
        int row = 0;
        try {
            ps = conn.prepareStatement("select max(pid) from product");
            rs = ps.executeQuery();
            while (rs.next()) {
                row = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row + 1;
    }

    //get the number of categories
    public int countCategories() {
        int total = 0;
        try {
            ps = conn.prepareStatement("SELECT COUNT(*) AS total FROM category");
            rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    //get all the categories
    public String[] getCategories() {
        String[] categories = new String[countCategories()];
        try {
            ps = conn.prepareStatement("select * from category");
            rs = ps.executeQuery();
            int i = 0;
            while (rs.next()) {
                categories[i] = rs.getString(2);
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return categories;
    }

    //check whether the student id exists
    public boolean isIDExist(int id) {
        try {
            ps = conn.prepareStatement("select * from product where pid = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    //check whether the product and category existed
    public boolean isProCatExist(String product, String categories) {
        try {
            ps = conn.prepareStatement("select * from product where pname = ? and cname = ?");
            ps.setString(1, product);
            ps.setString(2, categories);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    //insert into product data into the database
    public void insertProduct(int id, String pname, String categories, int quantity, double price) {
        try {
            ps = conn.prepareStatement("insert into product values(?,?,?,?,?)");
            ps.setInt(1, id);
            ps.setString(2, pname);
            ps.setString(3, categories);
            ps.setInt(4, quantity);
            ps.setDouble(5, price);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Product successfully updated");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //get all the product values
    public void getCategoriesValue(JTable table, String search) {
        try {
            ps = conn.prepareStatement("select * from product where concat(pid, pname, cname) like ? order by pid asc");
            ps.setString(1, "%" + search + "%");
            rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;
            while (rs.next()) {
                row = new Object[5];
                row[0] = rs.getInt(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getDouble(5);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //update product data
    public void update(int id, String pname, String category, int qty, double price) {
        try {
            ps = conn.prepareStatement("update product set pname = ?, cname = ?, pqty = ?, pprice = ? where pid = ?");
            ps.setString(1, pname);
            ps.setString(2, category);
            ps.setInt(3, qty);
            ps.setDouble(4, price);
            ps.setInt(5, id);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Product successfully updated");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //delete product data
    public void delete(int id) {
        int x = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this account", "Delete Account", JOptionPane.OK_CANCEL_OPTION);
        if (x == JOptionPane.OK_OPTION) {
            try {
                ps = conn.prepareStatement("delete from product where pid = ?");
                ps.setInt(1, id);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(null, "Product successfully deleted");
                }
            } catch (SQLException ex) {
                Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
