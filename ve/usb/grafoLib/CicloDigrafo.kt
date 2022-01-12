package ve.usb.grafoLib


public class CicloDigrafo(val g: GrafoDirigido) {
    /* Clase para detectar un ciclo en un digrafo. Se aplica DFS al instanciar esta
    clase
    g: Un grafo dirigido

    precondicion: g es un grafo dirigido
    */
    
    // Lista mutable con los vertices que forman un ciclo en g.
    // En caso de no haber ciclo, se queda vacia
    var ciclo: MutableList<Int> = mutableListOf()

    // variables para llevar si hay ciclo y, en caso de haberlo, saber 
    // si es un bucle
    var hayCiclo = false
    var arcoEsBucle = false

    init {
        /* Para conseguir un ciclo dentro del grafo g, se ejecuta DFS y se
        procede a buscar un BackEdge o un bucle
        precondicion: True
        postcondicion: Se consigue si el grafo tiene un ciclo o no
        tiempoDeEjecucion: O(|V| + |E| + |E|)
         */

        // Ejecutar DFS sobre el grafo g
        var dfs = BusquedaEnProfundidad(g)
        
        // variableara encontrar el arco BackEdge que forma un ciclo
        // en caso de existir
        var arcoBackEdge: Arco = Arco(-1, -1)
        for (arco in g.arcos()){
            

            // obtener los tiempos de los vertices pertenecientes al arco
            var (fueI, fueF) = dfs.obtenerTiempos(arco.fuente())
            var (sumI, sumF) = dfs.obtenerTiempos(arco.sumidero())
          
            // Si el vertice sumidero es un ancestro del vertice fuente, el arco es un Back Edge
            if ( (arco.fuente() == arco.sumidero()) || (sumI < fueI && fueI < fueF && fueF < sumF)){
                // Como se consigue un BackEdge, entonces hay un ciclo
                hayCiclo = true
                arcoBackEdge = arco

                // Comprobar que el BackEdge encontrado sea un bucle
                if (arco.fuente() == arco.sumidero()){
                    arcoEsBucle = true
                }
                break
            }
        } 

        if (hayCiclo){
            
            // Si el ciclo es solo un bucle, se agrega un solo elemento a la lista
            if(arcoEsBucle){
                ciclo.add(arcoBackEdge.fuente())
            } else {            
            
            // En caso contrario, se itera por los predecesores del vertice fuente del backedge
            // hasta llegar al vertice sumidero del mismo.
            var current: Int = arcoBackEdge.fuente()
            do {
                
                ciclo.add(current)
                current = dfs.obtenerPredecesor(current)!!
            }
            while (current != arcoBackEdge.sumidero())

            ciclo.add(arcoBackEdge.sumidero())
            }
            
        }
    }

    
    fun cicloEncontrado() : Iterable<Int> {
        /* Metodo que retorna la secuencia de vertices que conforman el ciclo del grafo g
        en caso de no existir un ciclo, arroja una exception 
        precondicion: Debe existir un ciclo en el grafo g
        postcondicion: Se retorna la secuencia de vertices que conforman el ciclo
        tiempo de ejecucion: O(1)
        */
        if (this.existeUnCiclo()){
            return ciclo
        } else {
            throw RuntimeException("El grafo no tiene ciclos para retornar")
        }
    }

    
    fun existeUnCiclo() : Boolean {
        /* Metodo para saber si el grafo tiene un ciclo 
        precondicion: True
        postcondicion: Se retorna true si el grafo tiene un ciclo, false
            en caso contrario
        tiempo de ejecucion: O(1)
        */
        return hayCiclo
    }
    

}