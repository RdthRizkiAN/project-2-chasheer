/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pointofsale;

import com.mysql.jdbc.Connection;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.swing.*;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author yanti
 */
public class supplier extends javax.swing.JFrame {

    /**
     * Creates new form supplier
     */
    Connection koneksi;
    public supplier() {
        koneksi = DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db_pos");
        initComponents();
        LihatData();
        text_idsupplier.setEditable(false);
        Kosong();
    }
    
    private void Kosong(){
        text_namasupplier.setText(null);
        text_notelepon.setText(null);
        text_alamat.setText(null);
        LihatData();
        IdSupplier();
    }
    
    DefaultTableModel dtm;
    private void LihatData(){
        String[] kolom = {"No", "ID Supplier", "Nama Supplier", "No. Telepon", "Alamat" };
        dtm = new DefaultTableModel(null, kolom);
        try{
            Statement stmt = koneksi.createStatement();
            String query = "SELECT * FROM supplier";
            ResultSet rs =  stmt.executeQuery(query);
            int no = 1;
            while (rs.next()){
                String id_supplier = rs.getString("id_supplier");
                String nama_supplier = rs.getString("nama_supplier");
                String no_telepon = rs.getString("no_telepon");
                String alamat = rs.getString("alamat");
                
                dtm.addRow(new String[]{no+"",id_supplier,nama_supplier,no_telepon,alamat});
                no++;
            }
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
        table_supplier.setModel(dtm);
        
        table_supplier.getColumnModel().getColumn(0).setPreferredWidth(15);
        table_supplier.getColumnModel().getColumn(1).setPreferredWidth(100);
        table_supplier.getColumnModel().getColumn(2).setPreferredWidth(50);
        table_supplier.getColumnModel().getColumn(3).setPreferredWidth(100);
    }
    
    private void IdSupplier(){
        try {
            Statement stmt = koneksi.createStatement();
            String query = "SELECT * FROM supplier order by id_supplier desc";
            ResultSet rs =  stmt.executeQuery(query);
            if (rs.next()) {
                String id_barang = rs.getString("id_supplier").substring(3);
                String AN = "" + (Integer.parseInt(id_barang) + 1);
                String Nol = "";

                if(AN.length()==1)
                {Nol = "000";}
                else if(AN.length()==2)
                {Nol = "00";}
                else if(AN.length()==3)
                {Nol = "0";}
                else if(AN.length()==4)
                {Nol = "";}

               text_idsupplier.setText("SPL" + Nol + AN);
            } else {
               text_idsupplier.setText("SPL0001");
            }

           }catch (SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi Kesalahan Pada Query!");
           }
    }
    
    private void TambahData(){
        try{
            String query = "INSERT INTO supplier (id_supplier, nama_supplier , no_telepon, alamat) VALUES (?, ?, ?, ?);";
            PreparedStatement ps = koneksi.prepareStatement(query);
            ps.setString(1, text_idsupplier.getText());
            ps.setString(2, text_namasupplier.getText());
            ps.setString(3, text_notelepon.getText());
            ps.setString(4, text_alamat.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Input Success!");
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Input Failed!");
        } 
    }
    
    private void EditData(){
        String id_supplier = text_idsupplier.getText();
        String nama_supplier = text_namasupplier.getText();
        String no_telepon = text_notelepon.getText();
        String alamat = text_alamat.getText();
        //String gambar = label_gambar.setBlob();
        
        try{
            Statement stmt = koneksi.createStatement();
            String query = "UPDATE supplier SET nama_supplier = '"+nama_supplier+"',"
                    + "no_telepon = '"+no_telepon+"',"
                    + "alamat = '"+alamat+"' WHERE id_supplier = '"+id_supplier+"'";
            
            System.out.println(query);
            int berhasil = stmt.executeUpdate(query);
            if(berhasil == 1){
                JOptionPane.showMessageDialog(null, "Data Berhasil Dirubah!");
            }else{
                JOptionPane.showMessageDialog(null, "Data Gagal Dirubah!");
            }
        } catch (SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi Kesalahan Pada Query!");
        }
    }
    
    private void HapusData(){
        String id_supplier = text_idsupplier.getText();
        try{
            // TODO add your handling code here:
            Statement stmt = koneksi.createStatement();
            String query = "DELETE FROM supplier WHERE id_supplier = '"+id_supplier+"';";
            int berhasil = stmt.executeUpdate(query);
            if(berhasil == 1){
                JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus!");
                dtm.getDataVector().removeAllElements();
                LihatData();
            }else{
                JOptionPane.showMessageDialog(null, "Data Gagal Dihapus!");
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        text_idsupplier = new javax.swing.JTextField();
        text_namasupplier = new javax.swing.JTextField();
        text_notelepon = new javax.swing.JTextField();
        text_alamat = new javax.swing.JTextField();
        button_save = new javax.swing.JButton();
        button_edit = new javax.swing.JButton();
        button_hapus = new javax.swing.JButton();
        button_reset = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_supplier = new javax.swing.JTable();
        button_keluar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Data Supplier");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("ID Supplier");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Nama Supplier");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Alamat");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("No. Telepon");

        text_idsupplier.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        text_namasupplier.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        text_notelepon.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        text_notelepon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_noteleponActionPerformed(evt);
            }
        });

        text_alamat.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        button_save.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        button_save.setText("Save");
        button_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_saveActionPerformed(evt);
            }
        });

        button_edit.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        button_edit.setText("Edit");
        button_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_editActionPerformed(evt);
            }
        });

        button_hapus.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        button_hapus.setText("Hapus");
        button_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_hapusActionPerformed(evt);
            }
        });

        button_reset.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        button_reset.setText("Reset");
        button_reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_resetActionPerformed(evt);
            }
        });

        table_supplier.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        table_supplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table_supplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_supplierMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table_supplier);

        button_keluar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        button_keluar.setText("Keluar");
        button_keluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_keluarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1254, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(text_alamat, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1000, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(text_notelepon, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(text_namasupplier, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(text_idsupplier, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1000, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(574, 574, 574)
                                .addComponent(jLabel1)
                                .addGap(450, 450, 450)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(button_reset, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(button_hapus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                            .addComponent(button_edit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(button_save, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(button_keluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(button_keluar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(button_save, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(button_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(button_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(text_idsupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(text_namasupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(text_notelepon, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(text_alamat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(button_reset, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 14;
        gridBagConstraints.ipady = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 10);
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void text_noteleponActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_noteleponActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_noteleponActionPerformed

    private void button_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_resetActionPerformed
        Kosong();
    }//GEN-LAST:event_button_resetActionPerformed

    private void button_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_saveActionPerformed
        TambahData();
        Kosong();
    }//GEN-LAST:event_button_saveActionPerformed

    private void table_supplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_supplierMouseClicked
        int baris = table_supplier.rowAtPoint(evt.getPoint());
        
        String id_supplier = table_supplier.getValueAt(baris, 1).toString();
        text_idsupplier.setText(id_supplier);
        
        String nama_supplier = table_supplier.getValueAt(baris, 2).toString();
        text_namasupplier.setText(nama_supplier);
        
        String no_telepon = table_supplier.getValueAt(baris, 3).toString();
        text_notelepon.setText(no_telepon);
        
        String alamat = table_supplier.getValueAt(baris, 4).toString();
        text_alamat.setText(alamat);
    }//GEN-LAST:event_table_supplierMouseClicked

    private void button_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_editActionPerformed
        EditData();
        Kosong();
    }//GEN-LAST:event_button_editActionPerformed

    private void button_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_hapusActionPerformed
        HapusData();
        Kosong();
    }//GEN-LAST:event_button_hapusActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        setExtendedState(supplier.MAXIMIZED_BOTH);
    }//GEN-LAST:event_formWindowOpened

    private void button_keluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_keluarActionPerformed
        dispose();
        admin variable = new admin();
        variable.show();
    }//GEN-LAST:event_button_keluarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new supplier().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_edit;
    private javax.swing.JButton button_hapus;
    private javax.swing.JButton button_keluar;
    private javax.swing.JButton button_reset;
    private javax.swing.JButton button_save;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table_supplier;
    private javax.swing.JTextField text_alamat;
    private javax.swing.JTextField text_idsupplier;
    private javax.swing.JTextField text_namasupplier;
    private javax.swing.JTextField text_notelepon;
    // End of variables declaration//GEN-END:variables
}
