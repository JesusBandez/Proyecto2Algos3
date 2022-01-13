
// algoritmo 3
package ve.usb.grafoLib

class ApareamientoVertexScan(val g:GrafoNoDirigido){

    var v2 : MutableList<Int> = mutableListOf()
    val e2 : MutableList<Arista> : mutableListOf()
    val m : MutableList<Arista> = mutableListOf()

    init{


        for (vertice in 0..g.obtenerNumeroDeVertices()-1){
            v2.add(vertice)
        }

        e2 = g.aristas() as MutableList

        // Se ordenan las aristas por costo, esto significa que si tengo el vertice i y quiero encontrar el lado (i,j)
        // con menor costo, este lado sera el primero que contenga a i como fuente que se encuentre
        e2.sortBy{it.costo}

        while (v2.size!=0){
            
            
            //Escoger vertice aleatorio
            //var i : Int = (0..g.obtenerNumeroDeVertices()-1).random()
            var i : Int = v2.random()
            
            for (arista in e2){
                if (arista.cualquieraDeLosVertices == i){
                    var aristaMin : Arista = arista
                }
            }

            var j = aristaMin.elOtroVertice(i)

            m.add(aristaMin)

            v2.remove(i)
            v2.remove(j)

            //e2.remove(arista)
            
            //Eliminar todos los lados que tengan como adyacentes a i y j
            for(lado in e2){
                if(lado.v == i || lado.v==j){
                    e2.remove(lado)
                }
            }
        }
    }
}