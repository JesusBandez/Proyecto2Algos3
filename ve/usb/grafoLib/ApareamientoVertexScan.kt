
// algoritmo 3
package ve.usb.grafoLib

class ApareamientoVertexScan(val g:GrafoNoDirigido){

    var v2 : MutableList<Int> = mutableListOf()
    var e2 : MutableList<Arista> = mutableListOf()
    val m : MutableList<Arista> = mutableListOf()

    var contador : Int = 0

    init{


        for (vertice in 0..g.obtenerNumeroDeVertices()-1){
            v2.add(vertice)
        }

        e2 = g.aristas() as MutableList

        // Se ordenan las aristas por costo, esto significa que si tengo el vertice i y quiero encontrar la arista (i,j)
        // con menor costo, este lado sera el primero que contenga a i
        
        e2.sortBy{it.peso}
        
        while (v2.size>0){
            
            
            //Escoger vertice aleatorio
            var i : Int = v2.random()
            lateinit var aristaMin : Arista

            for (arista in e2){
                if (arista.cualquieraDeLosVertices() == i 
                    || arista.elOtroVertice(arista.cualquieraDeLosVertices()) == i){
                    aristaMin = arista
                    break
                }
            }

            var j = aristaMin.elOtroVertice(i)

            m.add(aristaMin)

            v2.remove(i)
            v2.remove(j)
            
            //Eliminar todos los lados que tengan como adyacentes a i y j
            e2.removeAll{it.cualquieraDeLosVertices() == i || it.elOtroVertice(it.cualquieraDeLosVertices()) == i
                || it.cualquieraDeLosVertices() == j || it.elOtroVertice(it.cualquieraDeLosVertices()) == j}
                   
        }
    }
    
    fun  obtenerApareamiento() : Iterable<Arista>{
        return m
    }
}