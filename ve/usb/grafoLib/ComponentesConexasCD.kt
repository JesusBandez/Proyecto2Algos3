package ve.usb.grafoLib


// Determina las componentes conexas de un grafo no dirigido usando CD
public class ComponentesConexasCD(val g: GrafoNoDirigido) {

    // Se crea un objeto de clase ConjuntosDisjuntos que usaremos para hallar las componentes conexas
    var conjuntosDisjuntos = ConjuntosDisjuntos(g.lados.size)
    
    var aristas : Iterable<Arista> = g.aristas()

    var conj : MutableList<MutableSet<Int>> = mutableListOf()

    // Al crear un objeto de esta clase, se consiguen las componentes conexas
    init{
        // Se consiguen las componentes conexas con la funcion union
        for (arista in aristas){
            conjuntosDisjuntos.union(arista.u,arista.v)
        }

        // Luego se añaden los vertices de cada componente a un arreglo guiandose
        // por las propiedades de los nodos del arbol
        for (nodos in conjuntosDisjuntos.nodos){
            conj.add(mutableSetOf())
        }
        for (nodos in conjuntosDisjuntos.nodos){
            conj[nodos.p].add(nodos.valor)
        }
        conj = conj.filter{s -> !s.isEmpty()}.toMutableList()
        conjuntosDisjuntos.conjuntos = conj
    }
    

    

    // Retorna true si los dos vertices son parte de la misma componente, caso contrario retorna false
    fun estanMismaComponente(v: Int, u: Int) : Boolean {
        if (!g.lados.contains(v) || !g.lados.contains(u)){
            throw RuntimeException("Alguno de los vertices no pertenece a este grafo")
        }
        return conjuntosDisjuntos.encontrarConjunto(v) == conjuntosDisjuntos.encontrarConjunto(u)
        // Tiempo de ejecucion : O(1), solo se retorna un valor
    }

    // Indica el número de componentes conexas
    fun numeroDeComponentesConexas() : Int {
        
        // Retorna el numero de componentes conexas buscando cada elemento distinto en la lista de conjuntos
        return conjuntosDisjuntos.conjuntos.size
        // Tiempo de ejecucion : O(1), solo se retorna un valor
    }

    
    // Retorna el numero de la componente conexa donde está contenido v
    fun obtenerComponente(v: Int) : Int {

        if (!g.lados.contains(v)){
            throw RuntimeException("El vertice no pertenece a este grafo")
        }

        val listaConjuntos = conjuntosDisjuntos.conjuntos

        var cont = -1
        for ((indice,conj) in listaConjuntos.withIndex()){
            if (conj.contains(v)){
                cont = indice
            }
        }
        return cont
        // Tiempo de ejecucion : O(n), donde n es el numero de componentes
    }

    // Retorna el número de vértices de una componente conexa dada.
    fun numVerticesDeLaComponente(compID: Int) : Int {
        if(compID > numeroDeComponentesConexas()){
            throw RuntimeException("el identificador no pertenece a ninguna componente conexa")
        }
        return conjuntosDisjuntos.conjuntos[compID].size
        // Tiempo de ejecucion : O(1), solo se retorna un valor
    }
}
