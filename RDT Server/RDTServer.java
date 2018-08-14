package server;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RDTServer
{
  static DatagramSocket a;
  static float b = 0.005F;
  static float c = 0.002F;
  static float d = 0.005F;
  static float e = 0.002F;
  static String f;
  static int g = 64;
  static InetAddress h;
  static int i;
  static int j;
  static int k;
  static byte l;
  static boolean m = false;
  static Random n;
  
  public static void main(String[] paramArrayOfString) throws SocketException, UnknownHostException, UnsupportedEncodingException, IOException, InterruptedException
  {
    n = new Random();
    
    j = Integer.parseInt(paramArrayOfString[0]);
    k = Integer.parseInt(paramArrayOfString[1]);
    
    m = false;
    a = new DatagramSocket(k);
    h = InetAddress.getByName("localhost");
    int i1 = 0;
    if (!m)
    {
      while (!b()) {}
      byte[] arrayOfByte = f.toUpperCase().getBytes("UTF-8");
      int i2 = arrayOfByte.length;
      int i3 = (int)Math.ceil(i2 / g);
      i1 = 0;
      i = 0;
      l = 0;
      while (i1 < i3)
      {
        a(Arrays.copyOfRange(arrayOfByte, i1 * g, (i1 + 1) * g));
        if (c()) {
          i1++;
        } else {
          System.out.println("Retransmitting packet no: " + i1);
        }
      }
      do
      {
        a();
      } while (!c());
    }
  }
  
  static void a() throws IOException
  {
    if ((m) || (n.nextFloat() > c))
    {
      System.out.println("Sent close");
      byte[] arrayOfByte = new byte[2];
      arrayOfByte[0] = 2;
      arrayOfByte[1] = ((byte)i);
      DatagramPacket localDatagramPacket = new DatagramPacket(arrayOfByte, arrayOfByte.length, h, j);
      a.send(localDatagramPacket);
    }
  }
  
  static void a(byte[] paramArrayOfByte) throws IOException
  {
    if ((m) || (n.nextFloat() > c))
    {
      byte[] arrayOfByte = new byte[g + 2];
      arrayOfByte[0] = 0;
      arrayOfByte[1] = ((byte)i);
      for (int i1 = 2; i1 < g + 2; i1++) {
        arrayOfByte[i1] = paramArrayOfByte[(i1 - 2)];
      }
      DatagramPacket localDatagramPacket = new DatagramPacket(arrayOfByte, arrayOfByte.length, h, j);
      a.send(localDatagramPacket);
    }
    else
    {
      System.out.println("Packet2 lost");
    }
  }
  
  static void a(byte paramByte) throws IOException
  {
    if ((m) || (n.nextFloat() > b))
    {
      byte[] arrayOfByte = new byte[2];
      arrayOfByte[0] = 1;
      arrayOfByte[1] = paramByte;
      DatagramPacket localDatagramPacket = new DatagramPacket(arrayOfByte, arrayOfByte.length, h, j);
      a.send(localDatagramPacket);
    }
    else
    {
      System.out.println("Ack1 lost");
    }
  }
  
  static boolean b() throws IOException
  {
      
    if ((m) || (n.nextFloat() > e))
    {
      byte[] arrayOfByte1 = new byte[66];
      DatagramPacket localDatagramPacket = new DatagramPacket(arrayOfByte1, arrayOfByte1.length);
      try
      {
        a.receive(localDatagramPacket);
        byte[] arrayOfByte2 = new byte[localDatagramPacket.getLength()];
        arrayOfByte2 = Arrays.copyOfRange(localDatagramPacket.getData(), 0, localDatagramPacket.getLength());
        int i1 = arrayOfByte2[0];
        int i2 = arrayOfByte2[1];
        if (i1 == 0)
        {
          l = (byte) i2;
          if (i2 == i)
          {
            i = (i + 1) % 2;
            f += new String(Arrays.copyOfRange(arrayOfByte2, 2, arrayOfByte2.length));
            a(l);
            return false;
          }
          a(l);
        }
        else if (i1 == 2)
        {
          l = (byte) i2;
          a(l);
          return true;
        }
        return false;
      }
      catch (SocketTimeoutException localSocketTimeoutException)
      {
        System.out.println("Timeout");
      }
    }
    System.out.println("Packet1 lost");
//      try {
//          Thread.sleep(2000);
//      } catch (InterruptedException ex) {
//          Logger.getLogger(RDTServer.class.getName()).log(Level.SEVERE, null, ex);
//      }
    return false;
  }
  
  static boolean c() throws SocketException, IOException, InterruptedException
  {
    a.setSoTimeout(1000);
    byte[] arrayOfByte1 = new byte[2];
    DatagramPacket localDatagramPacket = new DatagramPacket(arrayOfByte1, arrayOfByte1.length);
    try
    {
      a.receive(localDatagramPacket);
      if ((m) || (n.nextFloat() > d))
      {
        byte[] arrayOfByte2 = localDatagramPacket.getData();
        int i1 = arrayOfByte2[0];
        int i2 = arrayOfByte2[1];
        if ((i1 == 1) && (i2 == i))
        {
          i = (i + 1) % 2;
          return true;
        }
        return false;
      }
      Thread.sleep(1000);
      System.out.println("Ack2 lost");
    }
    catch (SocketTimeoutException localSocketTimeoutException)
    {
      System.out.println("Timeout");
    }
    return false;
  }
}