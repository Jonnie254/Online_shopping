package dao;

import connection.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jonnie
 */
public class CategoryDao {

    DBConnection dbconnection = new DBConnection();
    Connection conn;
    PreparedStatement ps;
    Statement st;
    ResultSet rs;

    public CategoryDao() {
        try {
            conn = dbconnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //get the max row
    public int getRowMax() {
        int row = 0;
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select max(cid) from category");
            while (rs.next()) {
                row = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row + 1;
    }

    //check whether the category name exist
    public boolean isCategoryExists(String cname) {
        try {
            ps = conn.prepareStatement("Select * from category where cname = ?");
            ps.setString(1, cname);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    //insert a new category to the database
    public void insert(int id, String cname, String desc) {
        String sql = ("insert into category values(?,?,?)");
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, cname);
            ps.setString(3, desc);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Category added successfully");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //get values for category
    public void getCategoryValues(JTable table, String search) {
        String sql = "Select * from category where concat(cid, cname, cdesc) like ? order by cid asc";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + search + "%");
            rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;
            while (rs.next()) {
                row = new Object[9];
                row[0] = rs.getInt(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                model.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //update category data
    public void update(int id, String cname, String desc){
      String  sql = "update category set cname = ?, cdesc = ? where cid = ? ";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,cname);
            ps.setString(2,desc);
            ps.setInt(3, id);
            if(ps.executeUpdate()>0){
                JOptionPane.showMessageDialog(null, "Category successfully updated");
            } 
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
        }          
    }
    
    //check whether the category id exists
    public boolean isIDExists(int id){
        try {
            ps = conn.prepareStatement("select * from category where cid = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    //delete category data
    public void delete(int id) {
        int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this category?", "Delete account", JOptionPane.OK_CANCEL_OPTION);
        if (confirmation == JOptionPane.OK_OPTION) {
            try {
                ps = conn.prepareStatement("delete from category where cid = ?");
                ps.setInt(1, id);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(null, "Category account successfully deleted");
                }
            } catch (SQLException ex) {
                Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
