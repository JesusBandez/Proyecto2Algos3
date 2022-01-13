package ve.usb.grafoLib

// Clase arco que hereda las propiedades de la clase abstracta lado
public class Arco(val inicio: Int, 
                val fin: Int, 
                var peso: Double =0.0) : Lado(inicio, fin) {

    fun fuente() : Int {
        
        // Precondicion
        // !!(this.inicio != null)

        // Tiempo de ejecucion : O(1) se retorma una variable solamente
	    return this.inicio
    }


    // Retorna el vértice final del arco
    fun sumidero() : Int {

        // Precondicion
        // !!(this.fin != null)

        // Tiempo de ejecucion : O(1) se retorma una variable solamente
	    return this.fin
    }

    fun obtenerPeso() : Double {

        // Precondicion
        // !!(this.peso != null)

        // Tiempo de ejecucion : O(1) se retorma una variable solamente
	    return this.peso
    }

    // Representación del arco
    override fun toString() : String {

        // Tiempo de ejecucion : O(1) se retorma una variable solamente
	    return " ${this.inicio} ---> ${this.fin}"
    }
} 
