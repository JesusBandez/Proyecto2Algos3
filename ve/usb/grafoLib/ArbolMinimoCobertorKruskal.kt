package ve.usb.grafoLib


public class ArbolMinimoCobertorKruskal(val g: GrafoNoDirigido) {
    /*
    Clase para aplicar el algoritmo de Kruskal en un grafo g.
    Se usa la libreria de conjuntos disjuntos contenida en este paquete para generar
    el arbol minimo
    */

    // Lista para llevar los arboles que conforman el arbol y variable para el peso del arbol
    var ladosDelArbolMinCobertor: MutableList<Arista> = mutableListOf()
    var pesoDelArbol: Double = 0.0
    init{

        var conjDisjuntos = ConjuntosDisjuntos(g.obtenerNumeroDeVertices())

        // Ordenar la lista de aristas por peso
        var aristas = g.aristas() as MutableList
        aristas.sortBy {it.peso}

        // Aplicar el algoritmo
        for (arista in aristas){
            var u: Int = arista.cualquieraDeLosVertices()
            var v: Int = arista.elOtroVertice(u)

            if (conjDisjuntos.encontrarConjunto(u) != 
                    conjDisjuntos.encontrarConjunto(v)){
                

                ladosDelArbolMinCobertor.add(arista)
                pesoDelArbol += arista.peso()

                conjDisjuntos.union(u, v)
            }
        }
    }

    // Retorna una lista que contiene los lados que pertenecen al
    // arbol minimo cobertor
    fun obtenerLados() : Iterable<Arista> {
    /* 
     Precondicion: true
     Prostcoindicion: Se retorna una lista con los lados del arbol cobertor minimo
     Tiempo O(1)
    */
        return ladosDelArbolMinCobertor
	
    }
    
    // Retorna el peso del árbol mínimo cobertor. 
    fun obtenerPeso() : Double {
   /* 
     Precondicion: true
     Prostcoindicion: Se retorna el peso del arbol cobertor
     Tiempo O(1)
    */
        return pesoDelArbol
    }
}
