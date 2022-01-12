import ve.usb.grafoLib.*


fun main(){
    var g = GrafoNoDirigido("test.txt", true)
    var gdij = DijkstraGrafoNoDirigido(g, 0)
    
    var v = 5
    println(gdij.existeUnCamino(v))
    println(gdij.costoHasta(v))
    println(gdij.obtenerCaminoDeCostoMinimo(v))
    
}
