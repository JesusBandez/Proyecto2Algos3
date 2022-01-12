package ve.usb.grafoLib

/*
 Clase para conseguir las comopoenntes conexas de un grafo con la estructura de datos
 conjuntos disjuntos. Al instanciar la clase se consiguen las componentes
*/
public class ComponentesConexasCD(val g: GrafoNoDirigido) {

    var conjDisjuntos = ConjuntosDisjuntos(g.obtenerNumeroDeVertices())
    var componentesConexas: MutableList<ComponenteConexa> = MutableList(
        g.obtenerNumeroDeVertices(), {ComponenteConexa(it)})
    

    init{
        /* Al instanciar la clase se consiguen las componentes conexas        
        */

    // Se consiguen los vertices de cada arista perteneciente al grafo
    for (arista in g.aristas()){
        var v = arista.cualquieraDeLosVertices()
        var u = arista.elOtroVertice(v)
        var padreV = conjDisjuntos.encontrarConjunto(v)
        var padreU = conjDisjuntos.encontrarConjunto(u)

        // Si el padre de v y u son distintos, entonces deben unirse
        if (padreV != padreU){

            conjDisjuntos.union(v, u)

            // Se consigue cual es el nuevo padre del conjunto que resulta de la union
            var nuevoPadre = conjDisjuntos.encontrarConjunto(v)

            // Si el nuevo padre es el padre de v, entonces se suma la cantidad de vertices pertenecientes de este padre con la 
            // de el antihuo padre de U. Ademas, La componenteConexa[padreU] ya no es una componente conexa final
            if (nuevoPadre == padreV){

                componentesConexas[padreV].numeroDeVerticesPertenecientes += componentesConexas[padreU].numeroDeVerticesPertenecientes
                componentesConexas[padreU].componenteFinal = false
                
            // En caso contrario, se hace lo mismo pero si intercambian de posicion los padres
            } else if (nuevoPadre == padreU){
                componentesConexas[padreU].numeroDeVerticesPertenecientes += componentesConexas[padreV].numeroDeVerticesPertenecientes
                componentesConexas[padreV].componenteFinal = false
            }
        }
    }
    // Finalmente, se eliminan las componentes que no son finales.
    componentesConexas.removeIf{ !it.componenteFinal }
    }

  
    fun estanMismaComponente(v: Int, u: Int) : Boolean {
    /*
     Retorna true si los dos vertices están en la misma componente conexa y
     falso en caso contrario. Si el algunos de los dos vértices de
     entrada, no pertenece al grafo, entonces se lanza un RuntineException
     */
        if ( ! ((0 <= v && v < g.obtenerNumeroDeVertices()) || 
            (0 < u && u < g.obtenerNumeroDeVertices()))){
                throw RuntimeException("Uno de los vertices no pertenece al grafo")
            }

        if (conjDisjuntos.encontrarConjunto(u) == 
                conjDisjuntos.encontrarConjunto(v)){
            
            return true
        } else {
            return false
        }	
    }

    // Retorna el numero de componentes conexas del grafo
    fun numeroDeComponentesConexas() : Int {
        return conjDisjuntos.numConjuntosDisjuntos()
    }

 
    fun obtenerComponente(v: Int) : Int {
    /*
     Retorna el identificador de la componente conexa donde está contenido 
     el vértice v. Si ve no pertenece al grafo, se arroja una exception     
     */
        if ( !(0 <= v && v < g.obtenerNumeroDeVertices())){
                throw RuntimeException("El vertice ${v} no pertenece al grafo")
            }

        var padreDeV: Int = conjDisjuntos.encontrarConjunto(v)

        for ((index, componente) in componentesConexas.withIndex()){
            if(componente.identificadorDelPadre == padreDeV){
                return index
            }
        }
        return -1
    }

    //Retorna el número de vértices que conforman una componente conexa 
    fun numVerticesDeLaComponente(compID: Int) : Int {
        /* 
        Precondicion: compID es el identificador de una componente
        Postcoindicion: se retorna el numero de vertices de la componente
        Tiempo de Ejecucion: O(1) 
        */
        return componentesConexas[compID].numeroDeVerticesPertenecientes
    }


    /* Clase interna para representar la cantidad de componentes conexas en el grafo.
    numeroDeVerticesPertenecientes es la cantidad inicial de vertices en la componente. A medida que la componente
      se va uniendo con otras, se van sumando sus numerosDeVerticesPertenecientes, de esta forma la componente conexa final
      tiene el numero de vertices total en ella

    componenteFinal: Es un flag para determinar si el conjunto al que esta componente hace referencia se le ha cambiado el padre
      Todo nodo al que jamas se le haya cambiado de padre tiene una componente conexa en la que se le guardan su numeroDeVerticesPertenecientes
      Las componentes conexas que no son finales son filtradas al final del algoritmo
     */
    inner class ComponenteConexa(val identificadorDelPadre: Int){        
        var numeroDeVerticesPertenecientes: Int = 1
        var componenteFinal: Boolean = true
    }

}
