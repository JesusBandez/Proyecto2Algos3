package ve.usb.grafoLib

public class Arista(val v: Int,
		    val u: Int,
		    val peso: Double =0.0) : Comparable<Arista>, Lado(v, u) {
    /*
    Representacion de un arista para un grafo no dirigido.file

    Son necesarios los siguientes parametros:
    u: Identificador de un vertice incidente a la arista
    v: Identificador del otro vertice incidente a la arista, no puede er igual a u
    peso: Valor asociado a la arista, por defecto = 0.0
    */


    fun peso() : Double {
        /*Devuelve el peso asociado a la arista 
        Precondicion: true
        Postcondicion: retorna el peso de la arista
        Tiempo de ejecucion: O(1)
        */
        return peso

    }

    override fun toString() : String {
     /*  Retorna una string con la representacion de la arista usando el siguiente formato:

     [u-v : pesoAsociado]

     ejemplo con una arista con un vertice 0, el otro 2 y peso 200.1

     [0-2 : 200.1]
     
     Precondicion: true
     Postcondicion: retorna una string con la representacion de la arista
     Tiempo de ejecucion: O(1)

     */
     return "[${v}-${u} : ${peso}]"


    }

    
     override fun compareTo(other: Arista): Int {
     /* 
     Se compara dos arista con respecto a su peso. 
     Si this.obtenerPeso > other.obtenerPeso entonces
     retorna 1. Si this.obtenerPeso < other.obtenerPeso 
     entonces retorna -1. Si this.obtenerPeso == other.obtenerPeso
     entonces retorna 0
     Precondicion: other es otra arista
     Postcondicion: Si this.obtenerPeso > other.obtenerPeso entonces
          retorna 1. Si this.obtenerPeso < other.obtenerPeso 
          entonces retorna -1. Si this.obtenerPeso == other.obtenerPeso
          entonces retorna 0
     Tiempo De ejecucion: O(1)
     */
     if(this.peso() > other.peso()){
          return 1
          } else if (this.peso() < other.peso()){
          return -1
          } else {
          return 0
          }

     }
} 
