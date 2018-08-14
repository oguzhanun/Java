package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.SocketTimeoutException;


public class RDTClient {

    static byte[] byteArray;
    
    public static final int DATA = 0;
    public static final int ACK = 1;
    public static final int END = 2;
    private static int P0 = 0;
    private static int P1 = 0;
    public static int sayacTimeOut = 0;
    static String dosyaAdı;
    static int sourcePort;
    static int targetPort;
    
    static FileInputStream fileInputStream = null;
        
    static DatagramSocket datagramSocket = null;
        
    static DatagramPacket datagramPacket1, datagramPacket2 = null;
    private static int sayacHata = 0;
    private static int sayac;
    private static int sayac2;
    private static int sayac1;
    
    private static String cevap = "";
    private static int sayacGonderiTimeOut;
    private static int sayacEnd = 0;
    private static int sayacAck;
        
    
    public static void main(String[] args) {
        
        if (args.length != 3){
        
            System.err.println("Lütfen <source_port_number> <target_port_number> <target_file> bilgileri ile birlikte programı çalıştırın...");
            return;
        }
        else {
            sourcePort = Integer.parseInt(args[0]);
            targetPort = Integer.parseInt(args[1]);
            dosyaAdı = args[2];
        }    
       
        byte[] b = new byte[64];
        
        long baslangic = System.currentTimeMillis();
        
        try {  // Veri transferinin başladığı kısım...
            
            fileInputStream = new FileInputStream(dosyaAdı);
            
            datagramSocket = new DatagramSocket(sourcePort);
            
            byte[] byte66 = new byte[66]; 
            
            while (fileInputStream.read(b) != -1){
            
                byte66[0] = DATA;
                byte66[1]= (byte) P0;
                
                System.arraycopy(b, 0, byte66, 2, 64);
                
                datagramPacket1 = new DatagramPacket(byte66, byte66.length, 
                        InetAddress.getByName("localhost"),targetPort);
            
                veriGonder();
                
                sayac1++;
                
                while(!veriAl()){
                
                    sayac2++;
                    
                    // veriAl komutu while komutu argümanı olarak çalıştırılıyor.
                    // mükerrer bir çalıştırma yapılmamamlı....
                }
              
                P0 = (P0+1) % 2;
                
            }
            
            sayac = sayac1 + sayac2 ;
            
            byte[] byteSon = new byte[2];
            byteSon[0] = 2;
            byteSon[1] = (byte) P0;
            
            datagramPacket1 = new DatagramPacket(byteSon, byteSon.length,InetAddress.getByName("localhost"),targetPort);
            
            veriGonder();  //End sinyali gönderiliyor...
            
            while(!veriAl()){ //End sinyalinin alındığının kontrol edilmesi... 
                sayacEnd++;
            }
            
            while(!veriAl()){} //Buradan sonrası Dinleme Modu...
        } 
        catch (SocketException ex) {
            Logger.getLogger(RDTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(RDTClient.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) {
            Logger.getLogger(RDTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
        
            try {
                fileInputStream.close();
                
                datagramSocket.close();
                
            } catch (IOException ex) {
                Logger.getLogger(RDTClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        long bitis = System.currentTimeMillis();
        
        float sure = (float) (bitis - baslangic)/1000 ;
        
        System.out.println("Transfer süresi " + sure + " saniyedir.");
        
        System.out.println("Transmission complete: Total number of messages: " + sayac1 + "," + "Total number of messages sent: " + sayac);

        FileOutputStream fileOutputStream = null;
        
        try {
        
            File file = new File("anonymit_altered.txt");
            
            fileOutputStream = new FileOutputStream(file);
            
            fileOutputStream.write(cevap.getBytes());
            
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(RDTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(RDTClient.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }

    private static void veriGonder(){
        
        try {
            
            datagramSocket.send(datagramPacket1);
            
        } 
        catch (IOException ex) {
            Logger.getLogger(RDTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static boolean veriAl(){
    
        datagramPacket2 = new DatagramPacket(new byte[66], 66);
       
        try {
            
            datagramSocket.setSoTimeout(1000);
            
            datagramSocket.receive(datagramPacket2);
            
            byte[] br = datagramPacket2.getData();
                
            if (br[0] == ACK){
                
                if(br[0] == P0){
                
                    return true;
                }       
                else {
                
                    veriGonder();
                    
                    sayacHata++;
                    
                    return false;
                }
                
            }
            else if (br[0] == DATA){
                
                if (br[1] == P1){
                    
                    cevap += new String(Arrays.copyOfRange(br, 2, 66));
                    
                    byte[] cvp = new byte[2];
                    cvp[0] = 1;
                    cvp[1] = (byte) P1;
                    
                    datagramPacket1 = new DatagramPacket(cvp, cvp.length, 
                        InetAddress.getByName("localhost"),targetPort);
            
                    veriGonder();
                    
                    P1 = (P1 +1) % 2;
                
                    sayacAck++;
                    
                    return false;
                } 
                else {
                    
                    return false;
                }
                
            } 
            else if (br[0] == END){
            
                return true;
            }
                
        } 
        catch (SocketException ex) {
            Logger.getLogger(RDTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SocketTimeoutException ex) {
            //Logger.getLogger(RDTClient.class.getName()).log(Level.SEVERE, null, ex);
            
            veriGonder();
            
            sayacTimeOut++;
            
            return false;
            
        } catch (UnknownHostException ex) { 
            Logger.getLogger(RDTClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RDTClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    
}
