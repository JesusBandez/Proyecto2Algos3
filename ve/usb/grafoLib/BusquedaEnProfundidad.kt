package ve.usb.grafoLib

import java.util.LinkedList

/*Clase donde se implementa el algoritmo de Busqueda en profundidad
El algoritmo se ejecuta sobre el grafo pasado como parametro al instanciar esta clase

g: Grafo de donde se consiguen los datos para ejecutar el algoritmo
*/
public class BusquedaEnProfundidad(val g: Grafo) {
    /* 
    Lista que contiene objetos de tipo vertice. La clase vertice en una clase interna
    definida al final de este archivo
    
    Esta lista contiene los atributos de los vertices como la distancia,
    color y predecesor. Para conseguir las propiedades del vertice i se busca
    el objeto vertice contenido en propiedadesDeVertice[i]
    Por ejemplo:
    Si el vertice 3 tiene color blanco, entonces la lista propiedadesDeVertice tiene
    un objeto tipo vertice con la propiedad color = blanco en el indice 3
    */
    var propiedadesDeVertice: List<Vertice> = List(0){Vertice()}

    // variable "global" para ejecutar el algoritmo
    var tiempo: Int = 0
    
    init {
       /*
        Se ejecuta DFS al instanciar la clase
        Precondicion: True
        Postcoindicion: BFS aplicado al grafo
        Tiempo de Ejecucion: O(|V| + |E|) 
            siendo V la cantidad de vertices y E la cantidad de lados
            en el grafo
         */
    
        // Crea la lista de vertices, estos ya estan inicializados para ejecutar el
        // algoritmo
        propiedadesDeVertice = List(g.obtenerNumeroDeVertices()){Vertice()}


        // Aplicar el algoritmo DFS
        for(indexVertice in 0 until g.obtenerNumeroDeVertices()){
            if (propiedadesDeVertice[indexVertice].color == Color.BLANCO){
                dfsVisit(g, indexVertice)
            }
        }
        
    
    }

    // Metodo dfsVisit del algoritmo DFS
    private fun dfsVisit(g: Grafo, u: Int) {
        tiempo +=  1
        propiedadesDeVertice[u].tiempoInicial = tiempo
        propiedadesDeVertice[u].color = Color.GRIS
        
        for (lado in g.adyacentes(u)){
            var v: Int = lado.elOtroVertice(u)

            if (propiedadesDeVertice[v].color == Color.BLANCO){
                propiedadesDeVertice[v].predecesor = u
                dfsVisit(g, v)
            }
        }

        propiedadesDeVertice[u].color = Color.NEGRO
        tiempo += 1
        propiedadesDeVertice[u].tiempoFinal = tiempo
        

    }


    fun obtenerPredecesor(v: Int) : Int? {
    /*
     Metodo para conseguir el precesor del vertice v. De no tenerlo, se retorna null.
        Se arroja RuntimeException si el vertice no existe 
     Precond: v pertenece al grafo
     Postcond: Se retorna el predecesor del vertice v
     Tiempo de Ejecucion: O(1)
     */
        if ( !(0 <= v && v < g.obtenerNumeroDeVertices())){
            throw RuntimeException("No existe el vertice $v en el grafo")
        }

        return propiedadesDeVertice[v].predecesor
     }



    fun obtenerTiempos(v: Int) : Pair<Int, Int> {
        /*
        Retorna los tiempos iniciales y finales que se le asignan
        al vertice al aplicarle DFS. Los datos se retornan en una Par
        donde el primer elemento es el tiempo inicial, y el segundo el
        final
        Precond: v pertenece al grafo
        Postcond: Se retorna un par con los tiempos de v
        Tiempo de Ejecucion: O(1)

         */
        if ( !(0 <= v && v < g.obtenerNumeroDeVertices())){
            throw RuntimeException("No existe el vertice $v en el grafo")
        }

        var par: Pair<Int, Int> = Pair(
            propiedadesDeVertice[v].tiempoInicial, 
            propiedadesDeVertice[v].tiempoFinal)

        return par
      }

    /*
    Clase interna que lleva las propiedades de un vertice perteneciente al grafo.
    El vertice que sea representado por entero 6 tiene sus propiedades en la lista
    propiedadesDeVertice[6].

    color: Variable con el color al que se le asigna al vertice durante la ejecucion del
        algoritmo BFS. Se inicializa con blanco porque es necesario asignar ese color a todos
        los vertices cuando se ejecuta el algoritmo
    predecesor: Indica quien es el predecesor del vertice al ejecutar BFS
    tiempoInicial: Momento en el que es descubierto el vertice y cuando empieza
        a explorarse.
    tiempoFinal: Momento en el que se termina de explorar el algoritmo
    */
    inner class Vertice(
        var color: Color = Color.BLANCO,
        var predecesor: Int? = null,
        var tiempoInicial: Int = -1, 
        var tiempoFinal: Int = -1){
            
        }
}
 
