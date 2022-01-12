package ve.usb.grafoLib
import java.io.File
import kotlin.math.abs


public class Sol2SAT(nombreArchivo: String) {
/* 
 Clase para resolver problemas del tipo 2SAT. Al instanciar la clase
  se carga un grafo con la representacion de la formula dada en el nombreArchivo.
  Luego se trabaja sobre dicha formula para conseguir los resultados  
*/

    // Grafo para representar la formula
    var grafo: GrafoDirigido 
    // Variable para llevar si se puede hacer true la formula dada
    var puedeAsignarseTrue: Boolean = true

    // Lista que contiene la asignacion de cara literal para que la formula sea verdadera
    var asignacionDeVariable: MutableList<Boolean> = mutableListOf()

    init{
        /* El algoritmo para determinar si existe la asignacion y conseguirla se ejecuta al
        instanciar la clase.
        precond: El archivo contiene una formula segun el formato especificad
        postcond: Se consigue si es posible asignar true a dicha formula y la asignacion de las 
           variables  
        Tiempo de ejecucion: O(|N| + |E|)*/


        // Cargar el archivo para crear el grafo
        //Se recorre el archivo completo para buscar la cantida de vertices con el que se debe iniciar el grafo.
        var archivo: File = File(nombreArchivo) 

        if (!archivo.exists()){
            throw RuntimeException("No existe el archivo ${nombreArchivo}")
        }
        var mayor: Int = 0
        
        archivo.forEachLine() {

            if (it.isBlank()){
                return@forEachLine
            }

            var lineaDiv: List<String> = it.split(" ")

            var num: Int = abs(lineaDiv[0].trim().toInt())
            if (num > mayor){
                mayor = num
            }
            num = abs(lineaDiv[1].trim().toInt())
            if (num > mayor){
                mayor = num
            }
        }

        // Crear un grafo dirigido con 2*mayor vertices+2   
        grafo = GrafoDirigido(2*mayor + 2)
        // El grafo guarda en la posicion 2*n al vertice n. Esto es, si se quieren
        // conocer las adyacencias del vertice 4. Entonces se usa g.adyacencias(2*4). De igual forma,
        // el negado del literal representado por n se encuentra en n+1. Esto es, si se quieren encontrar
        // las adyacencias del negado de 4, entonces se usa g.adyacencias(2*4 + 1)
       
        // Volver a leer el archivo para llenar los lados del grafo
        archivo.forEachLine() {
            if (it.isBlank()){
                return@forEachLine
            }
            var lineaDiv: List<String> = it.split(" ")
            var izq = abs(lineaDiv[0].trim().toInt())*2
            var der = abs(lineaDiv[1].trim().toInt())*2

            // Si se tiene el primer elemento negado
            if (lineaDiv[0].get(0) == '-' ){

                // Si se tiene ambos negados
                 if (lineaDiv[1].get(0) == '-' ){

                     // agregar izq a ¬der
                     grafo.agregarArco(Arco(izq, der+1))

                     // agregar der a ¬izq
                     grafo.agregarArco(Arco(der, izq+1))

                 } else {
                    // Solo el primero negado

                    // agregar izq a der
                    grafo.agregarArco(Arco(izq, der))

                    // agregar ¬der a ¬izq
                    grafo.agregarArco(Arco(der+1, izq+1))
                 }
            // Se tiene solo el segundo negado
            } else if (lineaDiv[1].get(0) =='-'){

                // agregar ¬izq a ¬der
                grafo.agregarArco(Arco(izq+1, der+1))

                // agregar der a izq
                grafo.agregarArco(Arco(der, izq))

            } else {
                // Ninguno negado

                // agregar ¬der a izq
                grafo.agregarArco(Arco(der+1, izq))
                // agregar ¬izq a der
                grafo.agregarArco(Arco(izq+1, der))
            }

        }

        // Conseguir las componentes fuertemente conexas del grafo
        var cfc = CFC(grafo)
        
        // Ahora se debe comprobar si existe un literal en el grafo el cual
        // el y su negado estan en la misma componente conexa. Si lo existe, entonces
        // no se puede asignar true a la expresion.
        for (vertice in 0 until grafo.obtenerNumeroDeVertices() step 2){

            var verticeNegado = vertice + 1

            if (cfc.estanFuertementeConectados(vertice, verticeNegado)){
                puedeAsignarseTrue = false
                break
            }

        }
        
        // Si es posible asignarle true a la formula, se busca que asignacion debe ser
        if (puedeAsignarseTrue) {
            
            // Obtener el grafo asociado a las componentes     
            var grafoComponente = cfc.obtenerGrafoComponente()
            // Conseguir el ordenamiento topologico del grafo componete
            var ordenTopologico = OrdenTopologico(grafoComponente).obtenerOrdenTopologico()


            // Para cada literal y su negado en el grafo
            for (literal in 0 until grafo.obtenerNumeroDeVertices() step 2){

                // Obtener el identificador de componente de ambos y buscar en el
                // orden topologico cual se consigue primero
                var componenteDeLiteral = cfc.obtenerIdentificadorCFC(literal)
                var componenteDeNoLiteral = cfc.obtenerIdentificadorCFC(literal+1)
                // Iterar por el odenamiento topologico buscando quien es el menor
                // de ambos
                for (elemento in ordenTopologico){
                    // Si la componente del literal es menor en el orden
                    if (componenteDeLiteral == elemento){

                        // La asignacion de la variable debe ser false
                        asignacionDeVariable.add(false)
                        break

                    // Si la componente del no literal es menor en el orden
                    } else if(componenteDeNoLiteral == elemento) {

                        // La asignacion de la variable debe ser true
                        asignacionDeVariable.add(true)
                        break
                    }
                }
            }
        }
    }

    
    fun tieneAsignacionVerdadera() : Boolean {
        /*Retorna true si es posible ahacer verdadera la fórmula dada, si no retorna false.  
        precond: True
        postcond: Retorna si es posible hacer verdadera la formula
        tiempo de ejecucion: O(1)
        */
        return puedeAsignarseTrue

    }

 
    fun asignacion() : Iterable<Boolean> {
        /* Metodo que retorna una lista con las asignaciones que debe tener cada variable para hacer
             verdadera la formula. La posición en la lista es el indice de la variable.
             Por ejemplo, si se tienen n variables, entonces la lista corresponde a [ X0, X1, ..., Xn-1], y si se 
             tiene que todos son true, entonces se obtiene [true, true, ..., true]
             Si la asignacion no es posible, entonces se arroja una exception
            precond: Se puede hacer verdadera la formula
            postcond: Se retorna la asignacion de las variables
            tiempo de ejecucion: O(1)
         */ 

        if (!puedeAsignarseTrue){
            throw RuntimeException("No se puede hacer verdadera la formula")
        }

        return asignacionDeVariable
    }
}
