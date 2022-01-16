
import java.io.File
import java.time.Instant

fun main(args: Array<String>) {
    
    // Cuando se lee el txt agregar la arista (u,v) con costo cv1 y la arita (v,u) con costo cv2
    // Modificar agregar arista para este fin

    // Guardar el momento en el que inicia el algoritmo
    val inicio = Instant.now().toEpochMilli()

    val algoritmo : String = args[0]
    val instancia : String = args[1]  
    var aristasReq : Int 
    var vertices : Int
    var nombre : String
    var costo : Double = 0.0
    


    var archivo = File(instancia).bufferedReader().readLines()

    var linea0 = archivo[0].split(" ").toList()
    linea0 = linea0.filter { it != "" && it != " "}
    nombre = linea0[2]

    var linea2 = archivo[2].split(" ").toList()
    linea2 = linea2.filter { it != "" && it != " "}
    vertices = linea2[2].toInt()

    var linea3 = archivo[3].split(" ").toList()
    linea3 = linea3.filter { it != "" && it != " "}
    aristasReq = linea3[2].toInt()

    println(nombre)

    var linea : List<String>

    // Grafo que contiene solo los lados requeridos
    val gRequerido : GrafoNoDirigido = GrafoNoDirigido(vertices)
    val grafoCompleto : GrafoNoDirigido = GrafoNoDirigido(vertices)

    for (i in 6..archivo.size-1){

        if(i == aristasReq + 6){
            continue
        }
        
        // Se lee la linea i del archivo
        // Se filtra para eliminar todos los espacios innecesarios
        linea = archivo[i].split(" ").toList()
        linea = linea.filter { it != "" && it != " "}
        
        var u : Int
        var v : Int
        var cv1 : Double  
        
        // Para cuando el valor de algun vertice es 1000, el split no funciona de la misma manera
        // Porque en este caso no hay espacio entre los valores
        if (linea.size <= 5){
            
            var lineaM = linea[1].split(",")

            u = lineaM[0].toInt() - 1
            v = lineaM[1].dropLast(1).toInt() - 1
            cv1 = linea[3].toDouble()      

        }else{

            u = linea[1].dropLast(1).toInt() - 1
            v = linea[2].dropLast(1).toInt() - 1
            cv1 = linea[4].toDouble()
          
        }

        var arista1 = Arista(u,v,cv1)                    
        grafoCompleto.agregarArista(arista1)
    }
    

    // Se recorren las lineas del archivo a partir de la 6ta linea hasta el valor de aristas requeridas
    // Las aristas no requeridas no nos importan
    // Se les resta 1 a cada vertice para que comiencen en 0  y que no explote el algoritmo xdxdx
    for (i in 6..aristasReq + 6 - 1){
    
        linea = archivo[i].split(" ").toList()
        linea = linea.filter { it != "" && it != " "}
        //var lineaM = linea[1].split(",")

        var u : Int
        var v : Int
        var cv1 : Double
        var cv2 : Double 
        
        if (linea.size <= 5){
            // Cuando los numeros de los vertices son muy grandes explotaba sin esto xd
            var lineaM = linea[1].split(",")

            u = lineaM[0].toInt() - 1
            v = lineaM[1].dropLast(1).toInt() - 1
            cv1 = linea[3].toDouble()
            cv2 = linea[4].toDouble()    

        }else{

            u = linea[1].dropLast(1).toInt() - 1
            v = linea[2].dropLast(1).toInt() - 1
            cv1 = linea[4].toDouble()
            cv2 = linea[5].toDouble()
        }
        
        gRequerido.agregarArista(Arista(u,v,cv1))
        if (cv1 != cv2 ){
            print("error, pesos distintos")
        }
        
    }

    // Se establece un "isomorfismo" entre gRequerido y gPrima
    // gRequerido es un grafo que tiene tantos vertices como el grafo original pero sólo tiene los lados
    //      requeridos
    // gPrima es el grafo que se genera por los vertices incidentes en un lado requerido y los lados requeridos    
    // Los siguientes diccionarios son usados para conservar la relacion entre un vertice en grafo gRequerido y
    // un vertice en el grafo gPrima. En caso de que se quiera buscar el vertice i de gRequerido
    // en gPrima, se usa vertice = deGRequeridoAGPrima.get(i). Si se quiere lo contrario, se usa
    // vertice = deGprimaAGRequerido(i)

    var deGprimaAGRequerido: MutableMap<Int, Int> = mutableMapOf()
    var deGRequeridoAGPrima: MutableMap<Int, Int> = mutableMapOf()

    // Es necesario contar la cantidad de vertices que son incidentes en un lado
    // requerido
    var contadorDeVerticesRequeridos: Int = 0

    // Ahora se cuentan los vertices requeridos y se llena los diccionarios con la correspondencia
    // que tendrán los vertices de gRequerido a gPrima y viceversa
    for (vertice in 0 until vertices){

        // Si el vertice tiene grado mayor a 0, entonces
        // es incidente en un lado requerido
        if (gRequerido.grado(vertice)>0){

            // Se establece la relacion que tiene el vertice de gRequeridos
            // con el que tendra gPrima
            deGprimaAGRequerido.put(contadorDeVerticesRequeridos, vertice)
            deGRequeridoAGPrima.put(vertice, contadorDeVerticesRequeridos)
            
            contadorDeVerticesRequeridos++
        }
    }

    // Crear gPrima con solo los vertices que son incidentes en un lado requerido
    var gPrim = GrafoNoDirigido(contadorDeVerticesRequeridos)

    // Conseguir los lados de gPrima usando las relaciones entre los vertices
    for (arista in gRequerido.aristas()){
        var unoDeLosVertices = arista.cualquieraDeLosVertices()

        var vertice = deGRequeridoAGPrima.get(unoDeLosVertices)!!
        var otroVertice = deGRequeridoAGPrima.get(arista.elOtroVertice(unoDeLosVertices))!!
        
        gPrim.agregarArista(Arista(vertice, otroVertice, arista.peso()))
    }

    // Verificar que G' sea conexo (que todos sus vertices esten conectados por un camino)    
    // Conseguir el grafo dirigido asociado a gPrim
    var gPrimDirigidoAsociado = GrafoDirigido(gPrim.obtenerNumeroDeVertices())

    // Para cada vertice en el grafo no dirigido, se consiguen los arcos que salen de el
    // y se agregan al grafo dirigo asociado
    for (verticeInicial in 0 until gPrim.obtenerNumeroDeVertices()){
        for (lado in gPrim.adyacentes(verticeInicial)){
            gPrimDirigidoAsociado.agregarArco(Arco(verticeInicial, lado.elOtroVertice(verticeInicial), lado.peso()))
        }
    }
 
    // Se ejecuta el algoritmo CFC sobre el grafo dirigido asociado  
    var esFuertementeConexo: Boolean = CFC(gPrimDirigidoAsociado).numeroDeCFC() == 1  

    // Variable que contendrá el ciclo Euleriano conseguido del grafo
    var ciclo: MutableList<Arco>

    // Si el grafo es fuertemente conexo
    if( esFuertementeConexo){

        // Se verifica si es par
        var par : Boolean = true
        for ( vertice in 0 until gPrim.obtenerNumeroDeVertices()){
            
            if (gPrim.grado(vertice) % 2 != 0){
                par = false
                break
            }
        }

        if (par){               
            // Si es par, sólo falta obtener el ciclo Euleriano
            ciclo = CicloEulerianoGrafoNoDirigido(gPrim).obtenerCicloEuleriano() as MutableList<Arco>
            
        }else{              
            // Si no es par, es necesario procesar gPrim a partir de la linea 16 del algoritmo 1 presentado en
            // el enunciado del proyecto
            ciclo = aPartirDeLinea16(gPrim, grafoCompleto,algoritmo, deGprimaAGRequerido, deGRequeridoAGPrima)
        }

    }else{

        //  Si el grafo gPrim no es conexo, se debe procesar por todas las lineas del algoritmo 1 presentado en el
        // enunciado del proyecto
        ciclo = aPartirDeLinea9(gPrim , grafoCompleto , algoritmo, deGprimaAGRequerido, deGRequeridoAGPrima)
    }
   
    // Una vez obtenido el ciclo Euleriano, se consigue el costo del ciclo
    for (arco in ciclo){
        costo += arco.peso()
    }

    // Guardar el instante cuando termina el algoritmo 
    val fin = Instant.now().toEpochMilli()
  
    println("Costo de la solucion : $costo")
    println("Tiempo que tomo encontrar la solucion : ${(fin-inicio).toDouble()/1000} segundos\n")
}

fun aPartirDeLinea16(gPrim: GrafoNoDirigido, grafoCompleto: GrafoNoDirigido, algoritmo: String, 
     deGprimaAGRequerido: MutableMap<Int, Int>, deGRequeridoAGPrima: MutableMap<Int, Int>) : MutableList<Arco>{
    /* Funcion que ejecuta el algoritmo 1 a partir de la linea 16 de este

    gPrim: Grafo prima que contiene los vertices incidentes en los lados requeridos y los lados requeridos.
    grafoCompleto: Grafo que contiene tal grafo dado como entrada en el archivo
    algoritmo: String que representa si es necesario usar el algoritmo avido o el vertexScan al buscar el camino m
    deGprimaAGRequerido: Diccionario que guarda la relacion entre los vertices de gPrima a gRequerido
    deGRequeridoAGPrima: Diccionario que guarda la relacion entre los vertices de gRequerido a gPrima    

    Retorna:
    Una lista mutable de arcos que representan el Ciclo Euleriano que se pide como solución 
    */

        // Se debe crear un nuevo grafo G0 a partir de los vertices con grado impar presentes en gPrim

        // lista con los vertices impares
        var verticesImpares : MutableList<Int> = mutableListOf()

        // Es necesarop tener la relacion del vertice i en gPrim con el vertice i en el grafo que se va a crear: gImpar.
        // Se usan diccionarios para ello de la misma forma que se usan entre gPrim y gRequerido
        var deGPrimAGImpar: MutableMap<Int, Int> = mutableMapOf()
        var deGImparAGPrim: MutableMap<Int, Int> = mutableMapOf()

        // Se cuenta la cantidad de vertices impares
        var contador = 0

        // Para cada vertice en gPrim
        for (vertice in 0..gPrim.obtenerNumeroDeVertices() - 1){            
            // Si el vertice es impar
            if (gPrim.grado(vertice) % 2 != 0){
                // Agregarlo a la lista de vertices impares y establecer la relacion entre ese vertice con el que estará
                // en gImpar
                verticesImpares.add(vertice)

                deGPrimAGImpar.put(vertice, contador)
                deGImparAGPrim.put(contador, vertice)

                contador++
            }
        }

        // Crear el grafoG0 con la cantidad de vertices impares que tiene gPrim
        var grafoG0 = GrafoNoDirigido(verticesImpares.size)

        //Determinamos el CCM entre cada par de vertices de G' para poder añadir las aristas a G0
        //Para hallar los CCM usamos el algoritmo de Dijkstra para grafos no dirigidos sobre cada vertice
        //Se puede usar porque todo cv es mayor o igual a 0

        //Se guarda el camino de costo minimo en una matriz
        var matrizCaminosCCM: Array<Array<Iterable<Arista>>> = Array(verticesImpares.size, {Array(verticesImpares.size) {mutableListOf()} })
        
        // Para cada vertice u en el grafo vertices impares
        for (u in 0..verticesImpares.size-1){

            // Se ejecuta dijstra en el grafo completo con el vertice u como fuente.
            // Como u es un vertice del grafo de vertices impares, se debe conseguir quien es u en el grafo completo.
            // Para ello es necesario ver a quien representa u en el grafo gPrim. Luego, la representacion de u en gPrim
            // se usa para conseguir el vertice en el grafo completo.            
            var dijks = DijkstraGrafoNoDirigido(grafoCompleto, deGprimaAGRequerido.get(deGImparAGPrim.get(u)!!)!!)          

            // Para cada vertice v en el grafo vertices impares
            for(v in 0..verticesImpares.size-1){
                
                // Se obtiene el camino de coste minimo de u a v en el grafo completo. De igual forma, es necesario conseguir la
                // la representacion v en grafoCompleto:
                matrizCaminosCCM[u][v] = dijks.obtenerCaminoDeCostoMinimo(deGprimaAGRequerido.get(deGImparAGPrim.get(v)!!)!!)
                
                // Se agrega la arista a g0 con el costo del camino conseguido
                grafoG0.agregarArista(Arista(u,v, dijks.costoHasta(deGprimaAGRequerido.get(deGImparAGPrim.get(v)!!)!!)))
                
            }
        }    

        // Debemos calcular el apareamiento perfecto de costo minimo de G0
        // Aqui, dependiendo de lo que se especifique como entrada, se usaran ApareamientoPerfectoAvido o ApareamientoVertexScan

        // Variable que llevara el apareamiento perfecto
        var m : MutableList<Arista> = mutableListOf()
        
        if (algoritmo == "a"){
            // Se usa ApareamientoPerfectoAvido
            m = ApareamientoPerfectoAvido(grafoG0).obtenerApareamiento() as MutableList

        }else if (algoritmo == "v") {
            // Se usa ApareamientoVertexScan
            m = ApareamientoVertexScan(grafoG0).obtenerApareamiento() as MutableList
        }     
        
        // Con el apareamiento conseguido, se deben agregar a gPrim los vertices que son incidentes en los caminos de
        // costo minimo que van de un vertice incidente a cada arista de m con el otro vertice incidente

        var contadorDeNuevosVertices = 0
        for (arista in m){

            // Obtener el CCM entre los dos lados de la arista           
            var u = arista.cualquieraDeLosVertices()
            var v = arista.elOtroVertice(u)

            var camino = matrizCaminosCCM[u][v]         
            
            for (lado in camino){
                // Es necesario recordar que los lados estan dados en vertices del grafo completo

                //Se verifica si u y v pertenecen a G' y luego
                //Se agrega el lado (u,v) a G' asi este duplicado
                var unVertice = lado.cualquieraDeLosVertices()
                var elOtroVertice = lado.elOtroVertice(unVertice)
                
                // Si no hay una representacion de gRequerido a gPrima es porque el vertice no pertenecea gPrima
                if (deGRequeridoAGPrima.get(unVertice) == null){
                    
                    // Agregar el vertice a la representacion
                    deGRequeridoAGPrima.put(unVertice ,gPrim.obtenerNumeroDeVertices() + contadorDeNuevosVertices)
                    deGprimaAGRequerido.put(gPrim.obtenerNumeroDeVertices() + contadorDeNuevosVertices, unVertice)

                    contadorDeNuevosVertices++
                }

                // Se hace lo mismo para el otro vertice en el lado
                if (deGRequeridoAGPrima.get(elOtroVertice) == null){

                    deGRequeridoAGPrima.put(elOtroVertice ,gPrim.obtenerNumeroDeVertices() + contadorDeNuevosVertices)
                    deGprimaAGRequerido.put(gPrim.obtenerNumeroDeVertices() + contadorDeNuevosVertices, elOtroVertice)

                    contadorDeNuevosVertices++
                }               
            }
        }
       
        // Crear nuevo grafo gPrim con los vertices que se deben agregar, este grafo se llamara gTemp
        var gTemp = GrafoNoDirigido(gPrim.obtenerNumeroDeVertices() + contadorDeNuevosVertices)
       
        for (lado in gPrim.aristas()){
            gTemp.agregarArista(lado)
        }

        // Agregar los nuevos lados a gTemp
        for (arista in m){

            // Obtener el CCM entre los dos lados de la arista            
            var u = arista.cualquieraDeLosVertices()
            var v = arista.elOtroVertice(u)

            var camino = matrizCaminosCCM[u][v]
          
 
            for (lado in camino){
                // Para cada lado en el camino, se agrega el lado a gPrima. Como los lados en el camino tienen los vertices
                // representados en el grafoCompleto, es necesario conseguir su representacion el grafoGPrima

                var unVertice = lado.cualquieraDeLosVertices()
                var elOtro = lado.elOtroVertice(unVertice)       

                gTemp.agregarArista(Arista(deGRequeridoAGPrima.get(unVertice)!!, deGRequeridoAGPrima.get(elOtro)!!, lado.peso())) 
            }
        }

        // gTemp ahora es el nuevo gPrima, se consigue el ciclo Euleriano de gTemp y se retorna
        return CicloEulerianoGrafoNoDirigido(gTemp).obtenerCicloEuleriano() as MutableList<Arco>
}

fun aPartirDeLinea9(gPrim: GrafoNoDirigido, grafoCompleto: GrafoNoDirigido, algoritmo: String, 
    deGprimaAGRequerido: MutableMap<Int, Int>, deGRequeridoAGPrima: MutableMap<Int, Int>) : MutableList<Arco>{
    /* Funcion que ejecuta el algoritmo 1 a partir de la linea 9 de este

    gPrim: Grafo prima que contiene los vertices incidentes en los lados requeridos y los lados requeridos.
    grafoCompleto: Grafo que contiene tal grafo dado como entrada en el archivo
    algoritmo: String que representa si es necesario usar el algoritmo avido o el vertexScan al buscar el camino m
    deGprimaAGRequerido: Diccionario que guarda la relacion entre los vertices de gPrima a gRequerido
    deGRequeridoAGPrima: Diccionario que guarda la relacion entre los vertices de gRequerido a gPrima    

    Retorna:
    Una lista mutable de arcos que representan el Ciclo Euleriano que se pide como solución 
    */

        // Obtenemos las componentes conexas de gPrim para hacer Gt
        val compConexas = ComponentesConexasDFS(gPrim)

        // Se crea Gt con |V| = numero de CC de G' 
        var grafoGt : GrafoNoDirigido = GrafoNoDirigido(compConexas.numeroDeComponentesConexas())

        // Matriz booleana para llevar la cuenta de las componentes a las que ya se les ha asignado una arista
        var componentesConLado: Array<Array<Boolean>> = Array(compConexas.numeroDeComponentesConexas(), {Array(
            compConexas.numeroDeComponentesConexas(), {false}
        )})
        
        // Matriz de iterables que contiene el camino de coste minimo en el grafoCompleto desde el vertice v hasta el
        // vertice u.
        var caminosCostoMinimo: MutableList<MutableList<Iterable<Arista>?>> = MutableList(compConexas.numeroDeComponentesConexas(), {
            MutableList(compConexas.numeroDeComponentesConexas(), {mutableListOf()})
        })
        
        // Ahora se deben conseguir los lados del grafo GT. Para ello, se consiguen los caminos
        // de costo minimo entre los vertices que componen cada componente. El lado entre una componenteI
        // y otra componenteJ debe tener como peso el menor costo del camino que va de un vertice en componenteI
        // a cualquier otro vertice en la componenteJ

        // Para cada componente i en Gt
        for (componenteI in 0 until grafoGt.obtenerNumeroDeVertices()){
            // Para cada componente j en Gt
            for (componenteJ in 0 until grafoGt.obtenerNumeroDeVertices()){
                
                // Se continua si es la misma componente
                if (componenteI == componenteJ){
                    continue
                }
                // Si ya se ha asignado un lado a las componentes, se continua
                if (componentesConLado[componenteI][componenteJ]){
                    continue
                }

                // Se consigue la lista de vertices pertenenecientes al grafo gPrima que estan presentes
                // en la componenteI, lo mismo para la componenteJ
                var verticesComponenteI = compConexas.verticesEnComponenteConexa(componenteI)
                var verticesComponenteJ = compConexas.verticesEnComponenteConexa(componenteJ)

                var costo: Double = Double.POSITIVE_INFINITY
                // Para cada verticeI que pertenece a la componente I
                for (verticeI in verticesComponenteI){
                    
                    // Se consigue el camino de costo minimo con verticeI como fuente.
                    // Como dijkstra se ejecuta en el grafo completo, es necesario conseguir la representacion
                    // de verticeI en gRequerido
                    var dijks = DijkstraGrafoNoDirigido(grafoCompleto, deGprimaAGRequerido.get(verticeI)!!)

                    // Para cada verticeJ que pertenece a la componenteJ
                    for (verticeJ in verticesComponenteJ){

                        // Se consigue la representacion del verticeJ en el grafo completo. Esta vez se 
                        // guarda en una variable
                        var verticeJEnGrafoCompleto = deGprimaAGRequerido.get(verticeJ)!!

                        // Si el costo desde verticeI a verticeJ en grafo completo es menor al computado
                        // anteriormente, se actualiza el nuevo costo. Ademas, se guarda en la matriz de
                        // CaminosCostoMinimo el camino asociado a este costo
                        if(dijks.costoHasta(verticeJEnGrafoCompleto) < costo){                            

                            costo = dijks.costoHasta(verticeJEnGrafoCompleto)

                            // Como Gt es un grafoNoDirigido, el camino de componenteI a componenteJ es el mismo
                            // que va de componenteJ a componenteI.
                            caminosCostoMinimo[componenteI][componenteJ] = dijks.obtenerCaminoDeCostoMinimo(verticeJEnGrafoCompleto)
                            caminosCostoMinimo[componenteJ][componenteI] = dijks.obtenerCaminoDeCostoMinimo(verticeJEnGrafoCompleto)
                        }
                    }
                }

                // Se agrega la arista conseguida con el menor costo
                grafoGt.agregarArista(Arista(componenteI, componenteJ, costo))

                // Se asigna que las componentes ya tienen un lado asignado
                componentesConLado[componenteI][componenteJ] = true
                componentesConLado[componenteJ][componenteI] = true
            }
        }
       
        // Luego de obtener Gt se calcula el arbol cobertor del mismo
        // Usaremos la clase ArbolMinimoCobertorPrim para ello

        var arbolMin = ArbolMinimoCobertorPrim(grafoGt, 0)

        // Se obtienen los lados del arbol cobertor
        var ladosEt = arbolMin.obtenerLados()

        // Conseguir los vertices de Et que no estan en gPrim
        var cantidadDeNuevosVertices = 0

        for (lado in ladosEt){
            // Conseguir el camino de coste minimo asociado al lado
            var unaComponente = lado.cualquieraDeLosVertices()
            var camino = caminosCostoMinimo[unaComponente][lado.elOtroVertice(unaComponente)]!!

            for (arista in camino){

                var unVertice = arista.cualquieraDeLosVertices()
                var elOtroVertice = arista.elOtroVertice(unVertice)

                // comprobar que los vertices del camino estan en gPrim. Si el vertice no tiene una representacion
                // en el diccionario es porque no pertenece a gPrim.
                if(deGRequeridoAGPrima.get(unVertice) == null){

                    // Se crea la representacion del vertice en gPrim
                    deGRequeridoAGPrima.put(unVertice, gPrim.obtenerNumeroDeVertices() + cantidadDeNuevosVertices)
                    deGprimaAGRequerido.put(gPrim.obtenerNumeroDeVertices() + cantidadDeNuevosVertices, unVertice)

                    cantidadDeNuevosVertices++
                }

                // Se hace lo mismo para el otro vertice
                if(deGRequeridoAGPrima.get(elOtroVertice) == null){
                    deGRequeridoAGPrima.put(unVertice, gPrim.obtenerNumeroDeVertices() + cantidadDeNuevosVertices)
                    deGprimaAGRequerido.put(gPrim.obtenerNumeroDeVertices() + cantidadDeNuevosVertices, unVertice)

                    cantidadDeNuevosVertices++
                }
            }
        }   

        // Agregar los vertices nuevos a gPrim. Se crea gTemp como grafo temportal y se copia a gPrim
        // en gTemp
        var gTemp = GrafoNoDirigido(gPrim.obtenerNumeroDeVertices() + cantidadDeNuevosVertices)

        for (lado in gPrim.aristas()){
            gTemp.agregarArista(lado)
        }
    
        // Se agregan los nuevos lados a gTemp
        for (lado in ladosEt){
            // ladosEt son los lados del arbol
            var unVertice = lado.cualquieraDeLosVertices()

            // Se obtiene el camino de costo minimo de una componente a la otra 
            var caminoCosteMinimo = caminosCostoMinimo[unVertice][lado.elOtroVertice(unVertice)]!!

            // Para cada arista en en el camino, se agrega una arista a gTemp.
            // Como la arista pertenece a un camino del grafoCompleto, es necesario conseguir la representacion
            // de los vertices de grafoCompleto en gPrima para agregarlos
            for (arista in caminoCosteMinimo){
                var unoDeLosVertices = arista.cualquieraDeLosVertices()
                var elOtroDeLosVertice = arista.elOtroVertice(unoDeLosVertices)

                gTemp.agregarArista(Arista(
                    deGRequeridoAGPrima.get(unoDeLosVertices)!!, deGRequeridoAGPrima.get(elOtroDeLosVertice)!!, arista.peso()))
            }
        }

    // A partir de este momento gTemp es gPrim, y es conexo. Por tanto, se ejecutan las lineas 16 del algoritmo 1
    // y se retorna el ciclo euleriano
    return aPartirDeLinea16(gTemp, grafoCompleto, algoritmo, deGprimaAGRequerido, deGRequeridoAGPrima)
}