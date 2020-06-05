package Lógica;

public class Md5 {

    private int a;
    private int b;
    private int c;
    private int d;
    private int X[];
    private int T[];
    
    public Md5(){
        a = 0x01234567;
        b = 0x89ABCDEF;
        c = 0xFEDCBA98;
        d = 0x76543210;
        X = new int[16];
        T = new int[64];
    }
    
    private int F(int x, int y, int z){
            
        return (x & y) | ((~x) & z);
    }
    
    private int G(int x, int y, int z){
     
        return ((x & z) | (y & (~x)));
    }
    
    private int H(int x, int y, int z){
        
        return (x ^ y ^ (z));
    }
    
    private int I(int x, int y, int z){
            
        return  (y ^ (x | (~z)));
    } 
    
    private String relleno(String entrada){
    
        String nuevoMensaje = "", longitud;
        int modulo;
        
        //Obtiene el binario del mensaje mezclado.
        for(int i = 0; i < entrada.length(); i++)
            nuevoMensaje += Integer.toBinaryString(entrada.charAt(i));
        
        modulo = (entrada.length() % 448);
        //Si no es congruente con 448, entonces se rellena.
        if(modulo != 0){
            nuevoMensaje += "1";
            for(int i = 0; i < modulo - 1; i++)
                nuevoMensaje += "0";
        }
        //Se agrega la longitud al mensaje a 64 bits.
        longitud = Integer.toBinaryString(nuevoMensaje.length());
        while(longitud.length() < 64)
            longitud = "0" + longitud;
        nuevoMensaje += longitud;
        
        return nuevoMensaje;
    }
    
    private void tabla(){
               
        for(int i = 0; i < 64; i++)
            T[i] = (int)(4294967296L * (Math.abs(Math.sin(i))));
    }

    private int b1(int a, int b, int c, int d, int k, int s, int i){
        
        return (b + ((a + F(b, c, d) + X[k] + T[i]) << s));
    }
    
    private int b2(int a, int b, int c, int d, int k, int s, int i){
        
        return (b + ((a + G(b, c, d) + X[k] + T[i]) << s));
    }
    
    private int b3(int a, int b, int c, int d, int k, int s, int i){
        
        return (b + ((a + H(b, c, d) + X[k] + T[i]) << s));
    }
    
    private int b4(int a, int b, int c, int d, int k, int s, int i){
        
        return (b + ((a + I(b, c, d) + X[k] + T[i]) << s));
    }
    
    private String rellenoFinal(String resultado){
        
        String res = "";
        
        while(resultado.length() < 32)
            resultado = "0" + resultado;
        
        for (int i = resultado.length() - 1; i >= 0; i--)
        	res += resultado.charAt(i);
                        
        return res;
    }
    
    public String algoritmoMd5(String message){
        
        int AA, BB, CC, DD;
        int lon;
        String resultado, newMessage;
        
        newMessage = relleno(message);
        lon = newMessage.length();
        tabla();
        AA = a;
        BB = b;
        CC = c;
        DD = d;
        
        for(int i = 0; i < lon/16; i++){
            for(int j = 0; j < 16; j++)
                X[j] = (int)newMessage.charAt((i * 16) + j);
             
                           
            a = b1(a, b, c, d, 0, 7, 0);
            d = b1(d, a, b, c, 1, 12, 1);
            c = b1(c, d, a, b, 2, 17, 2);
            b = b1(b, c, d, a, 3, 22, 3);
            a = b1(a, b, c, d, 4, 7, 4);
            d = b1(d, a, b, c, 5, 12, 5);
            c = b1(c, d, a, b, 6, 17, 6);
            b = b1(b, c, d, a, 7, 22, 7);
            a = b1(a, b, c, d, 8, 7, 8);
            d = b1(d, a, b, c, 9, 12, 19);
            c = b1(c, d, a, b, 10, 17, 10);
            b = b1(b, c, d, a, 11, 22, 11);
            a = b1(a, b, c, d, 12, 7, 12);
            d = b1(d, a, b, c, 13, 12, 13);
            c = b1(c, d, a, b, 14, 17, 14);
            b = b1(b, c, d, a, 15, 22, 15);                      
                    
            a = b2(a, b, c, d, 1, 5, 16);
            d = b2(d, a, b, c, 6, 9, 17);
            c = b2(c, d, a, b, 11, 14, 18);
            b = b2(b, c, d, a, 0, 20, 19);
            a = b2(a, b, c, d, 5, 5, 20);
            d = b2(d, a, b, c, 10, 9, 21);
            c = b2(c, d, a, b, 15, 14, 22);
            b = b2(b, c, d, a, 4, 20, 23);
            a = b2(a, b, c, d, 9, 5, 24);
            d = b2(d, a, b, c, 14, 9, 25);
            c = b2(c, d, a, b, 3, 14, 26);
            b = b2(b, c, d, a, 8, 20, 27);
            a = b2(a, b, c, d, 13, 5, 28);
            d = b2(d, a, b, c, 2, 9, 29);
            c = b2(c, d, a, b, 7, 14, 30);
            b = b2(b, c, d, a, 12, 20, 31);

            a = b3(a, b, c, d, 5, 4, 32);
            d = b3(d, a, b, c, 8, 11, 33);
            c = b3(c, d, a, b, 11, 16, 34);
            b = b3(b, c, d, a, 14, 23, 35);
            a = b3(a, b, c, d, 1, 4, 36);
            d = b3(d, a, b, c, 4, 11, 37);
            c = b3(c, d, a, b, 7, 16, 38);
            b = b3(b, c, d, a, 10, 23, 39);
            a = b3(a, b, c, d, 13, 4, 40);
            d = b3(d, a, b, c, 0, 11, 41);
            c = b3(c, d, a, b, 3, 16, 42);
            b = b3(b, c, d, a, 6, 23, 43);
            a = b3(a, b, c, d, 9, 4, 44);
            d = b3(d, a, b, c, 12, 11, 45);
            c = b3(c, d, a, b, 15, 16, 46);
            b = b3(b, c, d, a, 2, 23, 47);

            a = b4(a, b, c, d, 0, 6, 48);
            d = b4(d, a, b, c, 7, 10, 49);
            c = b4(c, d, a, b, 14, 15, 50);
            b = b4(b, c, d, a, 5, 21, 51);
            a = b4(a, b, c, d, 12, 6, 52);
            d = b4(d, a, b, c, 3, 10, 53);
            c = b4(c, d, a, b, 10, 15, 54);
            b = b4(b, c, d, a, 1, 21, 55);
            a = b4(a, b, c, d, 8, 6, 56);
            d = b4(d, a, b, c, 15, 10, 57);
            c = b4(c, d, a, b, 6, 15, 58);
            b = b4(b, c, d, a, 13, 21, 59);
            a = b4(a, b, c, d, 4, 6, 60);
            d = b4(d, a, b, c, 11, 10, 61);
            c = b4(c, d, a, b, 2, 15, 62);
            b = b4(b, c, d, a, 9, 21, 63);

            if((i % 32) == 0){
                a += AA;
                b += BB;
                c += CC;
                d += DD;
            }
        }
        a += AA;
        b += BB;
        c += CC;
        d += DD;
        
        resultado = rellenoFinal(Integer.toBinaryString(a));
        resultado += rellenoFinal(Integer.toBinaryString(b));
        resultado += rellenoFinal(Integer.toBinaryString(c));
        resultado += rellenoFinal(Integer.toBinaryString(d));
        
        return resultado;
    }
        
}
