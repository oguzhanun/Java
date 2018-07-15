package yilancik;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;



public class Yilancik extends Application {
    
    Timer timer = new Timer();
    List<Integer> yilaninNoktalari;
    Polyline yilan;
    Group group;
    int anaSayac = 0;
    public static List<Yem> yemListesi;
    int x = 5;
    int y = 5;
    public static double ekranGenisligi = 1200;
    public static double ekranYuksekligi = 1100;
    
    
    
    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(ilkYilan(), ekranGenisligi, ekranYuksekligi);
        scene.setFill(Color.ALICEBLUE);
        
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        yilan.getPoints().clear();
                        int i;
                        ArrayList arrayList= new ArrayList();
                        for (i = 0; i<yilaninNoktalari.size(); i++) {
                            if(i==0 ){
                                arrayList.add( yilaninNoktalari.get(i)+ x );     //  Bu bölümde yılanın noktaları başka bir arraylist'e 
                            } else if(i==1){
                                arrayList.add( yilaninNoktalari.get(i)+ y );     //  Bu bölümde yılanın noktaları başka bir arraylist'e 
                            } else {                                             //aktarılıyor ki yılanın başlangıç noktalarının değişimi 
                                arrayList.add( yilaninNoktalari.get(i-2));      //değişim öncesindeki noktaların arkaya doğru iletilmesini önlemesin.
                            }
                        }
                        for (i = 0; i<yilaninNoktalari.size(); i++) {           //  Bu bölümde de önceki bölümde aktarım yapılan noktalar yılanın kendi arraylist'ine
                            yilaninNoktalari.set(i, (Integer) arrayList.get(i));//ve dolayısıyla group noduna aktarılabilsin.
                            String elemanstr = String.valueOf(yilaninNoktalari.
                                    get(i));
                            double elemanDouble = Double.parseDouble(elemanstr);
                            yilan.getPoints().add(elemanDouble);
                        }  
                        group.getChildren().clear();
                        
                        for(int t = 0; t<50; t++){
                            Yem siradakiYem = yemListesi.get(t);
                            if((anaSayac-yemListesi.get(t).yemSayaci) > 100 ||
                                    yemListesi.get(t).yenmedi){  
                                group.getChildren().add(yemListesi.get(t));                       //  sayac2'nin 30 dan büyük olması durumunda yemleri sahneye ekliyoruz.
                            } 
                            if(yilan.intersects(yemListesi.get(t).getBoundsInLocal())){
                                group.getChildren().remove(yemListesi.get(t));
                                yemListesi.get(t).yenmedi = false;
                            }
                        }
                        group.getChildren().add(yilan);                         //Ancak bu işlemi yaptıktan bir süre sonra yemlerin tekrar ekranda görünür olmasını
                        anaSayac++;                                             //sağlamak zorundayız. Ya da yenenlere karşılık başka bir mevki de ki bu mevki 
                                                                                //random olarak atanabilir. Hatta bu daha güzel olur. Yani yenen yemin başka bir noktadas tekrar çıkması sağlanmalı.
                                                                                //Ancak burada yılanın da daha fazla büyümesi de ihmal edilmemeli. Ya yılanlar çok büyürse...
                                                                                //Yemlerin ayrı bir sınıf olarak yazılması düşünülmeli. Çünkü onlar üzerinde de iyileştirmeler yapılmak durumu ve karmaşının önlenmesi düşünülmeli...
                     
//                        System.out.println(" " + anaSayac + yilan.intersects
//                        (yem.getBoundsInLocal()) + yem.rectangleX + "----" + 
//                                yem.rectangleY + "----" + yem.color);
                        }
                    });
                }
            }, 0, 200);
        EventHandler<MouseEvent> eventHandler = 
                new EventHandler<MouseEvent>() {
            @Override                                                           //  Burada yılana scene üzerinden bir event vererek kontrol edilmesini sağladık.    
            public void handle(MouseEvent mouseEvent) {                         //Çalışma mantığı mouse tıklamasının gerçekleştiği scene koordinatının x ve y 
                double mouseX = mouseEvent.getSceneX();                         //değerlerini aldık. Bu iki değer ile yılanın başının bulunduğu koordinatların
                double mouseY = mouseEvent.getSceneY();                         //yani yilaninNoktaları adlı arraylist'teki ilk iki nokta ile mouse'tan gelen
                double farkX = mouseX - yilaninNoktalari.get(0);                //noktalar arasındaki fark alındı ve farkların birbirine bölünmesi ile elde edilen
                double farkY = mouseY - yilaninNoktalari.get(1);                //değerin arctanjantı alınarak tıklama sonrası meydana gelen hata açı değerini tespit
                double aci = Math.atan(farkY/farkX);                            //ettik. Daha sonra ise yılanın başı orjin kabul edilerek tıklamanın gerçekleştiği 
                if(farkX < 0 && farkY < 0){                                     //koordinatın düştüğü bölgeye bağlı olarak açı değerinin işareti değiştirildi.
                    x = (int) -(10*Math.cos(aci));                              //bu sayede yılanın doğru noktaya yol alması sağlandı.
                    y = (int) -(10*Math.sin(aci));                    
                }else if(farkX > 0 && farkY < 0){
                    x = (int) (10*Math.cos(aci));                                   
                    y = (int) (10*Math.sin(aci));
                }else if(farkX > 0 && farkY > 0){
                    x = (int) (10*Math.cos(aci));                                   
                    y = (int) (10*Math.sin(aci));    
                }else if(farkX < 0 && farkY > 0){
                    x = (int) -(10*Math.cos(aci));                                   
                    y = (int) -(10*Math.sin(aci));
                }
            }
        };
        scene.setOnMouseClicked(eventHandler);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        yemListesi = new ArrayList<>();
        Yem a = new Yem(1);
        yemListesi.add(a);
        Yem b = new Yem(2);
        yemListesi.add(b);
        Yem c = new Yem(3);
        yemListesi.add(c);
        Yem d = new Yem(4);
        yemListesi.add(d);
        Yem e = new Yem(5);
        yemListesi.add(e);
        Yem f = new Yem(6);
        yemListesi.add(f);        
        Yem g = new Yem(7);
        yemListesi.add(g);
        Yem h = new Yem(8);
        yemListesi.add(h);
        Yem j = new Yem(9);
        yemListesi.add(j);
        Yem k = new Yem(10);
        yemListesi.add(k);
        Yem l = new Yem(11);
        yemListesi.add(l);
        Yem m = new Yem(12);
        yemListesi.add(m);
        Yem n = new Yem(13);
        yemListesi.add(n);
        Yem o = new Yem(14);
        yemListesi.add(o);
        Yem p = new Yem(15);
        yemListesi.add(p);
        Yem r = new Yem(16);
        yemListesi.add(r);
        Yem s = new Yem(17);
        yemListesi.add(s);
        Yem u = new Yem(18);
        yemListesi.add(u);
        Yem v = new Yem(19);
        yemListesi.add(v);
        Yem y2 = new Yem(20);
        yemListesi.add(y2);
        Yem z = new Yem(21);
        yemListesi.add(z);
        Yem aa = new Yem(22);
        yemListesi.add(aa);
        Yem ab = new Yem(23);
        yemListesi.add(ab);
        Yem ac = new Yem(24);
        yemListesi.add(ac);
        Yem ad = new Yem(25);
        yemListesi.add(ad);
        Yem ae = new Yem(26);
        yemListesi.add(ae);
        Yem af = new Yem(27);
        yemListesi.add(af);
        Yem ag = new Yem(28);
        yemListesi.add(ag);
        Yem ah = new Yem(29);
        yemListesi.add(ah);
        Yem ai = new Yem(30);
        yemListesi.add(ai);
        Yem aj = new Yem(31);
        yemListesi.add(aj);
        Yem ak = new Yem(32);
        yemListesi.add(ak);
        Yem al = new Yem(33);
        yemListesi.add(al);
        Yem am = new Yem(34);
        yemListesi.add(am);
        Yem an = new Yem(35);
        yemListesi.add(an);
        Yem ao = new Yem(36);
        yemListesi.add(ao);
        Yem ap = new Yem(37);
        yemListesi.add(ap);
        Yem ar = new Yem(38);
        yemListesi.add(ar);
        Yem as = new Yem(39);
        yemListesi.add(as);
        Yem at = new Yem(40);
        yemListesi.add(at);
        Yem au = new Yem(41);
        yemListesi.add(au);
        Yem av = new Yem(42);
        yemListesi.add(av);
        Yem ay = new Yem(43);
        yemListesi.add(ay);
        Yem az = new Yem(44);
        yemListesi.add(az);
        Yem aaa = new Yem(45);
        yemListesi.add(aaa);
        Yem aba = new Yem(46);
        yemListesi.add(aba);
        Yem aca = new Yem(47);
        yemListesi.add(aca);
        Yem ada = new Yem(48);
        yemListesi.add(ada);
        Yem ade = new Yem(49);
        yemListesi.add(ade);
        Yem adf = new Yem(50);
        yemListesi.add(adf);
        
        launch(args);
    }
    
    public Group ilkYilan(){
        group = new Group();
        yilaninNoktalari = Arrays.asList(50,50, 55,51, 60,51, 65,51, 70,51, 
                75,51, 80,51, 85,51, 90,51, 95,51, 100,51,103,55, 103,58, 100,
                65, 100,70, 100,75, 100,80, 100,85, 100,90, 100,95, 
                100,100, 105,105);
        yilan = new Polyline();
        for (Integer eleman : yilaninNoktalari) {
            String elemanstr = eleman.toString();
            double elemanDouble = Double.parseDouble(elemanstr);
            yilan.getPoints().add(elemanDouble);
        }
        yilan.setStroke(Color.RED);
        yilan.setStrokeWidth(5);
        group.getChildren().add(yilan);
        return group;
    }
    
}
