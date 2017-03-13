package com.fsociety.linkutmbetty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 13/03/2017.
 */

public class dijkstra {
    private int[][] matrizAdyacencia;
    List conj_S = new ArrayList();
    List conjComp_S = new ArrayList();
    List caminos = new ArrayList();
    int nNodos;
    String tmp;
    String tmp2;
    int _origen;
    int _destino;
    public String caminosFinal;
    dijkstra(int[][] MatrizAdyacencia, int origen, int destino){
        matrizAdyacencia = MatrizAdyacencia;
        nNodos = matrizAdyacencia.length;
        matrizAdyacencia[origen][origen] = 0;
        resolverDrijkstra(origen,destino);
    }
    public void resolverDrijkstra(int origen, int destino){
        int nod;
        int minimo;
        int aux;
        int nodCambio=0;
        int intento;
        String tmp2;
        //Inicializando listas
        for(int i=0;i<nNodos;i++){
            if(i!=origen)
                conjComp_S.add(""+i);
            else
                conj_S.add(""+i);
            caminos.add("");
        }
        //Aplicando ciclo i de diksjtra
        for(int i=0;i<nNodos;i++){
            minimo=-1;
            for(int j=0;j<conjComp_S.size();j++){
                nod=Integer.valueOf((String)(conjComp_S.get(j))).intValue();
                aux=min(nod);
                if(minimo==-1 || (aux<minimo && aux!=-1)){
                    minimo=aux;
                    nodCambio=j;
                }
            }
            if(minimo!=-1){
                conj_S.add(""+(String)(conjComp_S.get(nodCambio)));
                conjComp_S.remove(nodCambio);
            }
        }
        //Imprimiendo resultados
        System.out.print("\n -> Resultados <-");
        for(int k=0;k<caminos.size();k++)
            if(k!=origen){
                tmp=(String)(caminos.get(k))+(char)(k+65);
                caminos.set(k,tmp);
            }
        for(int j=0;j<caminos.size();j++)
            if(j!=origen){
                intento=0;
                tmp=(String)(caminos.get(j));
                while(tmp.charAt(0)!=(char)(origen+65) ){//&& intento<10
                    aux=tmp.charAt(0)-65;
                    tmp=((String)(caminos.get(aux)))+tmp.substring(1,tmp.length());
                    //if(++intento==10)
                    //    tmp="*"+tmp;
                };
                if(tmp.charAt(tmp.length()-1)==(char)destino+65){
                    caminosFinal=tmp;
                }
                imprimeCamino(tmp,j,origen);
            }
        System.out.println("\n <-  Que tenga un buen viaje! ->\n");
    }
    private int min(int dest){
        int min=-1;
        int nod=0;
        int nodOrig=-1;
        int aux;
        for(int i=0;i<conj_S.size();i++){
            nod=Integer.valueOf((String)(conj_S.get(i))).intValue();
            if(matrizAdyacencia[nod][nod]!=-1 && matrizAdyacencia[nod][dest]!=-1)
                aux=matrizAdyacencia[nod][nod]+matrizAdyacencia[nod][dest];
            else
                aux=-1;
            if((aux<min && aux!=-1)||min==-1){
                min=aux;
                nodOrig=nod;
            }
        }
        if(min!=-1){
            matrizAdyacencia[dest][dest]=min;
            caminos.set(dest,""+(char)(nodOrig+65));
        }
        return min;
    }
    private void imprimeCamino(String cam, int nod, int o){
        tmp2 += "\nCamino: ";
        if(cam.charAt(0)=='*')
            tmp2 += "Te jodes: no hay camino de: "+(o+1)+" a: "+((int)cam.charAt(cam.length()-1)-64)+"!!";
        else{
            for(int i=0;i<cam.length();i++) {
                tmp2 += "" + ((int) cam.charAt(i) - 64) + (i == cam.length() - 1 ? "" : "->");
            }
            tmp2 += " costo: "+matrizAdyacencia[nod][nod];
        }
    }
}
