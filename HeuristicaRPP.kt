
import java.io.File
import java.time.Instant

fun main(args: Array<String>) {
    
    // Cuando se lee el txt agregar la arista (u,v) con costo cv1 y la arita (v,u) con costo cv2
    // Modificar agregar arista para este fin

    val inicio = Instant.now().toEpochMilli()

    val algoritmo : String = args[0]
    val instancia : String = args[1]  
    var aristasReq : Int 
    var vertices : Int
    var nombre : String
    var componentes : Int
    var costo : Double = 0.0
    


    var archivo = File(instancia).bufferedReader().readLines()

    var linea0 = archivo[0].split(" ").toList()
    linea0 = linea0.filter { it != "" && it != " "}
    nombre = linea0[2]

    var linea1 = archivo[1].split(" ").toList()
    linea1 = linea1.filter { it != "" && it != " "}
    componentes = linea1[2].toInt() 

    var linea2 = archivo[2].split(" ").toList()
    linea2 = linea2.filter { it != "" && it != " "}
    vertices = linea2[2].toInt()

    var linea3 = archivo[3].split(" ").toList()
    linea3 = linea3.filter { it != "" && it != " "}
    aristasReq = linea3[2].toInt()


    println(nombre)
    println(componentes)
    /*  
    println(vertices)    
    println(aristasReq)  
    println(aristasNoReq) */

    
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
        
        //println("Linea ${i + 1}")
        //println("u : $u , v : $v , cv1 : $cv1 , cv2 : $cv2 ")


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
        
        //println("Linea ${i + 1}")
        //println(linea)

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

        //println("u : $u , v : $v , cv1 : ${cv1} , cv2 : ${cv2} ")  
        // Por ahor asumamos que cv1 = cv2. Agregar un arista agrega los dos arcos
        gRequerido.agregarArista(Arista(u,v,cv1))
        if (cv1 != cv2 ){
            print("error, pesos distintos")
        }
        
    }


    // Se establece un "isomorfismo" entre gRequerido y gPrima
    // Diccionarios usados para conservar la relacion entre un vertice en grafo gRequerido y
    // un vertice en el grafo gPrima. En caso de que se quiera buscar el vertice i de gRequerido
    // en gPrima, se usa vertice = deGRequeridoAGPrima.get(i). Si se quiere lo contrario, se usa
    // vertice = deGprimaAGRequerido(i)

    var deGprimaAGRequerido: MutableMap<Int, Int> = mutableMapOf()
    var deGRequeridoAGPrima: MutableMap<Int, Int> = mutableMapOf()
    var contadorDeVerticesRequeridos: Int = 0

    // Ahora se consigue gPrim
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
    var ciclo: MutableList<Arco>

    if( esFuertementeConexo){

        // Se verifica si el grafo es par
        var par : Boolean = true
        for ( vertice in 0 until gPrim.obtenerNumeroDeVertices()){
            
            if (gPrim.grado(vertice) % 2 != 0){

                par = false
                break
            }
        }

        // Si es par se obtiene el ciclo euleriano
        if (par){            
            // Se debe cambiar ciclo Euleriano para que funcion con grafos no dirigidos////////////////////////////////////////////
            ciclo = CicloEulerianoGrafoNoDirigido(gPrim).obtenerCicloEuleriano() as MutableList<Arco>
            
        }else{

            println("no es par") 
            ciclo = aPartirDeLinea16(gPrim, grafoCompleto,algoritmo, deGprimaAGRequerido, deGRequeridoAGPrima)

        }

    }else{
        // G' no es conexo
        println("No es conexo")
        ciclo = aPartirDeLinea9(gPrim , grafoCompleto , algoritmo, deGprimaAGRequerido, deGRequeridoAGPrima)

    }
   
    for (arco in ciclo){
        costo += arco.peso()
    }

    val fin = Instant.now().toEpochMilli()

    //println("\nInstancia : $nombre")
    //println("\nSolucion al RPP :")
    //println(ciclo.joinToString(" "))
    println("\nCosto de la solucion : $costo")

    println("\nTiempo que tomo encontrar la solucion : ${(fin-inicio).toDouble()/1000} segundos")

    // Construir Grafo completo Gp    
}

fun aPartirDeLinea16(gPrim: GrafoNoDirigido, grafoCompleto: GrafoNoDirigido, algoritmo: String, 
     deGprimaAGRequerido: MutableMap<Int, Int>, deGRequeridoAGPrima: MutableMap<Int, Int>) : MutableList<Arco>{
       
         
        //linea 16 en adelante

        // Si el grafo es conexo pero no par, 
        // Se crea un nuevo grafo G0 a partir de los vertices con grado impar de G'
        var verticesImpares : MutableList<Int> = mutableListOf()

        // Es necesarop tener la relacion del vertice i en gPrim con el vertice i en gImpar.
        // Se usan diccionarios para ello
        var deGPrimAGImpar: MutableMap<Int, Int> = mutableMapOf()
        var deGImparAGPrim: MutableMap<Int, Int> = mutableMapOf()
        var contador = 0

        for (vertice in 0..gPrim.obtenerNumeroDeVertices() - 1){

            if (gPrim.grado(vertice) % 2 != 0){
                verticesImpares.add(vertice)

                deGPrimAGImpar.put(vertice, contador)
                deGImparAGPrim.put(contador, vertice)
                contador++
            }
        }

        
        var grafoG0 = GrafoNoDirigido(verticesImpares.size)

        //Determinamos el CCM entre cada par de vertices de G' para poder añadir las aristas a G0
        //Para hallar los CCM usamos el algoritmo de Dijkstra para grafos no dirigidos sobre cada vertice
        //Se puede usar porque todo cv es mayor o igual a 0
        //Se añade cada objeto Dijkstra a una lista porque mas adelante se necesitan los caminos de costo minimo

        var listaDijkstra : MutableList<DijkstraGrafoNoDirigido> = mutableListOf()
        var matrizCCM  = Array(verticesImpares.size){Array(verticesImpares.size) {Double.POSITIVE_INFINITY} }

        for (u in 0..verticesImpares.size-1){

            var dijks = DijkstraGrafoNoDirigido(grafoCompleto, deGprimaAGRequerido.get(deGImparAGPrim.get(u)!!)!!)
            listaDijkstra.add(dijks)

            for(v in 0..verticesImpares.size-1){

                matrizCCM[u][v] = dijks.costoHasta(deGprimaAGRequerido.get(deGImparAGPrim.get(v)!!)!!)

                if (matrizCCM[u][v] < Double.POSITIVE_INFINITY){
                    grafoG0.agregarArista(Arista(u,v, matrizCCM[u][v]))
                }
            }
        }
    
        // Linea 17
        // Debemos calcular el apareamiento perfecto de costo minimo de G0
        // Aqui, dependiendo de lo que se especifique como entrada, se usaran ApareamientoPerfectoAvido o ApareamientoVertexScan

        var m : MutableList<Arista> = mutableListOf()
        if (algoritmo == "a"){

            // Se usa ApareamientoPerfectoAvido

            m = ApareamientoPerfectoAvido(grafoG0).obtenerApareamiento() as MutableList

        }else if (algoritmo == "v") {

            // Se usa ApareamientoVertexScan

            m = ApareamientoVertexScan(grafoG0).obtenerApareamiento() as MutableList
        }
        /* 
        for (arco in m){
            var unV = arco.cualquieraDeLosVertices()
            var elOt = arco.elOtroVertice(unV)
            
            println("[${deGImparAGPrim.get(unV)}-${deGImparAGPrim.get(elOt)} : ${arco.peso()}]")
        }
        */

        var contadorDeNuevosVertices = 0
        for (arista in m){

            // Obtener el CCM entre los dos lados de la arista
            // Como ya se calcularon con Dijkstra, se usa el metodo caminoHasta para obtenerlos
            var u = arista.cualquieraDeLosVertices()
            var v = arista.elOtroVertice(u)
            var camino = listaDijkstra[u].obtenerCaminoDeCostoMinimo( deGprimaAGRequerido.get(deGImparAGPrim.get(v)!!)!! )
            print("Camino que no se que hace: $camino")
            
            for (lado in camino){
                // El camino está dado en vertices de gCompleto


                //Se verifica si u y v pertenecen a G' y luego
                //Se agrega el lado (u,v) a G' asi este duplicado
                var unVertice = lado.cualquieraDeLosVertices()
                var elOtroVertice = lado.elOtroVertice(unVertice)
                
                if (deGRequeridoAGPrima.get(unVertice) == null){

                    deGRequeridoAGPrima.put(unVertice ,gPrim.obtenerNumeroDeVertices() + contadorDeNuevosVertices)
                    deGprimaAGRequerido.put(gPrim.obtenerNumeroDeVertices() + contadorDeNuevosVertices, unVertice)
                    contadorDeNuevosVertices++
                }
               // Si no v esta en gPrim
                if (deGRequeridoAGPrima.get(elOtroVertice) == null){

                    deGRequeridoAGPrima.put(elOtroVertice ,gPrim.obtenerNumeroDeVertices() + contadorDeNuevosVertices)
                    deGprimaAGRequerido.put(gPrim.obtenerNumeroDeVertices() + contadorDeNuevosVertices, elOtroVertice)
                    contadorDeNuevosVertices++
                }               
            }
        }
        println("Nuevos vertices: $contadorDeNuevosVertices")
        // Crear nuevo grafo gPrim con los vertices agregados
        var gTemp = GrafoNoDirigido(gPrim.obtenerNumeroDeVertices() + contadorDeNuevosVertices)
       
        for (lado in gPrim.aristas()){
            gTemp.agregarArista(lado)
        }

        // Agregar los nuevos lados a gTemp
                   
        
        for (arista in m){

            // Obtener el CCM entre los dos lados de la arista
            // Como ya se calcularon con Dijkstra, se usa el metodo caminoHasta para obtenerlos
            var u = arista.cualquieraDeLosVertices()
            var v = arista.elOtroVertice(u)
            var camino = listaDijkstra[u].obtenerCaminoDeCostoMinimo( deGprimaAGRequerido.get(deGImparAGPrim.get(v)!!)!! )
          
 
            for (lado in camino){
                var unVertice = lado.cualquieraDeLosVertices()
                var elOtro = lado.elOtroVertice(unVertice)              
                gTemp.agregarArista(Arista(deGRequeridoAGPrima.get(unVertice)!!, deGRequeridoAGPrima.get(elOtro)!!, lado.peso()))
 
            }
        }
        println("gTemp last:")
        println(gTemp)
        return CicloEulerianoGrafoNoDirigido(gTemp).obtenerCicloEuleriano() as MutableList<Arco>
}

fun aPartirDeLinea9(gPrim: GrafoNoDirigido, grafoCompleto: GrafoNoDirigido, algoritmo: String, 
    deGprimaAGRequerido: MutableMap<Int, Int>, deGRequeridoAGPrima: MutableMap<Int, Int>) : MutableList<Arco>{

        // Obtenemos las componentes conexas de G' para hacer Gt
        val compConexas = ComponentesConexasDFS(gPrim)
        // Se crea Gt con |V| = numero de CC de G' 
        var grafoGt : GrafoNoDirigido = GrafoNoDirigido(compConexas.numeroDeComponentesConexas())

        var componentesConLado: Array<Array<Boolean>> = Array(compConexas.numeroDeComponentesConexas(), {Array(
            compConexas.numeroDeComponentesConexas(), {false}
        )})
        
        var caminosCostoMinimo: MutableList<MutableList<Iterable<Arista>?>> = MutableList(compConexas.numeroDeComponentesConexas(), {
            MutableList(compConexas.numeroDeComponentesConexas(), {mutableListOf()})
        })

        for (componenteI in 0 until grafoGt.obtenerNumeroDeVertices()){
            for (componenteJ in 0 until grafoGt.obtenerNumeroDeVertices()){

                if (componenteI == componenteJ){
                    continue
                }

                if (componentesConLado[componenteI][componenteJ]){
                    continue
                }

                var verticesComponenteI = compConexas.verticesEnComponenteConexa(componenteI)
                var verticesComponenteJ = compConexas.verticesEnComponenteConexa(componenteJ)

                var costo: Double = Double.POSITIVE_INFINITY
                for (verticeI in verticesComponenteI){

                    var dijks = DijkstraGrafoNoDirigido(grafoCompleto, deGprimaAGRequerido.get(verticeI)!!)
              
                    for (verticeJ in verticesComponenteJ){
                        var verticeJEnGrafoCompleto = deGprimaAGRequerido.get(verticeJ)!!

                        if(dijks.costoHasta(verticeJEnGrafoCompleto) < costo){
                            

                            costo = dijks.costoHasta(verticeJEnGrafoCompleto)

                            caminosCostoMinimo[componenteI][componenteJ] = dijks.obtenerCaminoDeCostoMinimo(verticeJEnGrafoCompleto)
                            caminosCostoMinimo[componenteJ][componenteI] = dijks.obtenerCaminoDeCostoMinimo(verticeJEnGrafoCompleto)
                        }
                    }
                }

                grafoGt.agregarArista(Arista(componenteI, componenteJ, costo))
                componentesConLado[componenteI][componenteJ] = true
                componentesConLado[componenteJ][componenteI] = true
            }
        }
       
        // Luego de obtener Gt se calcula el arbol cobertor del mismo
        // Usaremos la clase ArbolMinimoCobertorPrim para ello

        var arbolMin = ArbolMinimoCobertorPrim(grafoGt, 0)
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

                // comprobar que los vertices del camino estan en gPrim
                if(deGRequeridoAGPrima.get(unVertice) == null){
                    deGRequeridoAGPrima.put(unVertice, gPrim.obtenerNumeroDeVertices() + cantidadDeNuevosVertices)
                    deGprimaAGRequerido.put(gPrim.obtenerNumeroDeVertices() + cantidadDeNuevosVertices, unVertice)

                    cantidadDeNuevosVertices++
                }

                if(deGRequeridoAGPrima.get(elOtroVertice) == null){
                    deGRequeridoAGPrima.put(unVertice, gPrim.obtenerNumeroDeVertices() + cantidadDeNuevosVertices)
                    deGprimaAGRequerido.put(gPrim.obtenerNumeroDeVertices() + cantidadDeNuevosVertices, unVertice)

                    cantidadDeNuevosVertices++
                }
            }
        }

        println("de gPrima a gRequerido")
        println(deGprimaAGRequerido)
        println("gPrimaOriginal")
        print(gPrim)

        // Agregar los vertices a gPrima
        var gTemp = GrafoNoDirigido(gPrim.obtenerNumeroDeVertices() + cantidadDeNuevosVertices)

        for (lado in gPrim.aristas()){
            gTemp.agregarArista(lado)
        }
        println("ladosEt: $ladosEt")
        for (lado in ladosEt){
            // ladosEt son los lados del arbol
            var unVertice = lado.cualquieraDeLosVertices()

            var caminoCosteMinimo = caminosCostoMinimo[unVertice][lado.elOtroVertice(unVertice)]!!
            println(caminoCosteMinimo)
            for (arista in caminoCosteMinimo){

                var unoDeLosVertices = arista.cualquieraDeLosVertices()
                var elOtroDeLosVertice = arista.elOtroVertice(unoDeLosVertices)
                gTemp.agregarArista(Arista(
                    deGRequeridoAGPrima.get(unoDeLosVertices)!!, deGRequeridoAGPrima.get(elOtroDeLosVertice)!!, arista.peso()))
            }
        }
    println("gTemp antes de linea 16:")
    println(gTemp)

    return aPartirDeLinea16(gTemp, grafoCompleto, algoritmo, deGprimaAGRequerido, deGRequeridoAGPrima)
}