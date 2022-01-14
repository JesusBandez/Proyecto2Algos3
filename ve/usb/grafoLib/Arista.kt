package ve.usb.grafoLib

// Clase arista que hereda las propiedades de la clase abstracta lado
public class Arista(val v : Int,
		    val u : Int,
		    val peso : Double = 0.0) : Comparable<Arista>, Lado(v, u) {

    // Retorna el peso del arco
    fun peso() : Double {
        // Precondicion, la arista debe tener peso (asi sea 0.0)
        // !!(this.peso!=null)

        // Tiempo de ejecucion : O(1) se retorma una variable solamente
	    return this.peso
    }

    // Representación en string de la arista
    override fun toString() : String {
        // Tiempo de ejecucion : O(1) se retorma una variable solamente
        return "${this.v} ---- ${this.u}  = ${this.u} ---- ${this.v}"
    }

    /* 
     Se compara dos arista con respecto a su peso. 
     Si this.obtenerPeso > other.obtenerPeso entonces
     retorna 1. Si this.obtenerPeso < other.obtenerPeso 
     entonces retorna -1. Si this.obtenerPeso == other.obtenerPeso
     entonces retorna 0 
     */
     override fun compareTo(other: Arista): Int {

        // Precondicion
        // !!(other!=null)

        if (this.peso<other.peso) {
            return -1
        } else if (this.peso>other.peso){
            return 1
        }else{
            return 0
        }

        // Tiempo de ejecucion : O(1) se retorma una variable solamente
    }
}