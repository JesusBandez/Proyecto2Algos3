package ve.usb.grafoLib

public class Arco(val inicio: Int, val fin: Int, val peso: Double =0.0) : Lado(inicio, fin) {
    /*  Representacion de un Arco usada para un digrafo

    Se necesitan los siguientes parametros:
    
    inicio: Identificador del vertice inicial
    fin: Identificador del vertice final
    peso: Valor que cuesta "pasar" por este arco, 0.0 por defecto
    
    Todos los metodos de esta clase son O(1) debido a que sólo retornan un valor 
    */
    
    fun fuente() : Int {
    /* Devuelve el vertice inicial del arco
    Precondicion: True
    Postcondicion: retorna el vertice de inicio del arco
    Tiempo de ejecucion: O(1)
     */
    return inicio

    }

    fun sumidero() : Int {
    /* Devuelve el vertice inicial del arco
    Precondicion: True
    Postcondicion: retorna el vertice final del arco
    Tiempo de ejecucion: O(1)
     */
    return fin
    }

    fun peso() : Double {
   /* Devuelve el vertice inicial del arco
    Precondicion: True
    Postcondicion: retorna el peso del arco
    Tiempo de ejecucion: O(1)
     */
    return peso
	
    }

    override fun toString() : String {
/*  Retorna una string con la representacion del arco usando el siguiente formato:

    [verticeInicial-verticeFial : pesoAsociado]

    ejemplo con un arco con vertice inicial 0, vertice final 2 y peso 200.1

    [0-2 : 200.1]

    Precondicion: True
    Postcondicion: True
    Tiempo de ejecucion: O(1)
    */
    return "[${inicio}-${fin} : ${peso}]"

     }
} 
