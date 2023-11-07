package dao;

import connection.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import user.ForgotPassword;

/**
 *
 * @author jonnie
 */
public class ForgotPasswordDao {

    Statement st;
    PreparedStatement ps;
    ResultSet rs;
    Connection conn;
    DBConnection dbconnection = new DBConnection();

    public ForgotPasswordDao() {
        try {
            this.conn = dbconnection.getConnection();
         } catch (SQLException ex) {
             Logger.getLogger(ForgotPasswordDao.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    //check whether the email exists
    public boolean isEmailExists(String email) {
        try {
            ps = conn.prepareStatement("select * from user where uemail = ?");
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                ForgotPassword.jTextField1.setText(rs.getString(6));
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "The email address does not exist");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ForgotPasswordDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbconnection.closeResources();
        }
        return false;
    }
    // checking the old answer
   public boolean getAns(String email, String newAns){
        try {
            ps = conn.prepareStatement("Select * from user where uemail = ?");
            ps.setString(1,email);
            rs = ps.executeQuery();
            if(rs.next()){
                String oldAns = rs.getString(7);
                if(newAns.equals(oldAns)){
                    return true;
                }else{
                    JOptionPane.showMessageDialog(null,"Security answer didnot match");
                    return false;
                } 
            }
        } catch (SQLException ex) {
            Logger.getLogger(ForgotPasswordDao.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            dbconnection.closeResources();
        }
       return false;
   }
   // set the new password
   public void setPassword(String email, String pass){
        try {
            ps = conn.prepareStatement("update user set upassword = ? where uemail = ?");
            ps.setString(1, pass);
            ps.setString(2,email);
            if(ps.executeUpdate()> 0){
                JOptionPane.showMessageDialog(null, "password successfully updated");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ForgotPasswordDao.class.getName()).log(Level.SEVERE, null, ex);
        }
                      
   }  
      
}                           