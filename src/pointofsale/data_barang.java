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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
 * @author ilman30
 */
public class data_barang extends javax.swing.JFrame {

    /**
     * Creates new form data_barang
     */
    Connection koneksi;
    String s;
    public data_barang() {
        initComponents();
        koneksi = DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db_pos");
        LihatData();
        kosong();
        IdBarang();
        text_id.enable(false);
        ShowSupplier();
    }
    
    private void kosong(){
        text_id.setText(null);
        text_nama.setText(null);
        combo_kategori.setSelectedItem(this);
        text_harga.setText(null);
        text_stock.setText(null);
        combo_supplier.setSelectedItem(this);
        IdBarang();
    }
    
    public void ShowSupplier(){
        try{
            Statement stmt = koneksi.createStatement();
            String query = "SELECT * FROM supplier ORDER BY id_supplier ASC";
            ResultSet rs =  stmt.executeQuery(query);
            while(rs.next()){
                Object[] ob = new Object[3];
                ob[0] = rs.getString(2);
            
                combo_supplier.addItem(ob[0]);                                      
            }
            rs.close(); stmt.close();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    DefaultTableModel dtm;
    private void LihatData(){
        
        String[] kolom = {"No", "ID", "Nama Barang", "Kategori", "Harga Beli", "Harga Jual", "Stock", "Supplier", "Tanggal Kadaluarsa"};
        dtm = new DefaultTableModel(null, kolom);
        try{
            Statement stmt = koneksi.createStatement();
            String query = "SELECT * FROM barang";
            ResultSet rs =  stmt.executeQuery(query);
            int no = 1;
            while (rs.next()){
                String id_barang = rs.getString("id_barang");
                String nama_barang = rs.getString("nama_barang");
                String nama_kategori = rs.getString("nama_kategori");
                String harga_beli = rs.getString("harga_beli");
                String harga_barang = rs.getString("harga_barang");
                String stock_barang = rs.getString("stock_barang");
                String nama_supplier = rs.getString("nama_supplier");
                String kadaluarsa = rs.getString("kadaluarsa");
                
                dtm.addRow(new String[]{no+"",id_barang,nama_barang,nama_kategori,harga_beli,harga_barang,stock_barang,nama_supplier,kadaluarsa});
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
            pst.setString(1, (String)text_id.getText());
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
    
    public void Browse(){
        JFileChooser fileChooser = new JFileChooser();
         fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
         FileNameExtensionFilter filter = new FileNameExtensionFilter("*.IMAGE", "jpg","gif","png");
         fileChooser.addChoosableFileFilter(filter);
         int result = fileChooser.showSaveDialog(null);
         if(result == JFileChooser.APPROVE_OPTION){
             File selectedFile = fileChooser.getSelectedFile();
             String path = selectedFile.getAbsolutePath();
             label_gambar.setIcon(ResizeImage(path));
             s = path;
              }
         else if(result == JFileChooser.CANCEL_OPTION){
             System.out.println("No Data");
         }
    }
    
    public ImageIcon ResizeImage(String ImagePath){
        ImageIcon MyImage = new ImageIcon(ImagePath);
        Image img = MyImage.getImage();
        Image newImg = img.getScaledInstance(label_gambar.getWidth(), label_gambar.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        return image;
    }
    
    public void TambahData(){
        
        try{
            String query = "INSERT INTO barang (id_barang, nama_barang ,nama_kategori, harga_beli, harga_barang, stock_barang, nama_supplier, gambar, kadaluarsa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = koneksi.prepareStatement(query);
            InputStream is = new FileInputStream(new File(s));
            ps.setString(1, text_id.getText());
            ps.setString(2, text_nama.getText());
            ps.setString(3, combo_kategori.getSelectedItem().toString());
            ps.setString(4, text_hargabeli.getText());
            ps.setString(5, text_harga.getText());
            ps.setString(6, text_stock.getText());
            ps.setString(7, combo_supplier.getSelectedItem().toString());
            ps.setBlob(8,is);
            ps.setString(9, text_kadaluarsa.getDate().toString());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Input Success!");
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Input Failed!");
        } 
    }
    
    
    public void EditData(){
        String id_barang = text_id.getText();
        String nama_barang = text_nama.getText();
        String nama_kategori = combo_kategori.getSelectedItem().toString();
        String harga_beli = text_hargabeli.getText();
        String harga_barang = text_harga.getText();
        String stock_barang = text_stock.getText();
        String nama_supplier = combo_supplier.getSelectedItem().toString();
        String kadaluarsa = text_kadaluarsa.getDate().toString();
        //String gambar = label_gambar.setBlob();
        
        try{
            Statement stmt = koneksi.createStatement();
            String query = "UPDATE barang SET nama_barang = '"+nama_barang+"',"
                    + "nama_kategori = '"+nama_kategori+"',"
                    + "harga_beli = '"+harga_beli+"',"
                    + "harga_barang = '"+harga_barang+"',"
                    + "stock_barng = '"+stock_barang+"',"
                    + "nama_supplier = '"+nama_supplier+"' WHERE id_barang = '"+id_barang+"'";
            
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
    
    int baris;
    public void HapusData(){
        String id_barang = text_id.getText();
        try{
            // TODO add your handling code here:
            Statement stmt = koneksi.createStatement();
            String query = "DELETE FROM barang WHERE id_barang = '"+id_barang+"';";
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
    
    
    private void IdBarang(){
        try {
            Statement stmt = koneksi.createStatement();
            String query = "SELECT * FROM barang order by id_barang desc";
            ResultSet rs =  stmt.executeQuery(query);
            if (rs.next()) {
                String id_barang = rs.getString("id_barang").substring(3);
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

               text_id.setText("BRG" + Nol + AN);
            } else {
               text_id.setText("BRG0001");
            }

           }catch (SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi Kesalahan Pada Query!");
           }
    }
    
    public static Date getTanggalFromTable(JTable table, int kolom){
        JTable tabel = table;
        String str_tgl = String.valueOf(tabel.getValueAt(tabel.getSelectedRow(), kolom));
        Date tanggal = null;
        try {
            tanggal = new SimpleDateFormat("yyyy-MM-dd").parse(str_tgl);
        } catch (ParseException ex) {
            Logger.getLogger(data_barang.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tanggal;
        
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        button_exit = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_barang = new javax.swing.JTable();
        text_id = new javax.swing.JTextField();
        text_nama = new javax.swing.JTextField();
        text_stock = new javax.swing.JTextField();
        combo_kategori = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        text_harga = new javax.swing.JTextField();
        button_gambar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        button_tambah = new javax.swing.JButton();
        label_gambar = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        button_edit = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        button_hapus = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        button_reset = new javax.swing.JButton();
        text_hargabeli = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        combo_supplier = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        text_kadaluarsa = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel5.setText("Harga Jual");

        button_exit.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        button_exit.setText("Exit");
        button_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_exitActionPerformed(evt);
            }
        });

        table_barang.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
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
        jScrollPane2.setViewportView(table_barang);

        text_id.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        text_id.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                text_idCaretUpdate(evt);
            }
        });

        text_nama.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        text_stock.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        combo_kategori.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        combo_kategori.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kopi", "Susu" }));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel7.setText("Supplier");

        text_harga.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        button_gambar.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        button_gambar.setText("Browse");
        button_gambar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_gambarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Data Barang");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel8.setText("Gambar");

        button_tambah.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        button_tambah.setText("Tambah");
        button_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_tambahActionPerformed(evt);
            }
        });

        label_gambar.setText("Image");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel2.setText("ID Barang");

        button_edit.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        button_edit.setText("Edit");
        button_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_editActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel3.setText("Nama Barang");

        button_hapus.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        button_hapus.setText("Hapus");
        button_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_hapusActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setText("Kategori");

        button_reset.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        button_reset.setText("Reset");
        button_reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_resetActionPerformed(evt);
            }
        });

        text_hargabeli.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel9.setText("Harga Beli");

        combo_supplier.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        combo_supplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_supplierActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel10.setText("Stock");

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton1.setText("Tambah Stock");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        text_kadaluarsa.setDateFormatString("yyyy-MM-dd");
        text_kadaluarsa.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel6.setText("Tanggal Kadaluarsa");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(461, 461, 461)
                        .addComponent(button_exit, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1023, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(10, 10, 10)
                                .addComponent(button_gambar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(4, 4, 4)
                        .addComponent(label_gambar, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addComponent(text_id, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(10, 10, 10)
                                                    .addComponent(combo_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addComponent(jLabel2)
                                                    .addGap(427, 427, 427)
                                                    .addComponent(jLabel4)))
                                            .addGap(18, 18, 18)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel9)
                                                .addComponent(text_hargabeli, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(text_nama, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(combo_supplier, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(776, 776, 776)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(410, 410, 410))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(text_stock)
                                    .addGap(278, 278, 278))
                                .addComponent(jLabel6)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(text_kadaluarsa, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(22, 22, 22))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(text_harga)
                                            .addGap(18, 18, 18)))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(button_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(button_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(18, 18, 18)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(button_reset, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(button_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addComponent(jLabel5)))))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(button_exit, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel5)
                        .addComponent(jLabel9)))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(text_id, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(combo_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(button_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(text_harga, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(text_hargabeli, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(button_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6))
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(text_nama, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(text_kadaluarsa, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(button_reset, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(button_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(jLabel8))
                            .addComponent(button_gambar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(combo_supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(text_stock, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(label_gambar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 0, 15, 0);
        getContentPane().add(jPanel1, gridBagConstraints);

        setSize(new java.awt.Dimension(1344, 649));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void button_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_tambahActionPerformed
        TambahData();
        LihatData();
    }//GEN-LAST:event_button_tambahActionPerformed

    private void button_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_resetActionPerformed
        kosong();
    }//GEN-LAST:event_button_resetActionPerformed

    private void button_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_exitActionPerformed
        dispose();
        admin variable = new admin();
        variable.show();
    }//GEN-LAST:event_button_exitActionPerformed

    private void table_barangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_barangMouseClicked
        int baris = table_barang.rowAtPoint(evt.getPoint());
        
        String id_barang = table_barang.getValueAt(baris, 1).toString();
        text_id.setText(id_barang);
        
        String nama_barang = table_barang.getValueAt(baris, 2).toString();
        text_nama.setText(nama_barang);
        
        String nama_kategori = table_barang.getValueAt(baris, 3).toString();
        combo_kategori.setSelectedItem(nama_kategori);
        
        String harga_beli = table_barang.getValueAt(baris, 4).toString();
        text_hargabeli.setText(harga_beli);
        
        String harga_barang = table_barang.getValueAt(baris, 5).toString();
        text_harga.setText(harga_barang);
        
        String stock_barang = table_barang.getValueAt(baris, 6).toString();
        text_stock.setText(stock_barang);
        
        String supplier = table_barang.getValueAt(baris, 7).toString();
        combo_supplier.setSelectedItem(supplier);
        
        //String kadaluarsa = table_barang.getValueAt(baris, 9).toString();
        //text_kadaluarsa.setDate(kadaluarsa);
        text_kadaluarsa.setDate(getTanggalFromTable(table_barang, 8));
    }//GEN-LAST:event_table_barangMouseClicked

    private void button_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_editActionPerformed
        EditData();
        LihatData();
    }//GEN-LAST:event_button_editActionPerformed

    private void button_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_hapusActionPerformed
        HapusData();
    }//GEN-LAST:event_button_hapusActionPerformed

    private void button_gambarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_gambarActionPerformed
        Browse();
          
    }//GEN-LAST:event_button_gambarActionPerformed

    private void text_idCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_text_idCaretUpdate
        ShowImage();
    }//GEN-LAST:event_text_idCaretUpdate

    private void combo_supplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_supplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_combo_supplierActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        stock variable = new stock();
        variable.show();
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        setExtendedState(data_barang.MAXIMIZED_BOTH);
    }//GEN-LAST:event_formWindowOpened

   
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
            java.util.logging.Logger.getLogger(data_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(data_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(data_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(data_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new data_barang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_edit;
    private javax.swing.JButton button_exit;
    private javax.swing.JButton button_gambar;
    private javax.swing.JButton button_hapus;
    private javax.swing.JButton button_reset;
    private javax.swing.JButton button_tambah;
    private javax.swing.JComboBox combo_kategori;
    private javax.swing.JComboBox combo_supplier;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel label_gambar;
    private javax.swing.JTable table_barang;
    private javax.swing.JTextField text_harga;
    private javax.swing.JTextField text_hargabeli;
    private javax.swing.JTextField text_id;
    private com.toedter.calendar.JDateChooser text_kadaluarsa;
    private javax.swing.JTextField text_nama;
    private javax.swing.JTextField text_stock;
    // End of variables declaration//GEN-END:variables

    
}
