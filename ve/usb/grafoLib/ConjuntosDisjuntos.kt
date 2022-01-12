package ve.usb.grafoLib


public class ConjuntosDisjuntos(val n: Int) {
/*
clase que implementa la estuctura de datos conjuntos disjuntos como arboles
 Al instanciar la clase se genera la estructura de conjuntos disjuntos con cada
 elemento de 0 a n-1 en un singleton
*/

    val listaDeconjuntos: List<Conjunto> = List(n, {Conjunto(it)})
    var componentesConexas: Int = n

    
    fun union(v: Int, u: Int) : Boolean {
        /* Metodo para unir dos componentes conexas. Si se logra la union retorna true. Si no, false
        Si uno de los vertices no pertence al grafo, se arroja una exception */
        if ( ! ((0 <= v && v < listaDeconjuntos.size) || 
            (0 < u && u < listaDeconjuntos.size))){
                throw RuntimeException("Uno de los vertices no pertenece al grafo")
            }

        
        var representanteDeV: Int = encontrarConjunto(v)
        var representanteDeU: Int = encontrarConjunto(u)

        if (representanteDeV == representanteDeU){
            return false
        }

        link(representanteDeV, representanteDeU)
        componentesConexas--
        return true
    }

    fun link(v: Int, u: Int){
        if (listaDeconjuntos[v].rank > listaDeconjuntos[u].rank){
            listaDeconjuntos[u].p = listaDeconjuntos[v]
        } else {
            listaDeconjuntos[v].p = listaDeconjuntos[u]

            if(listaDeconjuntos[v].rank == listaDeconjuntos[u].rank){
                listaDeconjuntos[u].rank++
            }

        }
    }

   
    fun encontrarConjunto(v: Int) : Int {
        /* Metodo que recibe un elemento que pertenece a alguno de los conjuntos y retorna
        el representante de ese conjunto disjunto. Si el elemento no pertenece, se arroja una excepcion */
       if ( !(0 <= v && v < listaDeconjuntos.size)){
                throw RuntimeException("El vertice ${v} no pertenece al grafo")
            }
            
        if (listaDeconjuntos[v] != listaDeconjuntos[v].p){
            listaDeconjuntos[v].p = listaDeconjuntos[encontrarConjunto(
                listaDeconjuntos[v].p.identificador)]
        }

        return listaDeconjuntos[v].p.identificador
    }
 
    // Retorna la cantidad de conjuntos disjuntos en la estructura
    fun numConjuntosDisjuntos() : Int {

        return componentesConexas
    }

    // Clase interna que representa a un nodo perteneciente
    // a un arbol. Con esta clase se construye la estructura de conjuntos disjuntos
    // de manera que rank es la propiedad rank del nodo y p es la propiedad padre
    // del nodo. El conjunto que se tenga como padre a si mismo es el representante
    // del conjunto
    inner class Conjunto(var identificador: Int ){
        var p: Conjunto = this
        var rank: Int = 0   
    }
}
