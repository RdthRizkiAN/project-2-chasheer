/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pointofsale;

import java.awt.Color;
import java.awt.Image;
import com.mysql.jdbc.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Date;  
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import javax.swing.ImageIcon;
import javax.swing.table.TableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;


/**
 *
 * @author ilman30
 */
public class transaksi extends javax.swing.JFrame {

    /**
     * Creates new form transaksi
     */
    
    
    Connection koneksi;
    public transaksi() {
        
        initComponents();
        koneksi = DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db_pos");
        DetailTransaksi();
        Level();
        text_idtransaksi.setEditable(false);
        text_iddetail.setEditable(false);
        text_namabarang.setEditable(false);
        text_harga.setEditable(false);
        text_subtotal.setEditable(false);
        text_total.setEditable(false);
        text_total.setText("1");
        text_kembali.setEditable(false);
        text_stok.setEditable(false);
        text_opsi.setEditable(false);
        text_opsi.setVisible(false);
        text_opsi.setText("0");
        text_member.setEditable(false);
        text_member.setVisible(false);
        text_member.setText("0");
        text_diskon.setEditable(false);
        text_diskon.setVisible(false);
        text_diskon.setText("0");
        button_save.setEnabled(false);
        button_finish.setEnabled(false);
        ShowOpsiPembayaran();
        ShowMember();
        ShowDiskon();
        IdTransaksi();
        tanggal();
        total();
        String ID = login.getUserLogin();
        String level = login.getLevel();
        label_user.setText(ID);
        label_level.setText(level);
        Level();
        button();
    }
    
    private void ShowOpsiPembayaran(){
        try{
            Statement stmt = koneksi.createStatement();
            String query = "SELECT * FROM opsi_pembayaran ORDER BY id_opsi ASC";
            ResultSet rs =  stmt.executeQuery(query);
            while(rs.next()){
                Object[] ob = new Object[3];
                ob[0] = rs.getString(2);
            
                combo_opsi.addItem(ob[0]);                                      
            }
            rs.close(); stmt.close();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void ShowDetailOpsiPembayaran(){
        try{
            String query = "SELECT * FROM opsi_pembayaran where nama_opsi=?";
            PreparedStatement pst = koneksi.prepareStatement(query);
            pst.setString(1, (String)combo_opsi.getSelectedItem());
            ResultSet rs =  pst.executeQuery();
            while (rs.next()){
                text_opsi.setText(rs.getString("diskon"));
            }
            pst.close();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void ShowMember(){
        try{
            Statement stmt = koneksi.createStatement();
            String query = "SELECT * FROM jenis_member ORDER BY id_jenismember ASC";
            ResultSet rs =  stmt.executeQuery(query);
            while(rs.next()){
                Object[] ob = new Object[3];
                ob[0] = rs.getString(2);
            
                combo_member.addItem(ob[0]);                                      
            }
            rs.close(); stmt.close();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void ShowDetailMember(){
        try{
            String query = "SELECT * FROM jenis_member where nama_jenismember=?";
            PreparedStatement pst = koneksi.prepareStatement(query);
            pst.setString(1, (String)combo_member.getSelectedItem());
            ResultSet rs =  pst.executeQuery();
            while (rs.next()){
                text_member.setText(rs.getString("diskon"));
            }
            pst.close();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void ShowDiskon(){
        try{
            Statement stmt = koneksi.createStatement();
            String query = "SELECT * FROM diskon ORDER BY id_diskon ASC";
            ResultSet rs =  stmt.executeQuery(query);
            while(rs.next()){
                Object[] ob = new Object[3];
                ob[0] = rs.getString(2);
            
                combo_diskon.addItem(ob[0]);                                      
            }
            rs.close(); stmt.close();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void ShowDetailDiskon(){
        try{
            String query = "SELECT * FROM diskon where nama_diskon=?";
            PreparedStatement pst = koneksi.prepareStatement(query);
            pst.setString(1, (String)combo_diskon.getSelectedItem());
            ResultSet rs =  pst.executeQuery();
            while (rs.next()){
                text_diskon.setText(rs.getString("diskon"));
            }
            pst.close();
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void kosong(){
        text_stok.setText(null);
        text_idbarang.setText(null);
        text_namabarang.setText(null);
        text_harga.setText(null);
        text_qty.setText("1");
        text_subtotal.setText(null);
        text_total.setText("1");
        text_bayar.setText(null);
        text_kembali.setText(null);
        label_gambar.setIcon(null);
        button_finish.setEnabled(false);
        ShowOpsiPembayaran();
        ShowMember();
        ShowDiskon();
        DetailTransaksi();
        IdTransaksi();
        tanggal();
        total();
    }
    
    private void Level(){
        if(label_level.getText().equals("Admin")){
            button_delete.setEnabled(true);
        }else if(label_level.getText().equals("Gudang")){
            button_delete.setEnabled(false);
        }else if(label_level.getText().equals("Kasir")){
            button_delete.setEnabled(false);
        }
    }
    
    private void button(){
        if (label_level.getText().equals("Admin")){
            button_admin.setLabel("Login Kasir");
        } else if (label_level.getText().equals("Kasir")){
            button_admin.setLabel("Login Admin");
        }
    }
    
    public void TambahDetail(){
        String id_detail = text_stok.getText();
        String id_barang = text_idbarang.getText();
        String nama_barang = text_namabarang.getText();
        String harga_barang = text_harga.getText();
        String qty = text_qty.getText();
        String subtotal = text_subtotal.getText();
        
        try{
            Statement stmt = koneksi.createStatement();
            String query = "INSERT INTO transaksi(id_barang, nama_barang, harga_barang ,qty, subtotal,status)"
                         + "VALUES('"+id_barang+"','"+nama_barang+"','"+harga_barang+"','"+qty+"','"+subtotal+"','1')";
            System.out.println(query);
            int berhasil = stmt.executeUpdate(query);
            if(berhasil == 1){
                JOptionPane.showMessageDialog(null, "Data Tersimpan!");
            }else{
                JOptionPane.showMessageDialog(null, "Input Gagal!");
            }
        } catch (SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi Kesalahan Database!");
        }
    }
    
    DefaultTableModel dtm;
    public void DetailTransaksi(){
        String[] kolom = {"No","ID", "ID Barang", "Nama Barang", "Harga" , "Qty", "Sub Total"};
        dtm = new DefaultTableModel(null, kolom);
        try{
            Statement stmt = koneksi.createStatement();
            String query = "SELECT * FROM transaksi WHERE status = 1";
            ResultSet rs =  stmt.executeQuery(query);
            int no = 1;
            while (rs.next()){
                String id_detail = rs.getString("id_detail");
                String id_barang = rs.getString("id_barang");
                String nama_barang = rs.getString("nama_barang");
                String harga_barang = rs.getString("harga_barang");
                String qty = rs.getString("qty");
                String subtotal = rs.getString("subtotal");
                
                dtm.addRow(new String[]{no+"",id_detail,id_barang,nama_barang,harga_barang,qty,subtotal});
                no++;
            }
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
        table_detail.setModel(dtm);
    
    }
    
    public void ShowDataFromId(){
        try{
            byte[] imageBytes;
            Image image;
            String query = "SELECT * FROM barang where id_barang=?";
            PreparedStatement pst = koneksi.prepareStatement(query);
            pst.setString(1, (String)text_idbarang.getText());
            ResultSet rs =  pst.executeQuery();
            while (rs.next()){
                text_namabarang.setText(rs.getString("nama_barang"));
                text_harga.setText(rs.getString("harga_barang"));
                text_stok.setText(rs.getString("stock_barang"));
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
    
    public void ShowImage(){
        try{
            byte[] imageBytes;
            Image image;
            String query = "SELECT * FROM barang where id_barang=?";
            PreparedStatement pst = koneksi.prepareStatement(query);
            pst.setString(1, (String)text_idbarang.getText());
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
    
    
    private void IdTransaksi(){
        try {
            Statement stmt = koneksi.createStatement();
            String query = "SELECT * FROM struk order by id_transaksi desc";
            ResultSet rs =  stmt.executeQuery(query);
            if (rs.next()) {
                String id_transaksi = rs.getString("id_transaksi").substring(3);
                String AN = "" + (Integer.parseInt(id_transaksi) + 1);
                String Nol = "";

                if(AN.length()==1)
                {Nol = "000";}
                else if(AN.length()==2)
                {Nol = "00";}
                else if(AN.length()==3)
                {Nol = "0";}
                else if(AN.length()==4)
                {Nol = "";}

               text_idtransaksi.setText("TRS" + Nol + AN);
            } else {
               text_idtransaksi.setText("TRS0001");
            }

           }catch (SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi Kesalahan Pada Query!");
           }
    }
    
    public void tanggal(){
        Date ys=new Date();
        SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
        text_tanggal.setText(s.format(ys));
    }
    
    public void total(){
        
        int jumlahBaris = table_detail.getRowCount();
        int totalBiaya = 0;
        int subtotal;
        TableModel tabelModel;
        tabelModel = table_detail.getModel();
        for (int i=0; i<jumlahBaris; i++){
            subtotal = Integer.parseInt(tabelModel.getValueAt(i, 6).toString());
            totalBiaya = totalBiaya + (subtotal);
        }
            text_total.setText(String.valueOf(totalBiaya));
    }
    
    public void HapusData(){
        String id_detail = text_iddetail.getText();
        try{
            // TODO add your handling code here:
            Statement stmt = koneksi.createStatement();
            String query = "DELETE FROM transaksi WHERE id_detail = '"+id_detail+"';";
            int berhasil = stmt.executeUpdate(query);
            if(berhasil == 1){
                JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus!");
                dtm.getDataVector().removeAllElements();
                DetailTransaksi();
            }else{
                JOptionPane.showMessageDialog(null, "Data Gagal Dihapus!");
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public void struk(){
        String id_transaksi = text_idtransaksi.getText();
        String tanggal = text_tanggal.getText();
        String total = text_total.getText();
        String bayar = text_bayar.getText();
        String kembali = text_kembali.getText();
        String nama_opsi = combo_opsi.getSelectedItem().toString();
        String diskon_opsi = text_opsi.getText();
        String nama_jenismember = combo_member.getSelectedItem().toString();
        String diskon_member = text_member.getText();
        String nama_diskon = combo_diskon.getSelectedItem().toString();
        String diskon = text_diskon.getText();
        
        try{
            Statement stmt = koneksi.createStatement();
            String query = "INSERT INTO struk(id_transaksi, tanggal, total, bayar, kembali, status, nama_opsi, diskon_opsi, nama_jenismember, diskon_member, nama_diskon, diskon)"
                         + "VALUES('"+id_transaksi+"','"+tanggal+"','"+total+"','"+bayar+"','"+kembali+"','1','"+nama_opsi+"','"+diskon_opsi+"','"+nama_jenismember+"','"+diskon_member+"','"+nama_diskon+"','"+diskon+"')";
            System.out.println(query);
            int berhasil = stmt.executeUpdate(query);
            if(berhasil == 1){
                Cetak();
                ResetTable();
            }else{
                JOptionPane.showMessageDialog(null, "Input Gagal!");
            }
        } catch (SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Query Error!");
        }
    }
    
    public void ResetTable(){
        String id_transaksi = text_total.getText();
        String tanggal = text_idtransaksi.getText();
        String total = text_tanggal.getText();
        
        try{
            Statement stmt = koneksi.createStatement();
            String query = "UPDATE transaksi SET status = 0 WHERE status = 1";
            
            System.out.println(query);
            int berhasil = stmt.executeUpdate(query);
            if(berhasil == 1){
                ResetStruk();
            }else{
                ResetStruk();
            }
        } catch (SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Querry Error!");
        }
    }
    
    public void ResetStruk(){
        String id_transaksi = text_total.getText();
        String tanggal = text_idtransaksi.getText();
        String total = text_tanggal.getText();
        
        try{
            Statement stmt = koneksi.createStatement();
            String query = "UPDATE struk SET status = 0 WHERE status = 1";
            
            System.out.println(query);
            int berhasil = stmt.executeUpdate(query);
            if(berhasil == 1){
                JOptionPane.showMessageDialog(null, "Data Berhasil Dirubah!");
                kosong();
            }else{
                JOptionPane.showMessageDialog(null, "Struk Gagal!");
            }
        } catch (SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Query Error!");
        }
    }
    
    public void Cetak(){
        try {
            JasperPrint jp = JasperFillManager.fillReport(getClass().getResourceAsStream("invoice2.jasper"), null, DatabaseConnection.getKoneksi("localhost", "3306", "root", "", "db_pos"));
            JasperViewer.viewReport(jp, false);
            } catch(Exception e) {
                JOptionPane.showMessageDialog(rootPane, e);
        }
    }
    
    public void HitungOpsi(){
        Double totalbefore, opsi, total;
        totalbefore = Double.parseDouble(text_total.getText().toString());
        opsi = Double.parseDouble(text_opsi.getText().toString());
        total = totalbefore - (totalbefore * opsi);
        text_total.setText(Double.toString(total));
    }
    
    public void HitungMember(){
        Double totalbefore, member, total;
        totalbefore = Double.parseDouble(text_total.getText().toString());
        member = Double.parseDouble(text_member.getText().toString());
        total = totalbefore - (totalbefore * member);
        text_total.setText(Double.toString(total));
    }
    
    public void HitungDiskon(){
        Double totalbefore, diskon, total;
        totalbefore = Double.parseDouble(text_total.getText().toString());
        diskon = Double.parseDouble(text_diskon.getText().toString());
        total = totalbefore - (totalbefore * diskon);
        text_total.setText(Double.toString(total));
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
        button_save = new javax.swing.JButton();
        button_finish = new javax.swing.JButton();
        button_exit = new javax.swing.JButton();
        text_tanggal = new javax.swing.JLabel();
        button_delete = new javax.swing.JButton();
        text_stok = new javax.swing.JTextField();
        text_idbarang = new javax.swing.JTextField();
        text_harga = new javax.swing.JTextField();
        button_refresh = new javax.swing.JButton();
        text_qty = new javax.swing.JTextField();
        text_namabarang = new javax.swing.JTextField();
        text_subtotal = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        text_idtransaksi = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        label_gambar = new javax.swing.JLabel();
        text_iddetail = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        label_user = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_detail = new javax.swing.JTable();
        label_level = new javax.swing.JLabel();
        button_admin = new javax.swing.JButton();
        combo_opsi = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        text_opsi = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        text_bayar = new javax.swing.JTextField();
        text_kembali = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        text_total = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        combo_member = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        text_member = new javax.swing.JTextField();
        combo_diskon = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        text_diskon = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        button_save.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        button_save.setText("Save");
        button_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_saveActionPerformed(evt);
            }
        });

        button_finish.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        button_finish.setText("Selesai");
        button_finish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_finishActionPerformed(evt);
            }
        });

        button_exit.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        button_exit.setText("Keluar");
        button_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_exitActionPerformed(evt);
            }
        });

        text_tanggal.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        text_tanggal.setText("Tanggal");

        button_delete.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        button_delete.setText("Delete");
        button_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_deleteActionPerformed(evt);
            }
        });

        text_stok.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        text_stok.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                text_stokCaretUpdate(evt);
            }
        });

        text_idbarang.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        text_idbarang.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                text_idbarangCaretUpdate(evt);
            }
        });
        text_idbarang.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                text_idbarangInputMethodTextChanged(evt);
            }
        });
        text_idbarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_idbarangActionPerformed(evt);
            }
        });

        text_harga.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        text_harga.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                text_hargaInputMethodTextChanged(evt);
            }
        });

        button_refresh.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        button_refresh.setText("Refresh");
        button_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_refreshActionPerformed(evt);
            }
        });

        text_qty.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        text_qty.setText("1");
        text_qty.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        text_qty.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                text_qtyCaretUpdate(evt);
            }
        });
        text_qty.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                text_qtyMouseClicked(evt);
            }
        });
        text_qty.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                text_qtyInputMethodTextChanged(evt);
            }
        });
        text_qty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_qtyActionPerformed(evt);
            }
        });

        text_namabarang.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        text_namabarang.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                text_namabarangInputMethodTextChanged(evt);
            }
        });
        text_namabarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_namabarangActionPerformed(evt);
            }
        });

        text_subtotal.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        text_subtotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_subtotalActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel2.setText("ID Barang");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setText("Harga");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel5.setText("Qty");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel6.setText("Sub Total");

        text_idtransaksi.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel7.setText("ID Transaksi");

        label_gambar.setBackground(new java.awt.Color(153, 153, 153));
        label_gambar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsale/images/bgProduct.png"))); // NOI18N
        label_gambar.setText("Image");

        text_iddetail.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel10.setText("Stok");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel3.setText("Nama Barang");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setText("Nama Kasir: ");

        label_user.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_user.setText("User");

        table_detail.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        table_detail.setModel(new javax.swing.table.DefaultTableModel(
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
        table_detail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_detailMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table_detail);

        label_level.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label_level.setText("Level");

        button_admin.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        button_admin.setText("Login Admin");
        button_admin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_adminActionPerformed(evt);
            }
        });

        combo_opsi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_opsiActionPerformed(evt);
            }
        });

        jLabel1.setText("jLabel1");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel14.setText("Opsi Pembayaran");

        text_opsi.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                text_opsiCaretUpdate(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel11.setText("Bayar");

        text_bayar.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        text_bayar.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                text_bayarCaretUpdate(evt);
            }
        });
        text_bayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_bayarActionPerformed(evt);
            }
        });

        text_kembali.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        text_kembali.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                text_kembaliCaretUpdate(evt);
            }
        });
        text_kembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_kembaliActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel12.setText("Kembali");

        text_total.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel9.setText("Total");

        combo_member.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_memberActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel15.setText("Member");

        text_member.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                text_memberCaretUpdate(evt);
            }
        });

        combo_diskon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_diskonActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel16.setText("Diskon");

        text_diskon.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                text_diskonCaretUpdate(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(text_namabarang, javax.swing.GroupLayout.PREFERRED_SIZE, 763, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(text_idbarang, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel2))
                                        .addGap(6, 6, 6)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(text_qty, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel5))
                                        .addGap(6, 6, 6)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(text_harga, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel4))
                                        .addGap(6, 6, 6)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(text_subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel10)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(text_stok, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(text_iddetail, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(174, 174, 174))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(label_user)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(text_tanggal)
                                .addGap(63, 63, 63)))
                        .addComponent(label_level)
                        .addGap(18, 18, 18)
                        .addComponent(button_admin, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_exit, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(668, 668, 668)
                                .addComponent(button_refresh))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(button_save, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(button_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(label_gambar, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(text_idtransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(text_opsi))
                            .addComponent(combo_opsi, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(text_member, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(combo_member, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(text_diskon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(combo_diskon, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addGap(79, 79, 79)
                                        .addComponent(jLabel12)
                                        .addGap(74, 74, 74)
                                        .addComponent(jLabel9))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(text_bayar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(text_kembali, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(text_total)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(button_finish, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(label_user))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel10)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(button_exit, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(button_admin, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(label_level)
                        .addComponent(text_tanggal)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(text_stok, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(text_subtotal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(text_harga, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(text_qty, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(text_idbarang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(text_iddetail, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(9, 9, 9)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(text_namabarang, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(button_save, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(button_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(button_refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addComponent(label_gambar, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(button_finish, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(text_opsi, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(combo_opsi, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(text_idtransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(text_member, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(combo_member, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(text_diskon, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(combo_diskon, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(text_bayar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(text_kembali, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(text_total, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 11, 11, 14);
        getContentPane().add(jPanel1, gridBagConstraints);

        setSize(new java.awt.Dimension(1334, 729));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void text_kembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_kembaliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_kembaliActionPerformed

    private void text_bayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_bayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_bayarActionPerformed

    private void text_bayarCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_text_bayarCaretUpdate
        Double total, bayar, kembali;
        total = Double.parseDouble(text_total.getText().toString());
        bayar = Double.parseDouble(text_bayar.getText().toString());
        kembali = bayar - total;
        text_kembali.setText(Double.toString(kembali));
    }//GEN-LAST:event_text_bayarCaretUpdate

    private void text_subtotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_subtotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_subtotalActionPerformed

    private void text_namabarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_namabarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_namabarangActionPerformed

    private void text_namabarangInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_text_namabarangInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_text_namabarangInputMethodTextChanged

    private void text_qtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_qtyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_qtyActionPerformed

    private void text_qtyInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_text_qtyInputMethodTextChanged
        Double har, sub;
        Integer qty;
        har = Double.parseDouble(text_harga.getText().toString());
        qty = Integer.parseInt(text_qty.getText().toString());
        sub  = har * qty;
        text_subtotal.setText(Double.toString(sub));
    }//GEN-LAST:event_text_qtyInputMethodTextChanged

    private void text_qtyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_text_qtyMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_text_qtyMouseClicked

    private void text_qtyCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_text_qtyCaretUpdate
        Double har, sub;
        Integer stok, qty;
        har = Double.parseDouble(text_harga.getText().toString());
        qty = Integer.parseInt(text_qty.getText().toString());
        stok  = Integer.parseInt(text_stok.getText().toString());
        sub  = har * qty;
        text_subtotal.setText(Double.toString(sub));
        if (qty > stok){
            button_save.setEnabled(false);
        }
        else{
            button_save.setEnabled(true);
        }
    }//GEN-LAST:event_text_qtyCaretUpdate

    private void button_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_refreshActionPerformed
        DetailTransaksi();
        kosong();
        IdTransaksi();
    }//GEN-LAST:event_button_refreshActionPerformed

    private void text_hargaInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_text_hargaInputMethodTextChanged

    }//GEN-LAST:event_text_hargaInputMethodTextChanged

    private void text_idbarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_idbarangActionPerformed
        ShowDataFromId();
        
    }//GEN-LAST:event_text_idbarangActionPerformed

    private void text_idbarangInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_text_idbarangInputMethodTextChanged

    }//GEN-LAST:event_text_idbarangInputMethodTextChanged

    private void text_idbarangCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_text_idbarangCaretUpdate

    }//GEN-LAST:event_text_idbarangCaretUpdate

    private void button_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_deleteActionPerformed
        HapusData();
    }//GEN-LAST:event_button_deleteActionPerformed

    private void button_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_exitActionPerformed
        admin variable = new admin();
        variable.show();
        dispose();
    }//GEN-LAST:event_button_exitActionPerformed

    private void button_finishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_finishActionPerformed
        struk();
        kosong();
        DetailTransaksi();
        IdTransaksi();

    }//GEN-LAST:event_button_finishActionPerformed

    private void button_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_saveActionPerformed
        Double qty, stok;
        qty = Double.parseDouble(text_qty.getText().toString());
        stok = Double.parseDouble(text_stok.getText().toString());
        
        if (qty > stok){
            JOptionPane.showMessageDialog(null, "Jumlah yang dipesan melebihi stok yang tersedia!");
            DetailTransaksi();
        }
        else{
            TambahDetail();
            DetailTransaksi();
            total();
        }
        
        
    }//GEN-LAST:event_button_saveActionPerformed

    private void text_kembaliCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_text_kembaliCaretUpdate
        Double kembali;
        kembali = Double.parseDouble(text_kembali.getText().toString());
        if (kembali >= 0)
            {
                button_finish.setEnabled(true);
            }
        else
        {
            button_finish.setEnabled(false);
        }
    }//GEN-LAST:event_text_kembaliCaretUpdate

    private void text_stokCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_text_stokCaretUpdate
        Double stok;
        stok = Double.parseDouble(text_stok.getText().toString());
        if (stok > 0)
            {
                button_save.setEnabled(true);
            }
        else
        {
            button_save.setEnabled(false);
        }
    }//GEN-LAST:event_text_stokCaretUpdate

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        setExtendedState(transaksi.MAXIMIZED_BOTH);
    }//GEN-LAST:event_formWindowOpened

    private void table_detailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_detailMouseClicked
        int baris = table_detail.rowAtPoint(evt.getPoint());
        
        String id_detail = table_detail.getValueAt(baris, 1).toString();
        text_iddetail.setText(id_detail);
        
        String id_barang = table_detail.getValueAt(baris, 2).toString();
        text_idbarang.setText(id_barang);
        
        String nama_barang = table_detail.getValueAt(baris, 3).toString();
        text_namabarang.setText(nama_barang);
        
        String harga = table_detail.getValueAt(baris, 4).toString();
        text_harga.setText(harga);
        
        String qty = table_detail.getValueAt(baris, 5).toString();
        text_qty.setText(qty);
        
        String subtotal = table_detail.getValueAt(baris, 6).toString();
        text_subtotal.setText(subtotal);
        
        ShowImage();
    }//GEN-LAST:event_table_detailMouseClicked

    private void button_adminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_adminActionPerformed
        if (label_level.getText().equals("Admin")){
            login_kasir variable = new login_kasir();
            variable.show();
            dispose();
        }else if (label_level.getText().equals("Kasir")){
            login_admin variable = new login_admin();
            variable.show();
            dispose();
        }
        
    }//GEN-LAST:event_button_adminActionPerformed

    private void combo_opsiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_opsiActionPerformed
        ShowDetailOpsiPembayaran();
        total();
        HitungOpsi();
        HitungMember();
        HitungDiskon();
    }//GEN-LAST:event_combo_opsiActionPerformed

    private void text_opsiCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_text_opsiCaretUpdate
        
    }//GEN-LAST:event_text_opsiCaretUpdate

    private void combo_memberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_memberActionPerformed
        ShowDetailMember();
        total();
        HitungOpsi();
        HitungMember();
        HitungDiskon();
    }//GEN-LAST:event_combo_memberActionPerformed

    private void text_memberCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_text_memberCaretUpdate
        // TODO add your handling code here:
    }//GEN-LAST:event_text_memberCaretUpdate

    private void combo_diskonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_diskonActionPerformed
        ShowDetailDiskon();
        total();
        HitungOpsi();
        HitungMember();
        HitungDiskon();
    }//GEN-LAST:event_combo_diskonActionPerformed

    private void text_diskonCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_text_diskonCaretUpdate
        // TODO add your handling code here:
    }//GEN-LAST:event_text_diskonCaretUpdate

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
            java.util.logging.Logger.getLogger(transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new transaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_admin;
    private javax.swing.JButton button_delete;
    private javax.swing.JButton button_exit;
    private javax.swing.JButton button_finish;
    private javax.swing.JButton button_refresh;
    private javax.swing.JButton button_save;
    private javax.swing.JComboBox combo_diskon;
    private javax.swing.JComboBox combo_member;
    private javax.swing.JComboBox combo_opsi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label_gambar;
    private javax.swing.JLabel label_level;
    private javax.swing.JLabel label_user;
    private javax.swing.JTable table_detail;
    private javax.swing.JTextField text_bayar;
    private javax.swing.JTextField text_diskon;
    private javax.swing.JTextField text_harga;
    private javax.swing.JTextField text_idbarang;
    private javax.swing.JTextField text_iddetail;
    private javax.swing.JTextField text_idtransaksi;
    private javax.swing.JTextField text_kembali;
    private javax.swing.JTextField text_member;
    private javax.swing.JTextField text_namabarang;
    private javax.swing.JTextField text_opsi;
    private javax.swing.JTextField text_qty;
    private javax.swing.JTextField text_stok;
    private javax.swing.JTextField text_subtotal;
    private javax.swing.JLabel text_tanggal;
    private javax.swing.JTextField text_total;
    // End of variables declaration//GEN-END:variables
}
