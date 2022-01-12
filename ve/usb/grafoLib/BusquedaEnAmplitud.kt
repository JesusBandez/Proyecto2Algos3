package ve.usb.grafoLib

import java.util.LinkedList

/*Clase donde se implementa el algoritmo de Busqueda en Amplitud
El algoritmo se ejecuta sobre el grafo pasado como parametro al instanciar esta clase

g: Grafo de donde se consiguen los datos para ejecutar el algoritmo 
s: vertice raiz desde donde se inicia la ejecucion de BFS
*/
public class BusquedaEnAmplitud(val g: Grafo, val s: Int) {
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
    var propiedadesDeVertice: List<Vertice> = listOf(Vertice())
    

    init {
        /*
        Se ejecuta BFS al instanciar la clase
        Precondicion: s pertenece al grafo
        Postcoindicion: BFS aplicado al grafo
        Tiempo de Ejecucion: O(|V| + |E|) 
            siendo V la cantidad de vertices y E la cantidad de lados
            en el grafo
         */

        // Crea la lista de vertices, estos ya estan inicializados para ejecutar el
        // algoritmo
        propiedadesDeVertice = List(g.obtenerNumeroDeVertices()){Vertice()}

        // Lista mutable que hará la funcion de cola
        // Las listas mutables pueden simular una cola usando los siguientes metodos:
        // cola.pop() == listaMutable.removeAt()
        // cola.push() == listaMutable.add()

        var cola: MutableList<Int> = mutableListOf()

        // Asignar las propiedades al vertice raiz
        propiedadesDeVertice[s].distancia = 0
        propiedadesDeVertice[s].color = Color.GRIS

        // Algoritmo BFS
        cola.add(s)

        while (cola.size != 0){
            var current: Int = cola.removeAt(0)

            for(lado in g.adyacentes(current)){

                var v: Int = lado.elOtroVertice(current)
                
                if (propiedadesDeVertice[v].color == Color.BLANCO){
                    propiedadesDeVertice[v].color = Color.GRIS
                    propiedadesDeVertice[v].distancia = propiedadesDeVertice[current].distancia + 1
                    propiedadesDeVertice[v].predecesor = current
                    cola.add(v)
                }
            }
            propiedadesDeVertice[current].color = Color.NEGRO
        }


    }

    fun obtenerPredecesor(v: Int) : Int? { 
    /*
     Metodo para conseguir el precesor del vertice v. De no tenerlo, se retorna null
        arroja RuntimeException si el vertice no existe 
     Precond: v pertenece al grafo
     Postcond: Se retorna el predecesor del vertice v
     Tiempo de Ejecucion: O(1)
     */
        
        if (!(0 <= v && v < g.obtenerNumeroDeVertices())){
            throw RuntimeException("El vertice $v no existe en el grafo")            
        }

        return propiedadesDeVertice[v].predecesor
            
        
     }

    
     
    fun obtenerDistancia(v: Int) : Int {
        /*
        Retorna la distancia mas corta desde el vertice raiz (s) hasta
        el vertice v. Si v no existe, se arroja una exception
        Precond: v pertenece al grafo
        Postcond: Se retorna el predecesor del vertice v
        Tiempo de Ejecucion: O(1)
         */
        if ( !(0 <= v && v < g.obtenerNumeroDeVertices())){
            throw RuntimeException("No existe el vertice $v en el grafo")
        }

        return propiedadesDeVertice[v].distancia
      }


    fun hayCaminoHasta(v: Int) : Boolean {
        /*
        Metodo que evalua si es posible conseguir un camino que vaya desde 
        el vertice raiz hasta el vertice v
        Precond: v pertenece al grafo
        Postcond: Se retorna true si hay un camino, false en cualquier otro caso
        Tiempo de Ejecucion: O(1)
         */
        
        if ( !(0 <= v && v < g.obtenerNumeroDeVertices())){
            throw RuntimeException("No existe el vertice $v en el grafo")
        }
        
        return propiedadesDeVertice[v].distancia > -1 

      }

  

    
    fun caminoConMenosLadosHasta(v: Int) : Iterable<Int>  {
        /*
        Metodo para hallar el camino con menos lados desde el vertice raiz hasta el vertice v
        precondicion: v pertenece al grafo
        postcondicion: Una lista con los vertices que contienen el camino. La lista inicia en la raiz
        y termina en v
        Tiempo de ejecucion: O(p), con p siendo la cantidad de predecesores por los que haya que
        iterar 
        */

        // Se arroja exception si el vertice no pertenece al grafo
        if ( !(0 <= v && v < g.obtenerNumeroDeVertices())){
            throw RuntimeException("No existe el vertice $v en el grafo")
        }

    
        var camino: MutableList<Int> = mutableListOf()
        var current: Int? = v
        
        // Si no hay un camino hasta v, se retorna una lista vacia
        if (!this.hayCaminoHasta(v)){
            return listOf()
        }


        // Iterar por los predecesores hasta conseguir la raiz        
        while(current!! != s){
            camino.add(current)
            current = this.obtenerPredecesor(current)      
        }

        // Al llegar a la raiz, esta se agrega. Luego, se invierte el orden de la 
        // lista
        camino.add(s)
        camino.reverse()

        return camino
        
      }
    /*
    Clase interna que lleva las propiedades de un vertice perteneciente al grafo.
    El vertice que sea representado por entero 6 tiene sus propiedades en la lista
    propiedadesDeVertice[6].

    color: Variable con el color al que se le asigna al vertice durante la ejecucion del
        algoritmo BFS. Se inicializa con blanco porque es necesario asignar ese color a todos
        los vertices cuando se ejecuta el algoritmo
    predecesor: Indica quien es el predecesor del vertice al ejecutar BFS
    distancia: Lleva cual es la distancia que hay desde el vertice raiz hasta el vertice que esta
        clase representa. Se inicializa con -1 para hacer las pases de la inicializacion de las 
        distancias con infinito. 
    */
    inner class Vertice(
        var color: Color = Color.BLANCO,
        var predecesor: Int? = null, 
        var distancia: Int = -1){}
}
