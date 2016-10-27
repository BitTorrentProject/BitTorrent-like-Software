
package GraphicInterface;

import BusinessLogic.Machine;
import com.sun.glass.events.KeyEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.ImageIcon;
import BusinessLogic.UploadingFile;

/**
 *
 * @author admin
 */
public class MainInterface extends javax.swing.JFrame {

    /**
     * Creates new form MainInterface
     */
    DefaultListModel listModelResultFile=new DefaultListModel();
    DefaultListModel listModelProcessFile=new DefaultListModel();
    InetAddress addressIP;
    Machine m = new Machine();
    
              
    public MainInterface() {
        initComponents();
        setVisible(true);
        setLocation(500, 200);
        setTitle("BitTorrent");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tfSearch.getParent().requestFocus();
        tfYourIP.setText(getIP());
        this.loadFilesFromLocalBitTorrent();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    public void loadFilesFromLocalBitTorrent()
    {
        File folder = new File("BitTorrent");
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                UploadingFile UploadedFile = new UploadingFile(fileEntry);
                m.AddFile(UploadedFile);
                
              
                listModelProcessFile.addElement(UploadedFile.GetName());
                listProcessFile.setModel(listModelProcessFile);
                
                
                System.out.println(UploadedFile.GetName());
            } else {
                //System.out.println(fileEntry.getName());
            }
        }
        System.out.println(m.GetFiles().size());
    }
    public String getIP()
    {
        try
        {
            addressIP = InetAddress.getLocalHost();
            
        }
    catch(Exception e)
        {
            e.printStackTrace();
        }
        return addressIP.getHostAddress().toString();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        tfSearch = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        listResultFile = new javax.swing.JList<>();
        btnAddFile = new javax.swing.JButton();
        btnDeleteFile = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listProcessFile = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        tfYourIP = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tfSearch.setText("Search");
        tfSearch.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tfSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tfSearchMousePressed(evt);
            }
        });
        tfSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfSearchActionPerformed(evt);
            }
        });
        tfSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfSearchKeyPressed(evt);
            }
        });

        listResultFile.setToolTipText("Kết quả tìm kiếm");
        jScrollPane2.setViewportView(listResultFile);

        btnAddFile.setBackground(new java.awt.Color(0, 0, 255));
        btnAddFile.setFont(new java.awt.Font(".VnBook-AntiquaH", 1, 12)); // NOI18N
        btnAddFile.setForeground(new java.awt.Color(255, 255, 255));
        btnAddFile.setText("Upload");
        btnAddFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFileActionPerformed(evt);
            }
        });

        btnDeleteFile.setBackground(new java.awt.Color(255, 0, 0));
        btnDeleteFile.setFont(new java.awt.Font(".VnBook-AntiquaH", 1, 12)); // NOI18N
        btnDeleteFile.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteFile.setText("Delete");
        btnDeleteFile.setPreferredSize(new java.awt.Dimension(60, 20));
        btnDeleteFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteFileActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(listProcessFile);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Searching result");

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("File list");
        jLabel4.setToolTipText("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnAddFile, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 319, Short.MAX_VALUE)
                        .addComponent(btnDeleteFile, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addComponent(tfSearch)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jSeparator1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(tfSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(10, 10, 10)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddFile, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteFile, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setText("Ver: 1.0.1");
        jLabel1.setEnabled(false);

        tfYourIP.setEnabled(false);

        jLabel2.setText("Your IP:");
        jLabel2.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(tfYourIP, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel1))
                    .addComponent(tfYourIP, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddFileActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser=new JFileChooser();
        chooser.showOpenDialog(null);
        File file=chooser.getSelectedFile();
        String fileName=file.getName();
        try {
            
            if(fileName!=null)
                JOptionPane.showMessageDialog(null, "File Uploaded", "Message", JOptionPane.YES_OPTION);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to upload", "Message", JOptionPane.ERROR_MESSAGE);
            return;
        }
        listModelProcessFile.addElement(fileName);
        
        listProcessFile.setModel(listModelProcessFile);
       
        UploadingFile upLoadingFile=new UploadingFile(file);
        m.AddFile(upLoadingFile);
    }//GEN-LAST:event_btnAddFileActionPerformed

    private void btnDeleteFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteFileActionPerformed
        // TODO add your handling code here:
        String fileName; // tên file sẽ xóa
        int pos=-1;
        pos=listProcessFile.getSelectedIndex();
        if(pos==-1)return;
        fileName=listModelProcessFile.get(pos).toString();
        listModelProcessFile.remove(pos);
        listProcessFile.setModel(listModelProcessFile);
        System.out.println(fileName);
        File file = new File("BitTorrent//" + fileName);
//          kiem tra nếu file tồn tại thì xóa
        if (file.exists()) {
            file.delete();
            m.RemoveFileAt(pos);
            JOptionPane.showMessageDialog(null, "File Deleted", "Message", JOptionPane.YES_OPTION);
        } else {
            JOptionPane.showMessageDialog(null, "File Not Exist","Error",JOptionPane.ERROR_MESSAGE);
            
        }
    }//GEN-LAST:event_btnDeleteFileActionPerformed

    private void tfSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfSearchActionPerformed

    private void tfSearchMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfSearchMousePressed
        // TODO add your handling code here:
        tfSearch.setText("");
    }//GEN-LAST:event_tfSearchMousePressed

    private void tfSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfSearchKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
        {
            if(tfSearch.getText().equals("")) JOptionPane.showConfirmDialog(null, "Chưa nhập tên tìm kiếm");
        }
    }//GEN-LAST:event_tfSearchKeyPressed

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
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainInterface().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddFile;
    private javax.swing.JButton btnDeleteFile;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JList<String> listProcessFile;
    private javax.swing.JList<String> listResultFile;
    private javax.swing.JTextField tfSearch;
    private javax.swing.JLabel tfYourIP;
    // End of variables declaration//GEN-END:variables
}
