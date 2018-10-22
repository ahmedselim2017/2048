package GKA;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;


public class OyunTahtasi {
    public static final int SATIR=4;//row
    public static final int SUTUN=4;//col
    public static int BOSLUK=10;
    public static int TAHTA_GENISLIK=(SUTUN+1)*BOSLUK+SUTUN*Fayans.GENISLIK;
    public static int TAHTA_YUKSEKLIK=(SATIR+1)*BOSLUK+SATIR*Fayans.YUKSEKLIK;

    private final int BaslangicFayansi=2;
    private Fayans[][] tahta;
    private boolean oldunMu;
    private boolean kazandinMi;
    private BufferedImage tahtaResim;
    private BufferedImage sonTahtaResim;
    private int x;
    private int y;
    private boolean oyunBasladiMi;




    public OyunTahtasi(int x,int y){
        this.x=x;
        this.y=y;
        tahta=new Fayans[SATIR][SUTUN];
        tahtaResim=new BufferedImage(TAHTA_GENISLIK,TAHTA_YUKSEKLIK,BufferedImage.TYPE_INT_RGB);
        sonTahtaResim=new BufferedImage(TAHTA_GENISLIK,TAHTA_YUKSEKLIK,BufferedImage.TYPE_INT_RGB);

        tahtaResimOlustur();
        baslat();
    }

    private void tahtaResimOlustur(){
        Graphics2D g=(Graphics2D)tahtaResim.getGraphics();

        g.setColor(new Color(189, 173, 158));
        g.fillRect(0,0,TAHTA_GENISLIK,TAHTA_YUKSEKLIK);

        g.setColor(new Color(206, 193, 179));



        for(int satir=0;satir<SATIR;satir++){
            for(int sutun=0;sutun<SUTUN;sutun++){
                int x=BOSLUK+BOSLUK*sutun+Fayans.GENISLIK*sutun;
                int y=BOSLUK+BOSLUK*satir+Fayans.YUKSEKLIK*satir;
                g.fillRoundRect(x,y,Fayans.GENISLIK,Fayans.YUKSEKLIK,Fayans.KAVIS_GENISLIK,Fayans.KAVIS_YUKSEKLIK);
            }
        }
        g.dispose();
    }


    public void guncelle(){
        klavyeyiKontrolEt();


        Fayans simdiki;
        for(int satir=0; satir<SATIR;satir++){
            for(int sutun=0;sutun<SUTUN;sutun++){
                simdiki=tahta[satir][sutun];
                if(simdiki==null)
                    continue;
                simdiki.guncelle();
                posisyonuSifirla(simdiki,satir,sutun);
                if(simdiki.degerAl()==2048){
                    kazandinMi=true;
                }
            }
        }
    }

    private void posisyonuSifirla(Fayans simdiki,int satir,int sutun){
        if(simdiki==null)
            return;

        int x=fayansXAl(sutun);
        int y=fayansYAl(satir);

        int uzkX=simdiki.getX()-x;
        int uzkY=simdiki.getY()-y;


        if(Math.abs(uzkX)<Fayans.KAYDIRMA_HIZI)
             simdiki.setX(simdiki.getX()-uzkX) ;

        if(Math.abs(uzkY)<Fayans.KAYDIRMA_HIZI)
            simdiki.setY(simdiki.getY()-uzkY);


        if(uzkX<0)
            simdiki.setX(simdiki.getX()+Fayans.KAYDIRMA_HIZI);

        if(uzkY<0)
            simdiki.setY(simdiki.getY()+Fayans.KAYDIRMA_HIZI);


        if(uzkX>0)
            simdiki.setX(simdiki.getX()-Fayans.KAYDIRMA_HIZI);
        if(uzkY>0)
            simdiki.setY(simdiki.getY()-Fayans.KAYDIRMA_HIZI);

    }

    public void render(Graphics2D g){
        Graphics2D g2d=(Graphics2D)sonTahtaResim.getGraphics();
        g2d.drawImage(tahtaResim,0,0,null);


        Fayans simdiki;
        for(int satir=0;satir<SATIR;satir++){
            for(int sutun=0;sutun<SUTUN;sutun++){
                simdiki=tahta[satir][sutun];
                if(simdiki==null)
                    continue;
                simdiki.render(g2d);
            }
        }

        g.drawImage(sonTahtaResim,x,y,null);
        g2d.dispose();
    }

    private void baslat(){
        for(int i=0; i< BaslangicFayansi;i++){
            rastgeleSpawnla();
        }
    }


    private void rastgeleSpawnla(){
        Random rastgele=new Random();
        boolean gecerliDegil=true;

        int lokasyon;
        int satir;
        int sutun;

        Fayans suanki;
        int deger;

        Fayans fayans;

        while (gecerliDegil){
            lokasyon=rastgele.nextInt(SATIR*SUTUN);
            satir=lokasyon/SATIR;
            sutun=lokasyon%SUTUN;
            suanki=tahta[satir][sutun];

            if(suanki==null){
                //eğer rastegele sayı 9 dan küçük ise 2 değil ise 4
                deger=rastgele.nextInt(10) < 9 ? 2 :4;
                fayans=new Fayans(deger,fayansXAl(sutun),fayansYAl(satir));
                tahta[satir][sutun]=fayans;
                gecerliDegil=false;
            }

        }
    }

    public int fayansXAl(int sutun){
        return BOSLUK + sutun * Fayans.GENISLIK + sutun * BOSLUK;
    }

    public int fayansYAl(int satir){
        return BOSLUK +satir * Fayans.YUKSEKLIK + satir * BOSLUK;
    }


    private void fayansiOynat(Yon yon){
        boolean oynayabilirMi=false;
        int yatayYon=0;
        int dikeyYon=0;

        switch (yon){
            case SAG:
                yatayYon=1;

                for(int satir=0;satir<SATIR;satir++){
                    for (int sutun=SUTUN-1;sutun>=0;sutun--){
                        if(!oynayabilirMi){
                            oynayabilirMi=oyna(satir,sutun,yatayYon,dikeyYon,yon);
                        }
                        else{
                            oyna(satir,sutun,yatayYon,dikeyYon,yon);
                        }
                    }
                }

                break;
            case SOL:
                yatayYon=-1;

                for(int satir=0;satir<SATIR;satir++){
                    for (int sutun=0;sutun<SUTUN;sutun++){
                        if(!oynayabilirMi){
                            oynayabilirMi=oyna(satir,sutun,yatayYon,dikeyYon,yon);
                        }
                        else{
                            oyna(satir,sutun,yatayYon,dikeyYon,yon);
                        }
                    }
                }

                break;
            case YUKARI:
                dikeyYon=-1;

                for(int satir=0;satir<SATIR;satir++){
                    for (int sutun=0;sutun<SUTUN;sutun++){
                        if(!oynayabilirMi){
                            oynayabilirMi=oyna(satir,sutun,yatayYon,dikeyYon,yon);
                        }
                        else{
                            oyna(satir,sutun,yatayYon,dikeyYon,yon);
                        }
                    }
                }

                break;
            case ASAGI:
                dikeyYon=1;

                for(int satir=SATIR-1;satir>=0;satir--){
                    for (int sutun=0;sutun<SUTUN;sutun++){
                        if(!oynayabilirMi){
                            oynayabilirMi=oyna(satir,sutun,yatayYon,dikeyYon,yon);
                        }
                        else{
                            oyna(satir,sutun,yatayYon,dikeyYon,yon);
                        }
                    }
                }

                break;
            default:
                System.out.println(yon+" Doğru Bir Yön Değil");
                break;
        }


        Fayans simdiki;

        for(int satir=0; satir<SATIR;satir++){
            for(int sutun=0;sutun<SUTUN;sutun++){
                simdiki=tahta[satir][sutun];
                if (simdiki==null)
                    continue;
                simdiki.setBirlesebilirMi(true);
            }
        }

        if(oynayabilirMi){
            rastgeleSpawnla();

            olumuKontrolEt();
        }

    }

    private void olumuKontrolEt(){

        for(int satir=0;satir<SATIR;satir++){
            for (int sutun=0;sutun<SUTUN;sutun++){

                if(tahta[satir][sutun]==null)
                    return;

                if(kapsayanFayanslariKontrolEt(satir,sutun,tahta[satir][sutun]))
                    return;
            }
        }

        oldunMu=true;
    }


    private boolean kapsayanFayanslariKontrolEt(int satir,int sutun,Fayans simdiki){
        Fayans kontrol;
        if(satir>0){
            kontrol=tahta[satir-1][sutun];

            if (kontrol==null || simdiki.degerAl()==kontrol.degerAl())
                return true;

        }
        if(satir < SATIR -1){
            kontrol=tahta[satir+1][sutun];

            if (kontrol==null || simdiki.degerAl()==kontrol.degerAl())
                return true;
        }
        if(sutun>0){
            kontrol=tahta[satir][sutun-1];

            if (kontrol==null || simdiki.degerAl()==kontrol.degerAl())
                return true;
        }
        if(sutun < SUTUN -1){
            kontrol=tahta[satir][sutun+1];

            if (kontrol==null || simdiki.degerAl()==kontrol.degerAl())
                return true;
        }
        return false;
    }


    private boolean oyna(int satir,int sutun,int yatayYon,int dikeyYon,Yon yon){
        boolean oynayabilirMi=false;

        Fayans simdiki=tahta[satir][sutun];
        if(simdiki==null)
            return false;

        boolean oyna=true;
        int yeniSatir=satir;
        int yeniSutun=sutun;

        while (oyna){
            yeniSutun+=yatayYon;
            yeniSatir+=dikeyYon;

            if(sinirlardanCiktiMi(yon,yeniSatir,yeniSutun))
                break;

            if(tahta[yeniSatir][yeniSutun]==null){
                tahta[yeniSatir][yeniSutun]=simdiki;
                tahta[yeniSatir-dikeyYon][yeniSutun-yatayYon]=null;
                tahta[yeniSatir][yeniSutun].setKaydir(new Nokta(yeniSatir,yeniSutun));
                oynayabilirMi=true;
            }
            else if(tahta[yeniSatir][yeniSutun].degerAl()==simdiki.degerAl() && tahta[yeniSatir][yeniSutun].birlesebilirMi()){
                tahta[yeniSatir][yeniSutun].setBirlesebilirMi(false);
                tahta[yeniSatir][yeniSutun].setDeger(tahta[yeniSatir][yeniSutun].degerAl()*2);
                oynayabilirMi=true;
                tahta[yeniSatir-dikeyYon][yeniSutun-yatayYon]=null;
                tahta[yeniSatir][yeniSutun].setKaydir(new Nokta(yeniSatir,yeniSutun));
                tahta[yeniSatir][yeniSutun].setBirlestirmeAnimasyonu(true);
            }
            else {
                oyna=false;
            }


        }


        return oynayabilirMi;
    }


    private boolean sinirlardanCiktiMi(Yon yon,int satir,int sutun){
        switch (yon){
            case SAG:
                return sutun>SUTUN-1;
            case SOL:
                return sutun<0;
            case YUKARI:
                return satir<0;
            case ASAGI:
                return satir>SATIR-1;
        }
        return false;
    }

    private void klavyeyiKontrolEt(){
        if(Klavye.yaz(KeyEvent.VK_LEFT)){
            fayansiOynat(Yon.SOL);

            if(!oyunBasladiMi)
                oyunBasladiMi=true;

        }
        if(Klavye.yaz(KeyEvent.VK_RIGHT)){
            fayansiOynat(Yon.SAG);

            if(!oyunBasladiMi)
                oyunBasladiMi=true;
        }
        if(Klavye.yaz(KeyEvent.VK_UP)){
            fayansiOynat(Yon.YUKARI);

            if(!oyunBasladiMi)
                oyunBasladiMi=true;
        }
        if(Klavye.yaz(KeyEvent.VK_DOWN)){
            fayansiOynat(Yon.ASAGI);

            if(!oyunBasladiMi)
                oyunBasladiMi=true;
        }
    }
    


}
