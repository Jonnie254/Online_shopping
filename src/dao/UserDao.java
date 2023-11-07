package dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import connection.DBConnection;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class UserDao {

    Statement st;
    PreparedStatement ps;
    ResultSet rs;
    Connection conn;
    DBConnection dbconnection = new DBConnection();

    public UserDao() {
        try {
            conn = dbconnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //get user table max row    
    public int getMaxRow() {
        int row = 0;
        try (Connection connection = dbconnection.getConnection(); Statement st = connection.createStatement(); ResultSet rs = st.executeQuery("select max(uid) from user")) {

            if (rs.next()) {
                row = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row + 1;
    }

    // check whether the email exists
    public boolean isEmailExist(String email) {
        try {
            ps = conn.prepareStatement("Select * from user where uemail = ?");
            ps.setString(1, email);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbconnection.closeResources();
        }
        return false;
    }

    // check whether the phone number exists
    public boolean isPhoneExists(String phone) {
        try {
            ps = conn.prepareStatement("Select * from user where uphone = ?");
            ps.setString(1, phone);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbconnection.closeResources();
        }
        return false;
    }

    // insert data into the user table
    public void insert(int id, String username, String email, String password, String phone,
            String seq, String ans, String address1, String address2) {
        String sql = "insert into user values(?,?,?,?,?,?,?,?,?)";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, username);
            ps.setString(3, email);
            ps.setString(4, password);
            ps.setString(5, phone);
            ps.setString(6, seq);
            ps.setString(7, ans);
            ps.setString(8, address1);
            ps.setString(9, address2);
            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbconnection.closeResources();
        }
    }

    // get user value
    public String[] getUserValues(int id) {
        String[] value = new String[9];
        try {
            ps = conn.prepareStatement("select * from user where uid = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                value[0] = rs.getString(1);
                value[1] = rs.getString(2);
                value[2] = rs.getString(3);
                value[3] = rs.getString(4);
                value[4] = rs.getString(5);
                value[5] = rs.getString(6);
                value[6] = rs.getString(7);
                value[7] = rs.getString(8);
                value[8] = rs.getString(9);

            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbconnection.closeResources();
        }
        return value;
    }

    public int getUserId(String email) {
        int id = 0;
        try {
            ps = conn.prepareStatement("select uid from user where uemail = ?");
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbconnection.closeResources();
        }
        return id;

    }
    // update user data

    public void update(int id, String username, String email, String password, String phone,
            String seq, String ans, String address1, String address2) {
        try {
            ps = conn.prepareStatement("update user set uname = ?, uemail = ?, upassword = ?, uphone = ?, usecqus = ?, uans = ?, uaddress1 = ?, uaddress2 = ? where uid = ?");
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, phone);
            ps.setString(5, seq);
            ps.setString(6, ans);
            ps.setString(7, address1);
            ps.setString(8, address2);
            ps.setInt(9, id);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "User data successfully updated");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbconnection.closeResources();
        }
    }

    // delete user data
    public void delete(int id) {
        int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this account", "Delete account", JOptionPane.OK_CANCEL_OPTION);
        if (confirmation == JOptionPane.OK_OPTION) {
            try {
                ps = conn.prepareStatement("delete from user where uid = ?");
                ps.setInt(1, id);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(null, "User account successfully deleted");
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        dbconnection.closeResources();
    }

    // get user data
    public void getUsersValue(JTable table, String search) {
        try {
            ps = conn.prepareStatement("select * from user where concat(uid, uname, uemail) like ? order by uid asc ");
            ps.setString(1, "%" + search + "%");
            rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            while (rs.next()) {
                Object[] row = new Object[9];
                row[0] = rs.getInt(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                row[5] = rs.getString(6);
                row[6] = rs.getString(7);
                row[7] = rs.getString(8);
                row[8] = rs.getString(9);
                model.addRow(row);
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
