package ve.usb.grafoLib


public class DosColoreable(val g: GrafoNoDirigido) {
/*
 clase que muestra si un grafo no dirigido es bipartito.
 */

    // Variable para saber si el grafo es bipartito y lista
    // de clase interna vertice para llevar las propiedades de los vertices
    var esBipartito: Boolean = true
    var propiedadesDeVertice: List<Vertice> = List(0){Vertice()}   

    init {
        /*
        Se ejecuta DFS modificado al instanciar la clase para asignar "colores" a los vertices
        y comparar si un vertice no explorado tienen un vecino de igual color
    
        Precondicion: True
        Postcoindicion: Se consigue si un grafo bipartito el grafo
        Tiempo de Ejecucion: O(|V| + |E|) 
            siendo V la cantidad de vertices y E la cantidad de lados
            en el grafo
            */

        // Crea la lista de vertices, estos ya estan inicializados para ejecutar el
        // algoritmo
        propiedadesDeVertice = List(g.obtenerNumeroDeVertices()){Vertice()}

        // Se marca el primer nodo como visitado y se le asigna un color
        propiedadesDeVertice[0].porExplorar = false
        propiedadesDeVertice[0].color = 0

        // Se inicia la recursion
        esBipartito = bipartiteVisit(g, 0)        
    }   
    
    private fun bipartiteVisit(g: Grafo, v: Int): Boolean {    
        /*Metodo dfsVisit del algoritmo DFS modificado para retornar un valor
        booleano que indica si el grafo g es bipartito.*/   
        
        // Iterar por los adyacentes de v
        for (lado in g.adyacentes(v)){
            var u = lado.elOtroVertice(v)

            // Si no se ha explorado el vertice
            if (propiedadesDeVertice[u].porExplorar){
                
                // Se marca como explorado
                propiedadesDeVertice[u].porExplorar = false

                // Se pinta de color opuesto al de su padre
                propiedadesDeVertice[u].color = 1 - propiedadesDeVertice[v].color

                // Explorar el vertice u, si en su exploracion se consigue 
                // un vertice cuyo padre sea del mismo color que el, entonces el
                // grafo no es bipartito y se retorna false
                if (!bipartiteVisit(g, u)){
                    return false
                }
            
            // Si ya se exploro el vertice u, se compara su color con el de su padre
            } else if (propiedadesDeVertice[v].color == propiedadesDeVertice[u].color) {
                // Si tienen el mismo color, el grafo no es bipartito 
                return false
            }    
        }

        // Si se itera y explora por todos los adyacentes al primer vertice que entra en la
        // recursion y no se consigue que un hijo tenga el mismo color de su padre, entonces
        // el grafo es bipartito
        return true
    }
    

    
    fun esDosColoreable() : Boolean {
        /*Retorna true si el grafo de entrada es un grafo bipartito, false en caso contrario.
        precond: True
        postcond: Muestra si el grafo es bipartito o no
        Tiempo de ejecucion: O(1)
        */

        return esBipartito
    }


   /*
    Clase interna que lleva las propiedades de un vertice perteneciente al grafo.
    El vertice que sea representado por entero 6 tiene sus propiedades en la lista
    propiedadesDeVertice[6].

    color: Propiedad para asignar un color al vertice para compararlo con su padre. Durante
        la ejecucion del algoritmo solo puede tener como color 0 o 1

    porExplorar: Hace la misma funcion que los colores blanco y negro al momento de ejecutar DFS.
        En este caso se cambian por un valor booleano 
    */
    inner class Vertice(
        var color: Int = -1,
        var porExplorar: Boolean = true){}
}
