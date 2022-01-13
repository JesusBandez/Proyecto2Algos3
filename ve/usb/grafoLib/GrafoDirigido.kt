package ve.usb.grafoLib

import java.io.File

public class GrafoDirigido : Grafo {
    /* 
    listaDeAdyacencias: Lista de listas mutables donde las listas mutables contienen 
    arcos. 
    El formato para obtener las adyacencias es el siguiente: el indice de la lista 
    externa corresponde al vertice inicial, la lista mutable correspondiente a ese
    indice contiene los arcos donde el vertice inicial es incidente.
    */
    private var listaDeAdyacencias: List<MutableList<Arco>> = List(0){mutableListOf()}

    // lleva el conteo de los lados del grafo
    private var numeroDeLados = 0
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

    
    constructor(nombreArchivo: String, conPeso: Boolean)  {
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
	    archivo.forEachLine() {

            
        when (contadorDeLinea){

            // Si es la primera linea, se obtiene el numero de vertices y se inicializa un
            // una lista de tamano numeroDeVertices que contiene listas mutables 
            1-> {
                numeroDeVertices = it.trim().toInt()
                listaDeAdyacencias = List(numeroDeVertices){mutableListOf()}                    
            }

             // Para la segunda linea no se hace nada, el numero de lados se obtiene al contar
             // cada lado que se va agregando
            2 -> {}

            // Para las demas lineas, se obtienen los datos de los arcos
            else -> {
                
                // Terminar la funcion si una linea está en blanco
                if (it.isBlank()){
                    return@forEachLine
                }

                // Obtener los datos de linea y crear un arco con ellos
                var lineaDiv: List<String> = it.split(" ")                
                var arco: Arco
                
                if (conPeso) {
                    arco = Arco(
                    lineaDiv[0].trim().toInt(), 
                    lineaDiv[1].trim().toInt(),
                    lineaDiv[2].trim().toDouble())
                } else {
                    arco = Arco(
                    lineaDiv[0].trim().toInt(), 
                    lineaDiv[1].trim().toInt())
                }

                // Agregar el arco al grafo
                this.agregarArco(arco)

            } 
        }

        contadorDeLinea++            
        }
    }

    
    fun agregarArco(a: Arco) {
       /* Se Agrega un lado al digrafo

        a: Objeto tipo arco que se agrega al la lista de adyacencias

        Precond: 0 <= a.fuente() < this.numeroDeVertices
        Postcond: El arco a agregado al grafo, si este no estaba antes
        Tiempo de ejecucion: O(1)
         */
        // Si el arco ya existe, no se agrega
        

        
        if (!(0 <= a.sumidero() && a.sumidero() < numeroDeVertices)){
            throw RuntimeException("El arco ${a} tiene como sumidero un vertice que no existe en el grafo")
        }

        // agregar el lado a la correspondiente lista mutable
        listaDeAdyacencias[a.fuente()].add(a)

        numeroDeLados ++
            
        

    }

   
    override fun grado(v: Int) : Int {
        /*
        Metodo para retornar el grado de un vertice.
        Precondicion: v pertenece al grafo
        Postcondicion: Se retorna el grado de v
        Tiempo de ejecucion: O(n)
         */
        if (v < 0 || v > numeroDeVertices-1){
            throw RuntimeException("No existe el vertice ${v} en el grafo")
        }

        // sumar el grado interior mas el grado exterior del vertice
        var grado: Int = this.gradoInterior(v)
        grado += this.gradoExterior(v)
         

        return grado
    }

    
    fun gradoExterior(v: Int) : Int {
        /*
        Retorna el grado exterior del vertice v. En caso de que v no pertenezca al grafo
        se arroja una exception 
        Precond: v pertenece al grafo
        Postcon: se retorna el grado exterior de v
        Tiempo de ejecucion: O(n)
        */
        if (v < 0 || v > numeroDeVertices-1){
            throw RuntimeException("No existe el vertice ${v} en el grafo")
        }

        var grado: Int = 0

        // Iterar por cada arco en cada lista de adyacencias de los vertices, excepto por 
        // las adyacencias de v, sumando los arcos en donde v es el sumidero.
       for ((vertice, arcosAdyacentes) in listaDeAdyacencias.withIndex()){
            if (vertice == v){
                continue
            }
            else{
                for (arco in arcosAdyacentes){
                    if (arco.sumidero() == v){
                        grado += 1
                    }
                }
            }
        }

        return grado
    }

    
    fun gradoInterior(v: Int) : Int {
        /*
        Retorna el grado interior del vertice v. En caso de que v no pertenezca al grafo
        se arroja una exception 
        Precond: v pertenece al grafo
        Postcon: se retorna el grado interior de v
        Tiempo de ejecucion: O(1)
        */
       if (v < 0 || v > numeroDeVertices-1){
            throw RuntimeException("No existe el vertice ${v} en el grafo")
        }

        return listaDeAdyacencias[v].size

    }

    
    override fun obtenerNumeroDeLados() : Int {
        /*Retorna el numero de lados del grafo
        Precond: true
        Postcond: true
        Tiempo de ejecucion: O(1)
         */
	    return numeroDeLados
    }

   
    override fun obtenerNumeroDeVertices() : Int {
        /*Retorna el numero de lados del grafo
        Precond: true
        Postcond: true
        Tiempo de ejecucion: O(1)
         */
	    return numeroDeVertices

    }

    /* 
     Retorna los adyacentes de v, en este caso los lados que tienen como vértice inicial a v. 
     Si el vértice no pertenece al grafo se lanza una RuntimeException
     */
    override fun adyacentes(v: Int) : Iterable<Arco> {
        /*Retorna una lista que contiene todos los arcos donde el vertice v 
        es el vertice inicial
        Precond: 0 <= v < numeroDeVertices
        Postcond: true
        Tiempo de ejecucion: O(1)
         */

        if (v < 0 || v > numeroDeVertices-1){
            throw RuntimeException("No existe el vertice ${v} en el grafo")
        }

        // Toma la lista mutable que contiene los arcos con vertice inicial v y 
        // la retorna
        return listaDeAdyacencias[v]

    }

   
    fun arcos() : Iterable<Arco> {
        /*Retorna una lista que contiene todos los arcos del grafo
        Precond: true
        Postcond: true
        TiempoDeEjecucion: O(1)
         */

        // Concatena todas las listas mutables en la lista de adyacencias
        // y la retorna
        
        return listaDeAdyacencias.flatten()

    }
    
    
    override fun toString() : String {
       /* Metodo para retornar un String con la representacion del grafo. 
        El string tiene el siguiente formato para cada vertice:

        verticeInicial | verticeFinal1:pesoDelLado verticieFinal2:pesoDelLado ... verticeFinaln:pesoDelLado

        ejemplo con un grafo de 3 vertices donde hay 4 lados (de 0 a 1 con peso 2.3, de 0 a 2 con peso 3.1, 
        de 0 a 0 con peso 0.0 y de 1 a 0 con peso 0.0):

        0 | [1:2.3] [2:3.1] [0:0.0]
        1 | [0:0.0]
        2 |

        Precondicion: true
        Postcoindicion: grafoEnString una string que representa al grafo con todos sus contenidos
        TiempoDeEjecucion: O(n)
         */

        // String vacía
        var grafoEnString: String  = ""

        // Los indices de listaDeAdyacencias corresponden al vertice inicial y 
        // la lista mutable en ese indice contiene los arcos donde el vertice
        // inicial incide
        for ((verticieInicial, arcosIncidentes) in listaDeAdyacencias.withIndex()){

            // Se escribe el identificador del vertice inicial
            grafoEnString += "${verticieInicial} |"

            // se escribe los vertices finales y el peso de cada lado en el resto de la linea            
            for (arco in arcosIncidentes){
                grafoEnString += " [${arco.sumidero()}:${arco.peso()}]"
            }

            // salto de linea
            grafoEnString += "\n"
        }

        return grafoEnString

    }
}
