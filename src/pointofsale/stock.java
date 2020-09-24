/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pointofsale;

import com.mysql.jdbc.Connection;
import java.awt.Image;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author yanti
 */
public class stock extends javax.swing.JFrame {

    /**
     * Creates new form stock
     */
    Connection koneksi;
    public stock() {
        koneksi = DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db_pos");
        initComponents();
        LihatData();
        text_nama.setEditable(false);
        text_hargabeli.setEditable(false);
        text_hargabeli.setText("0");
        text_subtotal.setEditable(false);
        text_subtotal.setText("0");
        text_stock.setText("1");
        tanggal();
    }
    
    private void Kosong(){
        text_cari.setText(null);
        text_nama.setText(null);
        text_hargabeli.setText("0");
        text_subtotal.setText("0");
        text_stock.setText("1");
        LihatData();
    }
    
    public void tanggal(){
        Date ys=new Date();
        SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
        label_tanggal.setText(s.format(ys));
    }
    
    DefaultTableModel dtm;
    private void LihatData(){
        String id = text_cari.getText();
        String[] kolom = {"No", "ID", "Nama Barang", "Kategori", "Harga" , "Stock", "Supplier"};
        dtm = new DefaultTableModel(null, kolom);
        try{
            Statement stmt = koneksi.createStatement();
            String query = "SELECT * FROM barang WHERE id_barang = '"+id+"'";
            ResultSet rs =  stmt.executeQuery(query);
            int no = 1;
            while (rs.next()){
                String id_barang = rs.getString("id_barang");
                String nama_barang = rs.getString("nama_barang");
                String nama_kategori = rs.getString("nama_kategori");
                String harga_beli = rs.getString("harga_beli");
                String stock_barang = rs.getString("stock_barang");
                String nama_supplier = rs.getString("nama_supplier");
                
                dtm.addRow(new String[]{no+"",id_barang,nama_barang,nama_kategori,harga_beli,stock_barang,nama_supplier});
                no++;
            }
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
        table_barang.setModel(dtm);
        
    }
    
    public void ShowImage(){
        try{
            byte[] imageBytes;
            Image image;
            String query = "SELECT * FROM barang where id_barang=?";
            PreparedStatement pst = koneksi.prepareStatement(query);
            pst.setString(1, (String)text_cari.getText());
            ResultSet rs =  pst.executeQuery();
            while (rs.next()){
                imageBytes=rs.getBytes("gambar");
                image=getToolkit().createImage(imageBytes).getScaledInstance(label_gambar.getWidth(), label_gambar.getHeight(), Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(image);
                label_gambar.setIcon(icon);
               
            }
            pst.close();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void TambahStock(){
        String cari = text_cari.getText();
        String nama_barang = text_nama.getText();
        String qty = text_stock.getText();
        String tanggal = label_tanggal.getText();
        String harga_beli = text_hargabeli.getText();
        String subtotal = text_subtotal.getText();
        
        try{
            Statement stmt = koneksi.createStatement();
            String query = "INSERT INTO barang_masuk(id_barang, nama_barang, harga_beli, qty, subtotal, tanggal)"
                         + "VALUES('"+cari+"','"+nama_barang+"','"+harga_beli+"','"+qty+"','"+subtotal+"','"+tanggal+"')";
            System.out.println(query);
            int berhasil = stmt.executeUpdate(query);
            if(berhasil == 1){
                UpdateStock();
            }else{
                JOptionPane.showMessageDialog(null, "Input Gagal!");
            }
        } catch (SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Queri Error!");
        }
    }
    
    public void UpdateStock(){
        String cari = text_cari.getText();
        String qty = text_stock.getText();
        
        try{
            Statement stmt = koneksi.createStatement();
            String query = "UPDATE barang SET stock_barang = stock_barang + "+qty+" WHERE id_barang = '"+cari+"'";
            
            System.out.println(query);
            int berhasil = stmt.executeUpdate(query);
            if(berhasil == 1){
                JOptionPane.showMessageDialog(null, "Input Sukses!");
            }else{
                JOptionPane.showMessageDialog(null, "Data Gagal!");
            }
        } catch (SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Queri Error!");
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
        button_kembali = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_barang = new javax.swing.JTable();
        button_kembali1 = new javax.swing.JButton();
        label_tanggal = new javax.swing.JLabel();
        text_nama = new javax.swing.JTextField();
        text_cari = new javax.swing.JTextField();
        button_tambah = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        text_stock = new javax.swing.JTextField();
        label_gambar = new javax.swing.JLabel();
        text_hargabeli = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        text_subtotal = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        button_kembali.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        button_kembali.setText("Kembali");
        button_kembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_kembaliActionPerformed(evt);
            }
        });

        table_barang.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        table_barang.setModel(new javax.swing.table.DefaultTableModel(
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
        table_barang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_barangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table_barang);

        button_kembali1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        button_kembali1.setText("Cari");
        button_kembali1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_kembali1ActionPerformed(evt);
            }
        });

        label_tanggal.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_tanggal.setText("Tanggal");

        text_nama.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        text_cari.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        text_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_cariActionPerformed(evt);
            }
        });

        button_tambah.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        button_tambah.setText("Tambah");
        button_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_tambahActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Nama Barang");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Qty");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Cari Berdasarkan ID");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Tambah Stock");

        text_stock.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        text_stock.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                text_stockCaretUpdate(evt);
            }
        });
        text_stock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_stockActionPerformed(evt);
            }
        });

        label_gambar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_gambar.setText("                     IMAGE");

        text_hargabeli.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        text_hargabeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_hargabeliActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("Harga Beli");

        text_subtotal.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        text_subtotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_subtotalActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Subtotal");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(533, 533, 533)
                        .addComponent(jLabel4))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1)
                        .addComponent(text_nama)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(text_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(button_kembali1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(button_kembali, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel2))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label_tanggal))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 935, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(text_hargabeli, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5))
                                    .addGap(6, 6, 6)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(text_stock, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(6, 6, 6)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel6)
                                        .addComponent(text_subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(button_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(label_gambar, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(text_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(button_kembali1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(button_kembali, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label_tanggal))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(text_nama, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel6)
                        .addGap(2, 2, 2)
                        .addComponent(button_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(text_hargabeli, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(text_stock, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(text_subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(label_gambar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 3, 14, 0);
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void text_stockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_stockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_stockActionPerformed

    private void button_kembali1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_kembali1ActionPerformed
        LihatData();
    }//GEN-LAST:event_button_kembali1ActionPerformed

    private void text_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_cariActionPerformed
        LihatData();
    }//GEN-LAST:event_text_cariActionPerformed

    private void button_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_tambahActionPerformed
        TambahStock();
        text_nama.setText(null);
        text_stock.setText(null);
        LihatData();
        ShowImage();
    }//GEN-LAST:event_button_tambahActionPerformed

    private void table_barangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_barangMouseClicked
        int baris = table_barang.rowAtPoint(evt.getPoint());
        
        String id_barang = table_barang.getValueAt(baris, 1).toString();
        text_cari.setText(id_barang);
        
        String nama_barang = table_barang.getValueAt(baris, 2).toString();
        text_nama.setText(nama_barang);
        
        String harga_beli = table_barang.getValueAt(baris, 4).toString();
        text_hargabeli.setText(harga_beli);
        
        ShowImage();
        
    }//GEN-LAST:event_table_barangMouseClicked

    private void button_kembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_kembaliActionPerformed
        data_barang variable = new data_barang();
        variable.show();
        dispose();
    }//GEN-LAST:event_button_kembaliActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowActivated

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        setExtendedState(transaksi.MAXIMIZED_BOTH);
    }//GEN-LAST:event_formWindowOpened

    private void text_hargabeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_hargabeliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_hargabeliActionPerformed

    private void text_subtotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_subtotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_subtotalActionPerformed

    private void text_stockCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_text_stockCaretUpdate
        Double harga, subtotal;
        Integer qty;
        harga = Double.parseDouble(text_hargabeli.getText().toString());
        qty = Integer.parseInt(text_stock.getText().toString());
        
        subtotal = harga * qty;
        text_subtotal.setText(Double.toString(subtotal));
    }//GEN-LAST:event_text_stockCaretUpdate

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
            java.util.logging.Logger.getLogger(stock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(stock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(stock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(stock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new stock().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_kembali;
    private javax.swing.JButton button_kembali1;
    private javax.swing.JButton button_tambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label_gambar;
    private javax.swing.JLabel label_tanggal;
    private javax.swing.JTable table_barang;
    private javax.swing.JTextField text_cari;
    private javax.swing.JTextField text_hargabeli;
    private javax.swing.JTextField text_nama;
    private javax.swing.JTextField text_stock;
    private javax.swing.JTextField text_subtotal;
    // End of variables declaration//GEN-END:variables
}
