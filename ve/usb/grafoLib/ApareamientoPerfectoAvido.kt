
// algoritmo 2
package ve.usb.grafoLib
import java.util.PriorityQueue


class ApareamientoPerfectoAvido(val g:GrafoNoDirigido){

    var emp_perfecto : MutableList<Arista> = mutableListOf()
    var v2 : MutableList<Int> = mutableListOf()
    val l: PriorityQueue<Arista>

    init{

        for (vertice in 0..g.obtenerNumeroDeVertices()-1){
            v2.add(vertice)
        }

        val compareByCost: Comparator<Arista> = compareBy { it.costo }

        l = PriorityQueue<Arista>(compareByCost)

        for (arista in g.aristas()){
            // cola de prioridad con min heap, como la cabeza es el min está ordenada de manera creciente
            l.add(arista)
        }

        while (v2.size!=0){

            var arista : Arista = l.remove()

            var i : Int = arista.cualquieraDeLosVertices()
            var j : Int = arista.elOtroVertice(arista.cualquieraDeLosVertices())

            if (v2.contains( i ) && v2.contains( j )){

                //añadir el lado a M
                emp_perfecto.add(arista)

                // remover de V' i y j
                v2.remove(i)
                v2.remove(j)
            }
        }
    } 
    // falta retornar M
}