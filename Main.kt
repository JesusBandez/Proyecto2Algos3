import ve.usb.grafoLib.*


fun main(){
    var g = GrafoNoDirigido("test.txt", true)
    var ciclo = CicloEulerianoGrafoNoDirigido(g)
    
    println("ciclo: ${ciclo.obtenerCicloEuleriano()}")
    
}
