package Lógica;

public class Md5 {

    private static String a;
    private static String b;
    private static String c;
    private static String d;
    private long X[];
    private long T[];
    
    public Md5(){
        //01234567
        a = Integer.toBinaryString(19088743);
        a = "0000000" + a;
        //89ABCDEF
        b = Integer.toBinaryString((int)2309737967L);
        //FEDCBA98;
        c = Integer.toBinaryString((int)4275878552L);
        //76543210
        d = Integer.toBinaryString((int)1985229328);
        d = "0" + d;
        X = new long[16];
        T = new long[64];
    }
    
    private String F(String x, String y, String z){
        
        String resultado = "";
        
        for(int i = 0; i < 32; i++)
            resultado += (x.charAt(i) & y.charAt(i)) | ((~x.charAt(i)) & z.charAt(i));
        
        System.out.println("F: " + resultado);
        
        return resultado;
    }
    
    private String G(String x, String y, String z){
        
        String resultado = "";
        
        for(int i = 0; i < 32; i++)
            resultado += ((x.charAt(i) & z.charAt(i)) | (y.charAt(i) & (~x.charAt(i))));
        
        System.out.println("G: " + resultado);
        
        return resultado;
    }
    
    private String H(String x, String y, String z){
        
        String resultado = "";
        
        for(int i = 0; i < 32; i++)
            resultado += (x.charAt(i) ^ y.charAt(i) ^ (z.charAt(i)));
        
        System.out.println("H: " + resultado);
        
        return resultado;
    }
    
    private String I(String x, String y, String z){
        
        String resultado = "";
        
        for(int i = 0; i < 32; i++)
            resultado +=  (y.charAt(i) ^ (x.charAt(i) | (~z.charAt(i))));
        
        System.out.println("I: " + resultado);
        
        return resultado;
    }
    
    private String stringToBinario(String entrada){
        
        String binario = "";
        
        for(int i = 0; i < entrada.length(); i++)
            binario += Integer.toBinaryString(entrada.charAt(i));
        
        System.out.println("Binario: " + binario);
        
        return binario;
    } 
    
    public String relleno(String binario){
    
        String nuevoMensaje = "";
        int longitud;
        
        for(int i = 0; i < 128; i++)
            nuevoMensaje += binario.charAt(i);
        nuevoMensaje += "1";
        for(int i = 129; i < 448; i++)
            nuevoMensaje += "0";
        longitud = binario.length();
        nuevoMensaje += Integer.toBinaryString(longitud);
        System.out.println("Longitud binaria: " + Integer.toBinaryString(longitud));
        System.out.println("Nuevo Mensaje: " + nuevoMensaje + " " + nuevoMensaje.length());
        
        return nuevoMensaje;
    }
    
    private void tabla(){
               
        for(int i = 0; i < 64; i++){
            T[i] = (long)(4294967296L * (Math.abs(Math.sin(i))));
            System.out.println("T[" + i + "] = " + T[i]);
        }
    }
    
    private void algorithmMd5(String message){
        
        String AA, BB, CC, DD;
        int n = 64;
        String resultado;
        
        for(int i = 0; i < n/16; i++){
            for(int j = 0; j < 16; j++)
                X[j] = (long)message.charAt(i * 16 + j);
            AA = a;
            BB = b;
            CC = c;
            DD = d;
            
            for(int k = 0; k < 16; k++){
                
                switch(i){
                    
                    case 1:
                        //a = b + ((a + F(b, c, d) + X[k] + T[i]) << k);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                             
                }
            }
            
        }
    }
        
    /*public static void main(String[] args){
        
    }*/
}
