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
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author admin
 */
public class MainInterface extends javax.swing.JFrame implements ActionListener{
    /**
     * Creates new form MainInterface
     */
    DefaultListModel listModelResultFile=new DefaultListModel();
    
    Object[] columnFileList={"FileName","Size","Status"};
    DefaultTableModel tableModelFileList=new DefaultTableModel(columnFileList, 0);
    
    
    Object[] columnDownloadProcess={"FileName","Size","Process"};
    DefaultTableModel tableModelDownloadProcess=new DefaultTableModel(columnDownloadProcess,0);
    
    Object[] columnInforPeerConnect={"IP","Host Name","Status"};
    DefaultTableModel tableModelInforPeerConnect=new DefaultTableModel(columnInforPeerConnect, 0);
    
    
    
    InetAddress addressIP;
    String pathChooser="";
    Machine m = new Machine();

    JPopupMenu puMenu=new JPopupMenu();
    JMenuItem miOpen=new JMenuItem("Open");
    JMenuItem miDelete=new JMenuItem("Delete");
    JMenuItem miOpenLocation=new JMenuItem("Open Folder");
 
    public MainInterface() {
        initComponents();
        initPopupMenu();
        setVisible(true);
        setLocation(100, 100);
        setTitle("BitTorrent");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tfSearch.getParent().requestFocus();
        lYourIP.setText("Your IP: "+getYourIP());
        lYourHostName.setText("Your Host Name: "+getYourHostName());
        this.loadFilesFromLocalBitTorrent();
        setResizable(false);
      
       tableFileList.setComponentPopupMenu(puMenu);
       
       tableDownloadProcess.setModel(tableModelDownloadProcess);
       tableInforPeerConnect.setModel(tableModelInforPeerConnect);
       tableFileList.setModel(tableModelFileList);
       
       //tableFileList.setEnabled(true);
       //tableModelFileList.
  
       
       //jLabel2.setIcon(new ImageIcon(new ImageIcon("src/Image/rsz_upfile.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
       //btnUpFile.setIcon(new ImageIcon(new ImageIcon("src/Image/rsz_upfile.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
      // btnDeleteFile.setIcon(new ImageIcon(new ImageIcon("src/Image/rsz_remove.png").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
       
    }
    public void initPopupMenu()
    {
        
        puMenu.add(miOpen);
        puMenu.add(miDelete);
        puMenu.add(miOpenLocation);
        miOpen.addActionListener(this);
        miDelete.addActionListener(this);
        miOpenLocation.addActionListener(this);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void loadFilesFromLocalBitTorrent()
    {
        File folder = new File("BitTorrent");
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                // if the file does not contain extension .torrent
                if (!fileEntry.getName().endsWith(".torrent")) {
                    UploadingFile UploadedFile = new UploadingFile(fileEntry,0);
                    m.AddFile(UploadedFile);
                    //tableModelProcessFile.addElement(UploadedFile.GetName());
                    Object[] rowData={UploadedFile.GetName(),RoundFileSize(UploadedFile.GetSize()),"123"};
                    tableModelFileList.addRow(rowData);
                    //listModelProcessFile.addElement(UploadedFile.GetName() + "-----------------size: " + UploadedFile.GetSize() + "-----------------no of chunks: " + UploadedFile.GetChunks().size());
                    tableFileList.setModel(tableModelFileList);
                    
                    // write file info into file.torrent
                    try {
                        UploadedFile.WriteFileInfoToTorrent();
                    } catch (IOException ex) {
                        String[] Name = UploadedFile.GetName().split(".");
                        JOptionPane.showMessageDialog(null, Name[0], "Erorr", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Fail to Load Files From Local BitTorrent", "Erorr", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }
    public String getYourIP()
    {
        try
        {
            return InetAddress.getLocalHost().getHostAddress();
        }
    catch(Exception e)
        {
            e.printStackTrace();
        }
        return "Valid";
    }
    public String getYourHostName()
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
    catch(Exception e)
        {
            e.printStackTrace();
        }
        return "Valid";
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        puMenu1 = new javax.swing.JPopupMenu();
        jPanel1 = new javax.swing.JPanel();
        tfSearch = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableDownloadProcess = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableInforPeerConnect = new javax.swing.JTable();
        lYourIP = new javax.swing.JLabel();
        lYourHostName = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        btnUpFile = new javax.swing.JButton();
        btnDeleteFile = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableFileList = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tfSearch.setText("Search");
        tfSearch.setToolTipText("Nhập tên file tìm kiếm xong Enter");
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

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Downloads");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel5MouseExited(evt);
            }
        });

        tableDownloadProcess.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File", "Size", "Process"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tableDownloadProcess);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Download Process");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(tfSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Infor peer connecting");

        tableInforPeerConnect.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tableInforPeerConnect.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IP", "Host Name", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tableInforPeerConnect);

        lYourIP.setText("Your IP:");

        lYourHostName.setText("Your Host Name:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(lYourIP)
                        .addGap(140, 140, 140)
                        .addComponent(lYourHostName)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lYourIP)
                    .addComponent(lYourHostName))
                .addGap(323, 323, 323))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("File list");
        jLabel4.setToolTipText("");

        btnUpFile.setFont(new java.awt.Font(".VnBook-AntiquaH", 1, 12)); // NOI18N
        btnUpFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/rsz_upfile.png"))); // NOI18N
        btnUpFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpFileActionPerformed(evt);
            }
        });

        btnDeleteFile.setFont(new java.awt.Font(".VnBook-AntiquaH", 1, 12)); // NOI18N
        btnDeleteFile.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/rsz_remove.png"))); // NOI18N
        btnDeleteFile.setPreferredSize(new java.awt.Dimension(60, 20));
        btnDeleteFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteFileActionPerformed(evt);
            }
        });

        tableFileList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File Name", "Size", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableFileList);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(btnUpFile, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDeleteFile, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnDeleteFile, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUpFile, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {                                     
        openFileLocation();
    }                           

    private void btnDeleteFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteFileActionPerformed
        // TODO add your handling code here:
        String fileName; // tên file sẽ xóa
        int pos=-1;
        pos=tableFileList.getSelectedRow();
        if(pos==-1)return;
        fileName=tableModelFileList.getValueAt(pos, 0).toString();
        tableModelFileList.removeRow(pos);
        tableFileList.setModel(tableModelFileList);
        File file = new File("BitTorrent//" + fileName);
        File fileTorrrent = new File("BitTorrent//" + fileName + ".torrent");
        // kiem tra nếu file tồn tại thì xóa
        if (file.exists()) {
            file.delete();
            fileTorrrent.delete();
            m.RemoveFileAt(pos);
            JOptionPane.showMessageDialog(null, "File Deleted", "Message", JOptionPane.YES_OPTION);
        } else {
            JOptionPane.showMessageDialog(null, "File Not Exist","Error",JOptionPane.ERROR_MESSAGE);

        }
    }//GEN-LAST:event_btnDeleteFileActionPerformed

    private void listProcessFileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listProcessFileMouseClicked
        // TODO add your handling code here:
        /*if (evt.getModifiers() == MouseEvent.BUTTON1_MASK) {
            if (evt.getClickCount() == 2) {
                listProcessFile.getSelectedIndex();
                int pos = -1;
                pos = listProcessFile.getSelectedIndex();
                if (pos == -1) {
                    return;
                }
                File file = new File("BitTorrent//" + listModelProcessFile.get(pos).toString());
                try {
                    java.awt.Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (evt.getModifiers() == MouseEvent.BUTTON3_MASK) {
            puMenu.setVisible(true);
            //System.out.println("vị trí"+pos);
            //if((JMenuItem)evt.getSource()==miOpen)System.out.println("bạn chọn open");
        }
        */
    }//GEN-LAST:event_listProcessFileMouseClicked

    private void jLabel5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseExited
        // TODO add your handling code here:
        jLabel5.setForeground(Color.BLACK);
    }//GEN-LAST:event_jLabel5MouseExited

    private void jLabel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseEntered
        // TODO add your handling code here:
        jLabel5.setForeground(Color.BLUE);
    }//GEN-LAST:event_jLabel5MouseEntered

    

    private void tfSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfSearchKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
        {
            if(tfSearch.getText().equals("")) JOptionPane.showConfirmDialog(null, "Chưa nhập tên tìm kiếm");
        }
    }//GEN-LAST:event_tfSearchKeyPressed

    private void tfSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfSearchActionPerformed

        // TODO add your handling code here:
    }//GEN-LAST:event_tfSearchActionPerformed

    private void tfSearchMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfSearchMousePressed
        // TODO add your handling code here:
        tfSearch.setText("");
    }//GEN-LAST:event_tfSearchMousePressed

    private void btnUpFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpFileActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser=new JFileChooser(pathChooser);

        chooser.showOpenDialog(null);
        File file=chooser.getSelectedFile();
        if(file==null)return;
        pathChooser=file.getPath();
        String fileName=file.getName();
        try {
            if(fileName!=null)
            JOptionPane.showMessageDialog(null, "File Uploaded", "Message", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to upload", "Message", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UploadingFile upLoadingFile=new UploadingFile(file,1);

        m.AddFile(upLoadingFile);
        Object[] rowData={upLoadingFile.GetName(),RoundFileSize(upLoadingFile.GetSize())};
        tableModelFileList.addRow(rowData);
        tableFileList.setModel(tableModelFileList);

        // write file info into file.torrent
        try {
            upLoadingFile.WriteFileInfoToTorrent();
        } catch (IOException ex) {
            Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnUpFileActionPerformed

     public void actionPerform(ActionEvent e) {
        if ((JMenuItem) e.getSource() == miOpen) {
            System.out.println("bạn chọn open");
        }
    }
                          
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
    private javax.swing.JButton btnDeleteFile;
    private javax.swing.JButton btnUpFile;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lYourHostName;
    private javax.swing.JLabel lYourIP;
    private javax.swing.JPopupMenu puMenu1;
    private javax.swing.JTable tableDownloadProcess;
    private javax.swing.JTable tableFileList;
    private javax.swing.JTable tableInforPeerConnect;
    private javax.swing.JTextField tfSearch;
    // End of variables declaration//GEN-END:variables
    private void deleteFile()
    {
        String fileName; // tên file sẽ xóa
        int row=-1;
        row=tableFileList.getSelectedRow();
        if(row==-1)return;
        fileName=tableModelFileList.getValueAt(row, 0).toString();
        tableModelFileList.removeRow(row);
        tableFileList.setModel(tableModelFileList);
        File file = new File("BitTorrent//" + fileName);
        File fileTorrrent = new File("BitTorrent//" + fileName + ".torrent");
        // kiem tra nếu file tồn tại thì xóa
        if (file.exists()) {
            file.delete();
            fileTorrrent.delete();
            m.RemoveFileAt(row);
            JOptionPane.showMessageDialog(null, "File Deleted", "Message", JOptionPane.YES_OPTION);
        } else {
            JOptionPane.showMessageDialog(null, "File Not Exist","Error",JOptionPane.ERROR_MESSAGE);

        }
    }
    private void openFileLocation()
    {
        File file = new File("BitTorrent");
        try {
            java.awt.Desktop.getDesktop().open(file);
        } catch (IOException IOe) {
            IOe.printStackTrace();
        }
    }
    private void openFile()
    {
        String fileName;
        int pos = -1;
        pos = tableFileList.getSelectedRow();
        if (pos == -1) {
            return;
        }
        fileName = tableModelFileList.getValueAt(pos, 0).toString();

        File file = new File("BitTorrent//" + fileName);
        try {
            java.awt.Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if((JMenuItem)e.getSource()==miDelete)
            deleteFile();
        if((JMenuItem)e.getSource()==miOpen)
            openFile();
        if((JMenuItem)e.getSource()==miOpenLocation)
            openFileLocation();
    }
    
    public String RoundFileSize(long size)
    {
       // Double d;
        if(size<1024) return Long.toString(size)+" Byte";
        if(size<1024*1024) return String.format("%.2f", (double)size/1024)+" KB";
        if(size<1024*1024*1024) return String.format("%.2f", (double)size/(1024.0*1024))+" MB";
        return "";
    }
}
