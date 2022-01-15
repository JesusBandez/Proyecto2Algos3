package ve.usb.grafoLib

import java.io.File

public class GrafoNoDirigido: Grafo {
    /* listaDeAdyacencias: Lista de listas mutables donde las listas mutables contienen 
     aristas. 
     El formato para obtener las adyacencias es el siguiente: el indice de la lista 
     externa corresponde a uno de los vertices que pertenecen a la arista, la lista mutable 
     correspondiente a ese indice contiene el objeto arista. El objeto arista contiene
     los identificadores numericos que inciden en ella */

    private var listaDeAdyacencias: List<MutableList<Arista>> = List(0){mutableListOf()}

    // Una lista donde se guardan todas las aristas del grafo. Es necesario usarla ya
    // que en caso de necesitar todos los lados, es más rápido retornar esta lista que 
    // recorrer la listaDeAdyacencias y sacar uno a uno las aristas asegurandose que estas
    // no se repitan
    private var listaDeAristas: MutableList<Arista> = mutableListOf()

    // lleva el conteo de los vertices del grafo
    private var numeroDeVertices = 0


    
    constructor(numDeVertices: Int) {
        /* Se construye el grafo a partir del número de vértices
        
        nnumDeVertices: Numero de vertices que tendrá el grafo
        Precond: numDeVertices > 0
        PostCond: Grafo vacio de tamano nnumDeVertices
        Tiempo de ejecucion: O(1)
        */
        listaDeAdyacencias = List(numDeVertices){mutableListOf()}
        numeroDeVertices = numDeVertices
    }

  
    constructor(nombreArchivo: String, conPeso: Boolean) {
       /* Se construye el grafo con los datos contenidos en un archivo.

        nombreArchivo: String con el nombre del archivo donde estan los datos
        conPeso: Indica si archivo contiene datos con pesos para los lados del grafo

        Precond: nombreArchivo != ""
        Postcond: Grafo armado con los datos contenido en nombreArchivo 
        Tiempo de ejecucion: O(n), donde n es la cantidad de lineas del archivo
         */

        
        var archivo: File = File(nombreArchivo) 

        // Comprueba que existe el archivo, en caso contrario, se arroja RuntimeException
        if (!archivo.exists()){
            throw RuntimeException("No existe el archivo ${nombreArchivo}")
        }

        var contadorDeLinea = 1

        // Para cada linea del archivo:
	    File(nombreArchivo).forEachLine() {

            // Si es la primera linea, se obtiene el numero de vertices y se inicializa un
            // una lista de tamano numeroDeVertices que contiene listas mutables 
            if (contadorDeLinea == 1){
                numeroDeVertices = it.trim().toInt()
                listaDeAdyacencias = List(numeroDeVertices){mutableListOf()}
            
            } else if (contadorDeLinea == 2){
                // Para la linea 2, no hacer nada
                // el numero de lados se obtiene del tamano de la lista
                // contenida en listaDeAristas
     
            } else {
                // Para las demas lineas, se obtienen los datos de los arcos

                // Terminar la funcion si una linea está en blanco
                if (it.isBlank()){
                    return@forEachLine
                }

                // Obtener los datos de linea y crear una arista con ellos
                var lineaDiv: List<String> = it.split(" ")                
                var arista: Arista
                
                if (conPeso) {
                    arista = Arista(
                    lineaDiv[0].trim().toInt(), 
                    lineaDiv[1].trim().toInt(),
                    lineaDiv[2].trim().toDouble())
                } else {
                    arista = Arista(
                    lineaDiv[0].trim().toInt(), 
                    lineaDiv[1].trim().toInt())
                }

                // Agregar la arista al grafo
                this.agregarArista(arista)

            }         
        contadorDeLinea++            
        }
    }

    
    fun agregarArista(a: Arista) {
        /* Se Agrega un lado al grafo no dirigido

        a: Objeto tipo arista que se agrega al la lista de adyacencias

        Precond: 0 <= a.u() < this.numeroDeVertices
        Postcond: El arco a agregado al grafo, si este no estaba antes
        Tiempo de ejecucion: O(1)
         */

        // Es posible que a.u == a.v, se debe descartar ese caso porque no
        // pueden haber bucles en un grafo no dirigido
        if (a.u == a.v){
            return
        }
        if (!(0 <= a.u && a.u < this.numeroDeVertices) || !(0 <= a.v && a.v < this.numeroDeVertices)){
            throw RuntimeException("El vertice ${a.u} o el vertice ${a.v} no pertenece al grafo")
        }

        // Se agrega la arista como incidente en el vertice a.u y el a.v
        listaDeAdyacencias[a.u].add(a)
        listaDeAdyacencias[a.v].add(a)

        // Agregar la arista a la lista de aristas
        listaDeAristas.add(a)
        

    }

   
    override fun obtenerNumeroDeLados() : Int {
       /*Retorna el numero de lados del grafo
        Precond: true
        Postcond: true
        Tiempo de ejecucion: O(1)
         */
	    return listaDeAristas.size

    }

    
    override fun obtenerNumeroDeVertices() : Int {
        /*Retorna el numero de vertices del grafo
        Precond: true
        Postcond: true
        Tiempo de ejecucion: O(1)
         */
	    return numeroDeVertices


    }

   
    override fun adyacentes(v: Int) : Iterable<Arista> {
        /*Retorna una lista que contiene las aristas donde el vertice v 
        es incidente
        Precond: 0 <= v < numeroDeVertices
        Postcond: true
        Tiempo de ejecucion: O(1)
         */

        // retorna la lista mutable donde residen las aristas
        // en las cuales el vertice v es incidente
	    return listaDeAdyacencias[v]

    }

    
    fun aristas() : Iterable<Arista> {
        /*Retorna la lista que contiene todas las aristas
        Precond: true
        Postcond: true
        TiempoDeEjecucion: O(1)
         */
        
	    return listaDeAristas

    }

    override fun grado(v: Int) : Int {
        /*
        Metodo para retornar el grado del vertice v, en caso de que no exista, se arroja una 
        excepcion 
        Precondicion: v pertenece al grafo
        Postcondicion: Se retorna el grado del grafo
        Tiempo de ejecucion: O(1)
        */
        if (v < 0 || v > numeroDeVertices-1){
            throw RuntimeException("No existe el vertice ${v} en el grafo")
        }        

        return listaDeAdyacencias[v].size
    }

   
    override fun toString() : String {
        /* Metodo para retornar un String con la representacion del grafo. 
        El string tiene el siguiente formato para cada vertice:

        verticeInicial | verticeFinal1:pesoDelLado verticieFinal2:pesoDelLado ... verticeFinaln:pesoDelLado

        ejemplo con un grafo de 3 vertices donde hay 4 lados (de 0 a 1 con peso 2.3, de 0 a 2 con peso 3.1, 
        de 0 a 0 con peso 0.0 y de 1 a 0 con peso 0.0):

        0 | [1:2.3] [2:3.1]
        1 | [0:2.3]
        2 | [0:3.1]

        Es necesario tener en cuenta que si está el "arco" (1,2) tambien debe estar
         el (2,1). Ademas, no se permiten bucles

        Precondicion: true
        Postcoindicion: grafoEnString una string que representa al grafo con todos sus contenidos
        TiempoDeEjecucion: O(n)
         */

        // String vacía
        var grafoEnString: String  = ""

        // Los indices de listaDeAdyacencias corresponden al vertice inicial y 
        // la lista mutable en ese indice contiene las aristas donde el vertice
        // inicial incide

        for ((verticeInicial, aristasIncidentes) in listaDeAdyacencias.withIndex()){

            // Se escribe el identificador del vertice inicial
            grafoEnString += "${verticeInicial} |"

            // Se escriben los demás vetices con los que verticeInicial comparte arista
            // y el peso de cada lado en el resto de la linea 
            for (arista in aristasIncidentes){
                grafoEnString += " [${arista.elOtroVertice(verticeInicial)}:${arista.peso()}]"
            }

            // salto de linea
            grafoEnString += "\n"
        }
        
        return grafoEnString    



     }
}
