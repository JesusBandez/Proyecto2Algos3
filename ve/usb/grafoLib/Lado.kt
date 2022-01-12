package ve.usb.grafoLib


abstract class Lado(val a: Int, val b: Int) {

    fun cualquieraDeLosVertices() : Int {
    /* Metodo para retornar cualquiera de los vertices 

    Precondicion: True
    Postcondicion: Se retorna uno de los vertices del lado
    Tiempo de ejecucion: O(1)
    */

    return a
    }

 
    fun elOtroVertice(w: Int) : Int {
       /* Metodo que pide un vertice y retorna el otro vertice de la arista.
        Si el identificador pasado como parametro no corresponde con ningun vertice incidente en la arista, se 
        arroja una RuntimeException
        
        Precondicion: w > -1
        Postcondicion: Retorna u o v, o arroja una excepcion
        Tiempo de ejecucion: O(1)
        */
        when (w){            
            a -> return b
            b -> return a
            else -> throw RuntimeException("${w} no es incidido por esta arista")
        }

    }
}
